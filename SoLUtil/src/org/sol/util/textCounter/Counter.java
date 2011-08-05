package org.sol.util.textCounter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Counter {
	/**
	 * 用于格式化计数器日期
	 */
	private static final DateFormat counterDf = new SimpleDateFormat("yyyyMMdd");
	/**
	 * 每日流量阀值
	 */
	private int countDayaccess;
	
	/**
	 * 计数器文件夹
	 */
	private String counterDocpath;
	/**
	 * 计数器文件前缀
	 */
	private String counterFilenamePrefix;
	
	public Counter(int countDayaccess,String counterDocpath,String counterFilenamePrefix) {
		this.countDayaccess = countDayaccess;
		this.counterDocpath = counterDocpath;
		this.counterFilenamePrefix = counterFilenamePrefix;
		
		this.makeCounterDocExist();
	}
	public Counter(String counterDocpath,String counterFilenamePrefix) {
		this.counterDocpath = counterDocpath;
		this.counterFilenamePrefix = counterFilenamePrefix;
		
		this.makeCounterDocExist();
	}
	
	/**
	 * 保证计数器文件夹存在
	 */
	public void makeCounterDocExist() {
		File file = new File(counterDocpath);
		if(!file.exists())
			file.mkdirs();
	}
	/**
	 * 增加计数器计数
	 */
	public void takeMOCount(int num) {
		String filename = counterFilenamePrefix
		 				+ counterDf.format(new Date());
		Writer w = new Writer(counterDocpath,filename);
		w.takeCount(num);
	}
	/**
	 * 检查计数器是否超过每日阀值
	 * @return
	 */
	public boolean checkNeedIgnoreThirdpartReport() {
		String filename = counterFilenamePrefix
			+ counterDf.format(new Date());
		Reader r = new Reader(counterDocpath,filename);
		int count = r.readCount();
		return count > this.countDayaccess;
	}
	/**
	 * 从计数器获得
	 * @return
	 */
	public int readCount() {
		String filename = counterFilenamePrefix
		+ counterDf.format(new Date());
		Reader r = new Reader(counterDocpath,filename);
		return r.readCount();
	}

	public int getCountDayaccess() {
		return countDayaccess;
	}

	public void setCountDayaccess(int countDayaccess) {
		this.countDayaccess = countDayaccess;
	}

	public String getCounterDocpath() {
		return counterDocpath;
	}

	public void setCounterDocpath(String counterDocpath) {
		this.counterDocpath = counterDocpath;
	}

	public String getCounterFilenamePrefix() {
		return counterFilenamePrefix;
	}

	public void setCounterFilenamePrefix(String counterFilenamePrefix) {
		this.counterFilenamePrefix = counterFilenamePrefix;
	}
}
