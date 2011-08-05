package org.sol.util.socket;

import java.io.IOException;
import java.io.InputStream;

/**
 * 增强的DataInputStream
 * @author HUYAO
 *
 */
public class DataInputStream extends java.io.DataInputStream{

	public DataInputStream(InputStream in) {
		super(in);
	}
	
	/**
	 * 读取String 
	 * @param length 长度
	 * @param charset 编码
	 * @return
	 * @throws IOException
	 */
	public String readString(int length,String charset) throws IOException {
		byte[] buf = new byte[length];
//		int readSize = 0;
		for(int i = 0; i < length; i ++)
			buf[i] = (byte)read();
		return (charset == null || charset.equals("")) ? new String(buf) : new String(buf,charset);
	}
	
	/**
	 * 读取String 默认编码
	 * @param length 长度
	 * @return
	 * @throws IOException
	 */
	public String readString(int length) throws IOException {
		return readString(length, null);
	}
	
}
