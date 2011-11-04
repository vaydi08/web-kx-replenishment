package org.sol.util.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 字符串工具
 * @author HUYAO
 *
 */
public class StringUtil {
	/**
	 * 首字母大写
	 * @param field
	 * @return
	 */
	public static String firstUpper(String field) {
		int strLen;
        if (field == null || (strLen = field.length()) == 0) {
            return field;
        }
        return new StringBuffer(strLen)
            .append(Character.toTitleCase(field.charAt(0)))
            .append(field.substring(1))
            .toString();
	}
	
	public static String getMethod(String name) {
		return "get" + name.replaceFirst(name.substring(0,1), name.substring(0,1).toUpperCase());
	}
	
	public static String setMethod(String name) {
		return "set" + name.replaceFirst(name.substring(0,1), name.substring(0,1).toUpperCase());
	}
	
	/**
	 * 首字母小写
	 * @param field
	 * @return
	 */
	public static String firstLower(String field) {
		int strLen;
        if (field == null || (strLen = field.length()) == 0) {
            return field;
        }
        return new StringBuffer(strLen)
            .append(Character.toLowerCase(field.charAt(0)))
            .append(field.substring(1))
            .toString();
	}
	
	/**
	 * 编排数组
	 * @param ary
	 * @return
	 */
	public static String deepArray(byte[] ary) {
		StringBuilder sb = new StringBuilder();
		for(byte b : ary){
			sb.append(b);
			sb.append(' ');
		}
		return sb.toString();
	}
	
	public static String newLine() {
		return System.getProperty("line.separator");
	}
	
	/**
	 * 日期转换
	 */
	private static final DateFormat df_short = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat df_full = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final DateFormat df_compact = new SimpleDateFormat("ddHHmmssS");
	
	
	public static Timestamp parseTimestampShort(String input) {
		Date date = new Date();
		try {
			date = df_short.parse(input);
		} catch (ParseException e) {
			date = new Date();
		}
		Timestamp t = new Timestamp(date.getTime());
		
		return t;
	}
	
	public static String formatDateCompactNow() {
		return df_compact.format(new Date());
	}
	
	public static String formatDateShort(Date date) {
		return df_short.format(date);
	}
	
	public static String formatDate(Date date) {
		return df_full.format(date);
	}
	
	public static String formatTimestamp(Timestamp t) {
		return df_full.format(t.getTime());
	}
}
