package org.sol.util.c3p0;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;

import org.sol.util.common.StringUtil;

public class DataConsoleUtil {
	public static Map<String,Class<?>> getClassDefine(Class<?> clazz,String... excludeName) {
		Field[] fields = clazz.getDeclaredFields();
		Map<String,Class<?>> map = new HashMap<String, Class<?>>(fields.length);
		
		FieldLoop:
		for(Field field : fields) {
			String fieldname = field.getName();
			Class<?> type = field.getType();
			
			if(fieldname.equals("serialVersionUID"))
				continue FieldLoop;
			
			if(excludeName.length > 0) {
				for(String exclude : excludeName) {
					if(fieldname.equals(exclude))
						continue FieldLoop;
				}
			}
			map.put(fieldname, type);
		}
		
		return map;
	}

}
