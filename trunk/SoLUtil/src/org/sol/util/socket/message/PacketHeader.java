package org.sol.util.socket.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface PacketHeader {

	/**
	 * 字节化
	 * @return
	 * @throws IOException 
	 */
	public ByteArrayOutputStream getBytes() throws IOException;

	/**
	 * 获取包长度
	 * @return
	 */
	public Integer getPacketLength();
	
	/**
	 * 获取头长度
	 * @return
	 */
	public Integer getHeaderLength();

	/**
	 * 获得指令
	 * @return
	 */
	public Integer getCommand();
	
	/**
	 * 
	 */
	public Integer getSequence();
}