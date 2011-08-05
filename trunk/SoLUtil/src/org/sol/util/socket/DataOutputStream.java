package org.sol.util.socket;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 增强的DataOutputStream
 * @author HUYAO
 *
 */
public class DataOutputStream extends java.io.DataOutputStream{

	public DataOutputStream(OutputStream out) {
		super(out);
	}

	/**
	 * 写入String 默认编码
	 * @param str
	 * @throws IOException
	 */
	public void writeString(String str) throws IOException {
		writeString(str,null);
	}
	
	/**
	 * 写入String 
	 * @param str String
	 * @param charset 编码
	 * @throws IOException
	 */
	public void writeString(String str,String charset) throws IOException {
		if(charset == null || charset.equals(""))
			write(str.getBytes());
		else
			write(str.getBytes(charset));
	}
}
