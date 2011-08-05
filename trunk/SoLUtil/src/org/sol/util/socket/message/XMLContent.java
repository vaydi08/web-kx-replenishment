package org.sol.util.socket.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.sol.util.socket.DataInputStream;
import org.sol.util.socket.DataOutputStream;

public class XMLContent implements PacketContent {
	protected XMLHeader header;
	
	private List<XMLItem> items;

	/**
	 * 字节化 带编码
	 * @return
	 * @throws IOException
	 */
	public byte[] getBytes(String charset) throws IOException {
		calculatePacketLength();
		ByteArrayOutputStream os = (charset ==null || charset.equals("") ? header.getBytes() : header.getBytes(charset));
		DataOutputStream ds = new DataOutputStream(os);
		
		if(items != null) {
			for(XMLItem item : items) {
				if(item.getType().equals(int.class))
					ds.writeInt(Integer.valueOf(item.getValue().toString()).intValue());
				else if(item.getType().equals(String.class))
					ds.writeString(item.getValue().toString());
			}
		}
		return os.toByteArray();
	}
	
	/* (non-Javadoc)
	 * @see org.sol.util.socket.message.PacketContent#getBytes()
	 */
	public byte[] getBytes() throws IOException {
		return getBytes(null);
	}
	
	/* (non-Javadoc)
	 * @see org.sol.util.socket.message.PacketContent#parseBytes(byte[])
	 */
	public void parseBytes(byte[] data) throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(data);
		DataInputStream ds = new DataInputStream(is);
		if(items != null) {
			for(XMLItem item : items) {
				if(item.getType().equals(int.class))
					item.setValue(ds.readInt());
				else if(item.getType().equals(String.class)) {
					if(item.getSize() == 0)
						item.setValue(ds.readString(Integer.parseInt(item.getLinkedItem().getValue().toString())));
					else
						item.setValue(ds.readString(item.getSize()));
				}
			}
		}
	}
	
	/**
	 * 计算数据包长度 并设置数据头的包长字段
	 * @return
	 * @throws IOException 
	 */
	public int calculatePacketLength() throws IOException {
		int count = 0;
		if(items != null) {
			for(XMLItem item : items) {
				int size = item.getSize();
				if(size == 0) {
					if(item.getLinkedItem().getValue() == null)
						throw new IOException("unset [" + item.getLinkedItem().getName() + "] value,please call setItemValue()");
					size = Integer.parseInt(item.getLinkedItem().getValue().toString());
				}
				count += size;
			}
		}
		
		header.setItemValue(ContentFactory.packetLengthFieldName, header.getHeaderLength() + count);
		return count;
	}
	
	/**
	 * 设置字段的值
	 * @param name
	 * @param value
	 * @throws UnsupportedEncodingException 
	 */
	public void setItemValue(String name,Object value,String charset) throws UnsupportedEncodingException {
		if(items != null) {
			for(XMLItem item : items) {
				if(item.getName().equals(name)) {
					if(item.getSize() == 0) {
						if(value instanceof String) {
							if(charset == null || charset.equals(""))
								item.getLinkedItem().setValue(((String)value).getBytes().length);
							else
								item.getLinkedItem().setValue(((String)value).getBytes(charset).length);
						} else {
							throw new UnsupportedEncodingException("unsupported value type,now just support [String]");
						}
					}
					item.setValue(value);
					return;
				}
			}
		}
		
		throw new XMLParseException("不存在的字段");
	}
	
	public void setItemValue(String name,Object value) {
		try {
			setItemValue(name, value, null);
		} catch (UnsupportedEncodingException e) {
		}
	}
	
	/**
	 * 获取字段的值
	 * @param name
	 * @return
	 */
	public Object getItemValue(String name) {
		if(items != null) {
			for(XMLItem item : items) {
				if(item.getName().equals(name)) 
					return item.getValue();
				
			}
		}
		
		throw new XMLParseException("不存在的字段");
	}
	
	/**
	 * 添加字段定义
	 * @param name
	 * @param type
	 * @param size
	 */
	public XMLItem addItem(String name,String type,int size){
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
			throw new XMLParseException("错误的绑定类型");
		
		items.add(item);
		
		return item;
	}
	
	/**
	 * 添加字段 并设定联结字段
	 * @param name
	 * @param type
	 * @param size
	 * @param linkedItem
	 * @return
	 */
	public XMLItem addItem(String name,String type,int size,XMLItem linkedItem) {
		XMLItem item = addItem(name, type, size);
		item.setLinkedItem(linkedItem);
		return item;
	}
	
	@Override
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append(header.toString());
		out.append("Content:\n");
		if(items != null) {
			for(XMLItem item : items)
				out.append(String.format("\tName:%s Type:%s Size:%d Value:%s\n", 
						item.getName(),item.getType().getSimpleName(),
						(item.getSize() == 0) ? item.getLinkedItem().getValue() : item.getSize(),
						item.getValue()));
		}
		return out.toString();
	}
	
	public PacketHeader getHeader() {
		return header;
	}

	public void setHeader(XMLHeader header) {
		this.header = header;
	}

	public List<XMLItem> getItems() {
		return items;
	}

	public void setItems(List<XMLItem> items) {
		this.items = items;
	}

}
