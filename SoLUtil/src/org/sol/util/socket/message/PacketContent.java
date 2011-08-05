package org.sol.util.socket.message;

import java.io.IOException;

public interface PacketContent {

	/**
	 * 字节化
	 * @return
	 * @throws IOException
	 */
	public byte[] getBytes() throws IOException;

	/**
	 * 对象化
	 * @param data
	 * @throws IOException 
	 */
	public void parseBytes(byte[] data) throws IOException;
	
	/**
	 * 获得数据头
	 * @return
	 */
	public PacketHeader getHeader();

}