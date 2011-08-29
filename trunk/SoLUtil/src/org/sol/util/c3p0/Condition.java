package org.sol.util.c3p0;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.sol.util.common.StringUtil;

public class Condition {
	private StringBuilder where;
	private List<Object> params;
	
	/**
	 * 解析PO的内容到where条件
	 * @param po PO对象
	 * @param excludeNames 排除的字段名
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	@Deprecated
	public Condition(Serializable po,String... excludeNames) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Field[] fields = po.getClass().getDeclaredFields();
		
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder sb = new StringBuilder();
		sb.append(" where");
		
		EachField:
		for(Field field : fields) {
			// 字段名
			String name = field.getName();
			// 过滤字段
			if(name.equals("serialVersionUID")) 
				continue;
			for(String exclude : excludeNames) 
				if(name.equals(exclude))
					continue EachField;
				
			// 字段值 通过get方法获取
			Object value = po.getClass().getMethod("get" + StringUtil.firstUpper(name)).invoke(po);
			if(value != null) {
				// 追加where条件
				sb.append(" ").append(field.getName()).append("=? and");
				// 存入参数列表
				params.add(value);
			}
		}
		sb.delete(sb.length() - 4, sb.length());
		
		if(params.size() > 0) {
			this.where = sb;
			this.params = params;
		}
	}

	
	public Condition() {
		this.where = new StringBuilder(" where");
	}
	
	public void addWhere(String sql,Object param) {
		if(params == null) {
			params = new ArrayList<Object>();
			where.append(" ").append(sql);
			params.add(param);
		} else {
			where.append(" and ").append(sql);
			params.add(param);
		}
	}
	
	public void addDefault(String type,String value) {
		addWhere(type + " like ?", "%" + value + "%");
	}
	public void addDefault(String type,Integer value) {
		addWhere(type + " = ?", value);
	}
	public void addDefault(String type,Double value) {
		addWhere(type + " = ?", value);
	}

	public String getWhere() {
		return where.toString();
	}

	public List<Object> getParams() {
		return params;
	}

}
