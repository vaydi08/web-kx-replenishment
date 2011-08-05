package org.sol.util.socket.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.sol.util.socket.DataInputStream;
import org.sol.util.socket.DataOutputStream;

public abstract class Content implements PacketContent{
	protected Header header;
	
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}
	
	public Content() {
	}
	
	public Content(int command,int contentLength) {
		header = new Header(command, contentLength);
	}
	
	public Content(int sequence,int command,int contentLength) {
		header = new Header(sequence,command, contentLength);
	}
	
	/**
	 * 字节化
	 * @return
	 * @throws IOException
	 */
	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream os = header.getBytes();
		DataOutputStream ds = new DataOutputStream(os);
		getSelfBytes(ds);
		return os.toByteArray();
	}
	
	/**
	 * 字节化
	 * @param os
	 * @return
	 * @throws IOException
	 */
	protected abstract void getSelfBytes(DataOutputStream ds) throws IOException;
	
	/**
	 * 对象化
	 * @param data
	 * @throws IOException 
	 */
	public void parseBytes(byte[] content) throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(content);
		DataInputStream ds = new DataInputStream(is);
		parseSelfBytes(ds);
	}
	
	/**
	 * 对象化
	 * @param data
	 * @throws IOException 
	 */
	protected abstract void parseSelfBytes(DataInputStream ds) throws IOException;
	


}
