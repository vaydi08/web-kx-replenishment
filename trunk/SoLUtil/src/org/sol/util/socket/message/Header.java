package org.sol.util.socket.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.sol.util.socket.DataInputStream;
import org.sol.util.socket.DataOutputStream;

/**
 * 数据头
 * @author HUYAO
 *
 */
public class Header implements PacketHeader{
	/**
	 * 包序列号
	 */
	private int sequence;
	
	/**
	 * 指令
	 */
	private int command;
	
	/**
	 * 数据包长度
	 */
	private int contentLength;
	
	/**
	 * 数据头长度
	 */
	private static int headerSize;

	private static int _seq = Integer.MIN_VALUE;
	
	public Header(int command,int contentLength) {
		setSequence();
		setCommand(command);
		setContentLength(contentLength);
	}
	
	public Header(int sequence,int command,int contentLength) {
		setSequence(sequence);
		setCommand(command);
		setContentLength(contentLength);
	}
	
	/**
	 * 字节化
	 * @return
	 * @throws IOException 
	 */
	public ByteArrayOutputStream getBytes() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream(contentLength + 12);
		DataOutputStream ds = new DataOutputStream(os);
		ds.writeInt(sequence);
		ds.writeInt(command);
		ds.writeInt(contentLength);
		return os;
	}
	
	/**
	 * 对象化
	 * @param data
	 * @throws IOException 
	 */
	public static Header parseBytes(byte[] data) throws IOException {
		ByteArrayInputStream is = new ByteArrayInputStream(data,0,12);
		DataInputStream ds = new DataInputStream(is);
		int sequence = ds.readInt();
		int command = ds.readInt();
		int contentLength = ds.readInt();
		
		Header header = new Header(sequence, command, contentLength);
		return header;
	}
	
	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	
	public void setSequence() {
		if(_seq == Integer.MAX_VALUE)
			_seq = Integer.MIN_VALUE;
		
		this.sequence = ++_seq;
	}

	public Integer getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public static int getHeaderSize() {
		return headerSize;
	}

	public static void setHeaderSize(int headerSize) {
		Header.headerSize = headerSize;
	}

	@Override
	public Integer getPacketLength() {
		return this.contentLength + 12;
	}

	@Override
	public Integer getHeaderLength() {
		return getHeaderSize();
	}

}
