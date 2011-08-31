package com.sol.kx.web.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.sol.util.common.StringUtil;

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
	
	public static void setValue(Object obj,String name,Object value) {
		setValue(obj, name, value.getClass(), value);
	}
	
	public static void setValue(Object obj,String name,Class<?> type,Object value) {
		try {
			Method method = obj.getClass().getMethod(StringUtil.setMethod(name), type);
			method.invoke(obj, value);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
