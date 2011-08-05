package org.sol.util.socket.message;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class ContentFactory {
	private static int _seq = Integer.MIN_VALUE;

	public static final String headerXml = "xml/header.xml";
	
	public static final String commandMappingXml = "xml/CommandMapping.xml";
	
	/**
	 * 数据头的模板
	 */
	private static XMLHeader headerTemplate;

	/**
	 * 指令匹配表
	 */
	private static final Map<Integer,File> commandMap = new HashMap<Integer,File>();
	public static String packetLengthFieldName;
	public static String commandFieldName;
	public static String sequenceFieldName;
	
	/**
	 * 初始化指令匹配表
	 */
	static {
		try {
			genarateCommandMap();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (JDOMException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * 从数据头的指令获取数据包模板
	 * @param header
	 * @return
	 */
	public static XMLContent getContent(XMLHeader header) {
		File file = commandMap.get(header.getItemValue(commandFieldName));
		XMLContent content = genarateContent(file);
		content.setHeader(header);
		return content;
	}
	
	public static PacketContent getContentResp(PacketContent content,String respFilename) {
		XMLHeader header = getHeaderTemplate();
		File file = commandMap.get(content.getHeader().getCommand());
		
		Integer command = findCommandByName(file.getName());
		if(command == null)
			throw new XMLParseException("not exist filename to command - filename:" + file.getName());
		for(XMLItem item : ((XMLHeader)content.getHeader()).getItems()) 
			header.setItemValue(item.getName(), item.getValue());
		header.setItemValue(packetLengthFieldName, -1);
		header.setItemValue(commandFieldName, findCommandByName(respFilename + ".xml"));
		
		XMLContent resp = genarateContent(new File("xml/" + respFilename + ".xml"));
		resp.setHeader(header);
		
		return resp;
	}
	
	/**
	 * 从指定的文件获取数据包模板
	 * @param file
	 * @return
	 */
	public static XMLContent getContent(File file) {
		XMLHeader header = getHeaderTemplate();
		
		Integer command = findCommandByName(file.getName());
		if(command == null)
			throw new XMLParseException("not exist filename to command - filename:" + file.getName());
		header.setItemValue(commandFieldName, command.intValue());
		header.setItemValue(sequenceFieldName, getNextSequnce());
		
		XMLContent content = genarateContent(file);
		content.setHeader(header);
		
		return content;
	}
	
	/**
	 * 从指定的文件获取数据包模板
	 * @param filename 文件名 xml/filename.xml -> filename
	 * @return
	 */
	public static XMLContent getContent(String filename) {
		return getContent(new File("xml",filename + ".xml"));
	}
	
	/**
	 * 初始化指令匹配表
	 * @throws IOException
	 * @throws JDOMException
	 */
	@SuppressWarnings("unchecked")
	private static void genarateCommandMap() throws IOException, JDOMException {
		File file = new File(commandMappingXml);
		SAXBuilder sax = new SAXBuilder();

		Document doc = sax.build(file);
		Element root = doc.getRootElement();
		commandFieldName = root.getAttributeValue("commandField");
		packetLengthFieldName = root.getAttributeValue("packetLengthField");
		sequenceFieldName = root.getAttributeValue("sequenceField");
		
		List<Element> commandList = root.getChildren("Map");
		for(Element map : commandList) {
			int command = Integer.parseInt(map.getAttributeValue("command"),16);
			File cfile = new File("xml",map.getAttributeValue("file"));
			if(!cfile.exists())
				throw new IOException("not exist content mappgin file - filename:" + map.getAttributeValue("file"));
			commandMap.put(command, cfile);
		}
	}
	
	/**
	 * 根据文件名获取指令
	 * @param filename
	 * @return
	 */
	private static Integer findCommandByName(String filename) {
		Set<Entry<Integer,File>> set = commandMap.entrySet();
		for(Entry<Integer,File> en : set) {
			if(filename.equals(en.getValue().getName()))
				return en.getKey();
		}
		return null;
	}
	
	/**
	 * 从数据包中提取指令
	 * @param content
	 * @return
	 */
	public static Integer getPacketCommand(PacketContent content) {
		PacketHeader header = content.getHeader();
		if(header instanceof XMLHeader)
			return (Integer)((XMLHeader)header).getItemValue(commandFieldName);
		else if(header instanceof Header)
			return ((Header)header).getCommand();
		else
			return null;
	}
	
	/**
	 * 获取一个数据头模板
	 * @return
	 * @throws CloneNotSupportedException 
	 */
	public static XMLHeader getHeaderTemplate(){
		if(headerTemplate == null) {
			synchronized (ContentFactory.class) {
				headerTemplate = genarateHeader(new File(headerXml));
			}
		}
			
		try {
			return headerTemplate.newInstance();
		} catch (CloneNotSupportedException e) {
			return genarateHeader(new File(headerXml));
		}
	}

	public static void setHeaderTemplate(XMLHeader headerTemplate) {
		ContentFactory.headerTemplate = headerTemplate;
	}

	/**
	 * 解析数据头结构
	 * @param file
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static XMLHeader genarateHeader(File file) {
		SAXBuilder sax = new SAXBuilder();
		try {
			Document doc = sax.build(file);
			
			Element root = doc.getRootElement();
			List itemList = root.getChildren("Item");
			XMLHeader header = new XMLHeader();
			
			int headerSize = 0;
			for(Iterator it = itemList.iterator(); it.hasNext();) {
				Element item = (Element)it.next();
				String name = item.getAttributeValue("name");
				String type = item.getAttributeValue("type");
				int size = Integer.parseInt(item.getAttributeValue("size"));
				header.addItem(name, type, size);
				headerSize += size;
			}
			header.setHeaderLength(headerSize);

			return header;
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解析数据包结构
	 * @param header
	 * @param file
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static XMLContent genarateContent(File file) {
		SAXBuilder sax = new SAXBuilder();
		try {
			Document doc = sax.build(file);
			
			Element root = doc.getRootElement();
			List itemList = root.getChildren("Item");
			XMLContent content = new XMLContent();
			
			XMLItem lengthFieldItem;
			
			for(Iterator it = itemList.iterator(); it.hasNext();) {
				Element item = (Element)it.next();
				String name = item.getAttributeValue("name");
				String type = item.getAttributeValue("type");
				int size = Integer.parseInt(item.getAttributeValue("size"));
				if(size == 0) {
					String lengthField = item.getAttributeValue("lengthField");
					lengthFieldItem = content.addItem(lengthField, "int", 4);
					
					content.addItem(name, type, size,lengthFieldItem);
					lengthFieldItem = null;
				} else {
					content.addItem(name, type, size);
				}
			}

			return content;
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	/**
	 * 获取下一个序列号
	 * @return
	 */
	public static int getNextSequnce() {
		if(_seq == Integer.MAX_VALUE)
			_seq = Integer.MIN_VALUE;
		
		return ++_seq;
	}


}
