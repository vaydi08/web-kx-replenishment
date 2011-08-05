package org.sol.util.common;


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
}
