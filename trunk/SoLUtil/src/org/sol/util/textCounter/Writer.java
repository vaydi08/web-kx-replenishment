package org.sol.util.textCounter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.sol.util.log.Logger;

public class Writer {
	private File file;
	/**
	 * 文件夹路径
	 */
	private String doc;
	/**
	 * 文件名
	 */
	private String filename;
	/**
	 * 写入句柄
	 */
	private FileWriter ow;
	
	/**
	 * 保存计数数据
	 */
	private int count;
		
	/**
	 * 初始化类 
	 * @param doc 文件夹路径
	 * @param filename 文件名
	 */
	public Writer(String doc,String filename) {
		this.doc = doc;
		this.filename = filename;
		
		this.file = new File(doc,filename);
		
		initCount();
	}
	
	/**
	 * 初始化计数，从文件中读取，文件不存在情况下设0
	 */
	private void initCount() {
		if(file.exists()) {
			Reader r = new Reader(doc, filename);
			int c;
			c = r.readCount();
			if(c == -1) this.count = 0;
			else this.count = c;
		} else {
			this.count = 0;
		}
	}
	
	/**
	 * 写入计数
	 * @param num
	 */
	public void takeCount() {
		takeCount(1);
	}
	
	public void takeCount(int addIncrement) {
		try {
			ow = new FileWriter(file,false);
			this.count += addIncrement;
			ow.write(Integer.toString(this.count));
			ow.flush();
		} catch (IOException e) {
			String err = String.format("写入计数文件时产生IO错误，文件 [%s] - %s", file.getAbsolutePath(),e.getMessage());
			Logger.SYS.error(err,e);
		} catch (Exception e) {
			String err = String.format("写入计数文件时产生错误，文件 [%s] - %s", file.getAbsolutePath(),e.getMessage());
			Logger.SYS.error(err,e);
		} finally {
			if(ow != null) {
				try {
					ow.close();
				} catch (IOException e) {
				}
				ow = null;
			}
		}
	}
	
}
