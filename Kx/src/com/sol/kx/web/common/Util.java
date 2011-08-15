package com.sol.kx.web.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 格式化时间
	 * @param m
	 * @return
	 */
	public static String parseDate(long m) {
		return df.format(new Date(m));
	}
}
