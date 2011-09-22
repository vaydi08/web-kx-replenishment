package org.sol.util.c3p0.dataEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sol.util.common.StringUtil;

public abstract class DataEntity {
	protected String sql;
	
	protected List<Object> params;
	
	protected String idname;
	
	protected Object getFieldValue(Object obj,String fieldname,boolean inherit) throws Exception {
		if(obj == null)
			return null;
		
		Method method = obj.getClass().getMethod(StringUtil.getMethod(fieldname));
		Object entityObj = method.invoke(obj);
		if(inherit) {
			method = entityObj.getClass().getMethod("getId");
			entityObj = method.invoke(entityObj);
		}
		
		return entityObj;
	}
	
	public DataEntity() {}
	
	protected String tablename;
	protected Class<?> clazz;
	
	/**
	 * 生成对象映射
	 * @param obj 映射POJO
	 */
	public DataEntity(Class<?> clazz) {
		// 获取表名
		try {
			tablename = clazz.getAnnotation(Table.class).name();
		} catch (Exception e) {
			throw new RuntimeException("未设置Table注解",e);
		}
		
		this.clazz = clazz;
		
		conditionMap = new HashMap<String, Object[]>(clazz.getDeclaredFields().length);
	}

	
	/**
	 * 条件列表
	 */
	protected Map<String,Object[]> conditionMap;
	
	public void eq(String fieldname,Object value) {
		conditionMap.put(fieldname + "=?", value != null ? new Object[]{value} : null);
	}
	public void like(String fieldname,String value) {
		conditionMap.put(fieldname + " like ?", value != null ? new Object[]{"%" + value + "%"} : null);
	}
	public void sql(String condition,Object... values) {
		conditionMap.put(condition, values);
	}
	
	protected void buildConditionMap(Object obj) throws Exception {
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			if(field.isAnnotationPresent(Id.class))
				eq(field.getName(), getFieldValue(obj, field.getName(), false));
			else if(field.isAnnotationPresent(Column.class))
				eq(field.getName(), getFieldValue(obj, field.getName(), !field.getAnnotation(Column.class).columnDefinition().isEmpty()));
		}
	}
	
	public String getWhereSql() {
		StringBuilder sql = new StringBuilder();
		// 拼合where条件
		if(conditionMap.size() > 0) {
			sql.append(" where ");
			// 查询参数列表
			List<Object> list = new ArrayList<Object>(conditionMap.size());
			for(Entry<String,Object[]> en : conditionMap.entrySet()) {
				if(en.getValue() != null) {
					sql.append(en.getKey()).append(" and ");
					
					for(Object obj : en.getValue())
						list.add(obj);
				}
			}

			sql.delete(sql.length() - 5, sql.length());
			this.params = list;
		}
		
		return sql.toString();
	}
	
	protected abstract void buildSql();
	
	

	public String getSql() {
		buildSql();
		
		return sql;
	}
	
	public String getSimpleSql() {
		return sql;
	}

	public List<Object> getParams() {
		return params;
	}

	public String getIdname() {
		Field[] fields = clazz.getDeclaredFields();
		
		for(Field field : fields) {
			if(field.isAnnotationPresent(Id.class)) 
				return field.getName();
		}
		
		return null;
	}

	public Class<?> getClazz() {
		return clazz;
	}
}
