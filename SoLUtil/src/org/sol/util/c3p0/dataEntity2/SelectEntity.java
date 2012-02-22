package org.sol.util.c3p0.dataEntity2;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;

public class SelectEntity extends Entity {
	private Map<String,Class<?>> smap;
	
	@Override
	protected void buildSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select ");
		
		Field[] fields = clazz.getDeclaredFields();
		buildSmap(fields, sql);

		// 加上表名
		sql.append(" from ").append(getTablename());
		
		super.sql = sql.toString();
	}

	@Override
	public String getFullSql() {
		buildSql();
		return sql + criteria.getWhereSql() + criteria.getOrderString();
	}
	
	public String getFullSql(String sql) {
		return sql + criteria.getWhereSql() + criteria.getOrderString();
	}
	
	private void buildSmap(Field[] fields,StringBuilder sql) {
		// 输出字段映射表
		Map<String,Class<?>> map = new HashMap<String, Class<?>>(fields.length);
		
		for(Field field : fields) {
			if(field.isAnnotationPresent(Column.class)) {
				sql.append(field.getName()).append(',');
				map.put(field.getAnnotation(Column.class).name(), field.getType());
			}
		}

		if(criteria.getIdname() != null) {
			sql.append(criteria.getIdname());
			map.put(criteria.getIdname(), criteria.getIdclazz());
		} else 
			sql.deleteCharAt(sql.length() - 1);

		this.smap = map;
	}

	public Map<String, Class<?>> getSmap() {
		return smap;
	}

	public void setSmap(Map<String, Class<?>> smap) {
		this.smap = smap;
	}
}
