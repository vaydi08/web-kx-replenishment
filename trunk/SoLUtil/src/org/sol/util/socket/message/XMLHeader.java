package org.sol.util.socket.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.sol.util.socket.DataInputStream;
import org.sol.util.socket.DataOutputStream;

/**
 * XML 数据包头
 * @author HUYAO
 *
 */
public class XMLHeader implements Cloneable, PacketHeader{
	private int headerLength;
	
	private List<XMLItem> items;
	
	
	/* (non-Javadoc)
	 * @see org.sol.util.socket.message.PacketHeader#getBytes()
	 */
	public ByteArrayOutputStream getBytes() throws IOException {
		return getBytes(null);
	}
	
	/**
	 * 字节化 charset编码
	 * @return
	 * @throws IOException 
	 */
	public ByteArrayOutputStream getBytes(String charset) throws IOException {
		int packetLength = Integer.parseInt(getItemValue(ContentFactory.packetLengthFieldName).toString());
		ByteArrayOutputStream os = new ByteArrayOutputStream(packetLength);
		DataOutputStream ds = new DataOutputStream(os);
		
		if(items != null) {
			for(XMLItem item : items) {
				if(item.getValue() == null)
					throw new IOException("unset [" + item.getName() + "] value,please call setItemValue()");
				if(item.getType().equals(int.class))
					ds.writeInt(Integer.valueOf(item.getValue().toString()));
				else if(item.getType().equals(String.class))
					if(charset == null || charset.equals(""))
						ds.writeString(item.getValue().toString());
					else
						ds.writeString(item.getValue().toString(),charset);
			}
		}
		return os;
	}
	
	/**
	 * 对象化
	 * @param data
	 * @throws IOException 
	 */
	public void parseBytes(byte[] data) throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(data);
		DataInputStream ds = new DataInputStream(is);
		if(items != null) {
			for(XMLItem item : items) {
				if(item.getType().equals(int.class))
					item.setValue(ds.readInt());
				else if(item.getType().equals(String.class))
					item.setValue(ds.readString(item.getSize()));
			}
		}
	}
	
	/**
	 * 设置字段的值
	 * @param name
	 * @param value
	 */
	public void setItemValue(String name,Object value) {
		for(XMLItem item : items) {
			if(item.getName().equals(name)) {
				item.setValue(value);
				return;
			}
		}
		
		throw new XMLParseException("not exist field - " + name);
	}
	
	/**
	 * 获取字段的值
	 * @param name
	 * @return
	 */
	public Object getItemValue(String name) {
		for(XMLItem item : items) {
			if(item.getName().equals(name)) 
				return item.getValue();
			
		}
		
		throw new XMLParseException("not exist field - " + name);
	}
	
	/**
	 * 添加字段定义
	 * @param name
	 * @param type
	 * @param size
	 */
	public void addItem(String name,String type,int size){
		if(items == null)
			items = new ArrayList<XMLItem>();
		
		XMLItem item = new XMLItem();
		item.setName(name);
		item.setSize(size);
		
		if(type.equals("int"))
			item.setType(int.class);
		else if(type.equals("String"))
			item.setType(String.class);
		else
			throw new XMLParseException("Error bound field type - " + type);
		
		items.add(item);
	}
	
	public XMLHeader newInstance() throws CloneNotSupportedException {
		return (XMLHeader)clone();
	}
	
	/* (non-Javadoc)
	 * @see org.sol.util.socket.message.PacketHeader#getPacketLength()
	 */
	public Integer getPacketLength() {
		return Integer.parseInt(getItemValue(ContentFactory.packetLengthFieldName).toString());
	}
	
	/* (non-Javadoc)
	 * @see org.sol.util.socket.message.PacketHeader#getCommand()
	 */
	public Integer getCommand() {
		return Integer.parseInt(getItemValue(ContentFactory.commandFieldName).toString());
	}
	

	/* (non-Javadoc)
	 * @see org.sol.util.socket.message.PacketHeader#getSequnce()
	 */
	public Integer getSequence() {
		return Integer.parseInt(getItemValue(ContentFactory.sequenceFieldName).toString());
	}
	
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		Object o = getItemValue(ContentFactory.packetLengthFieldName);
		int packetLength = (o == null) ? -1 : Integer.parseInt(o.toString());
		out.append(String.format("Header Length:%d PacketLength:%d\n", headerLength,packetLength));
		if(items != null) {
			for(XMLItem item : items)
				out.append(String.format("\tName:%s Type:%s Size:%x Value:%s\n", item.getName(),item.getType().getSimpleName(),item.getSize(),item.getValue()));
		}
		return out.toString();
	}

	public Integer getHeaderLength() {
		return headerLength;
	}

	public void setHeaderLength(int headerLength) {
		this.headerLength = headerLength;
	}

	public List<XMLItem> getItems() {
		return items;
	}

	public void setItems(List<XMLItem> items) {
		this.items = items;
	}


}
