package org.sol.util.textCounter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.sol.util.log.Logger;

public class Reader {
	private File file;
	/**
	 * 文件夹路径
	 */
	@SuppressWarnings("unused")
	private String doc;
	/**
	 * 文件名
	 */
	@SuppressWarnings("unused")
	private String filename;
	/**
	 * 写入句柄
	 */
	private FileInputStream fr;
	
	
	/**
	 * 初始化类 
	 * @param doc 文件夹路径
	 * @param filename 文件名
	 */
	public Reader(String doc,String filename) {
		this.doc = doc;
		this.filename = filename;
		
		this.file = new File(doc,filename);
	}
	
	/**
	 * 读取计数
	 * @return 读取到的计数数据，失败返回 -1,文件不存在时返回0
	 * @throws IOException 
	 */
	public int readCount() {
		// 不存在文件时，返回0
		if(!file.exists())
			return 0;
		
		try {
//			fr = new FileInputStream(file);
			byte data[] = new byte[9];
			int size = 0;
			int times = 0;
			do {
				fr = new FileInputStream(file);
				size = fr.read(data);
				fr.close();
			} while(size == -1 && ++times < 10);

			String dataString = new String(data,0,size);
			int count = Integer.parseInt(dataString);
			return count;
		} catch (IOException e) {
			String err = String.format("读取计数文件时产生IO错误，文件 [%s] - %s", file.getAbsolutePath(),e.getMessage());
			Logger.SYS.error(err,e);
		} finally {
			if(fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
				}
				fr = null;
			}
		}
		return -1;
	}
}
