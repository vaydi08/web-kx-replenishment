package com.sol.kx.web.dao.pojo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Map;

public class Pojo {

	private static final long serialVersionUID = 1L;

	protected String prefix = "";

	
	protected void setField(Map<String,Object> map,Object obj,String name) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		// 获取方法列表
		Field[] fields = obj.getClass().getDeclaredFields();

		// 遍历方法
		for(Field field : fields) {
			// 字段名
			String fieldname = field.getName();
			if(Modifier.isStatic(field.getModifiers()))
				continue;
			// 变量名
			String mapname = (name == null) ? fieldname : name + "_" + fieldname;
			field.setAccessible(true);
			// 获取值(对象)
			Object value = field.get(obj);
			// 值为空 跳过
			if(value == null)
				continue;
			// 如果是pojo对象,再次遍历
			if(value instanceof Pojo) 
				setField(map,value,mapname);
			else
				map.put(mapname, value);
		}
		
//		map.put("id", id);
	}
	
	public Integer getId(){return 0;}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public String toString() {
		Field[] fields = this.getClass().getDeclaredFields();
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for(Field f : fields) {
			if(Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers()))
				continue;
			Object value = null;
			try {
				f.setAccessible(true);
				value = f.get(this);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if(value != null)
				sb.append(f.getName()).append('=').append(value).append(',');
		}
		sb.append(']');
		
		return sb.toString();
	}
}
