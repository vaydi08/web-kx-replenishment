package org.sol.util.c3p0.dataEntity2;

import java.lang.reflect.Field;

import javax.persistence.Id;
import javax.persistence.Table;

public abstract class Entity {
	protected String sql;
	protected Criteria criteria;
	
	protected Class<?> clazz;
	protected String idname;
	
	public void init(String sql,Criteria criteria) {
		this.sql = sql;
		this.criteria = criteria;
	}
	
	public void init(Object obj) throws Exception {
		this.clazz = obj.getClass();
		this.criteria = new Criteria();
		this.criteria.byPojo(obj, true);
	}
	public void init(Class<?> clazz,Criteria criteria) {
		this.clazz = clazz;
		this.criteria = criteria;
	}
	
	protected abstract void buildSql();
	
	public String getTablename() {
		return clazz.getAnnotation(Table.class).name();
	}
	
	public String getIdname() {
		Field[] fields = clazz.getDeclaredFields();
		
		for(Field field : fields) {
			if(field.isAnnotationPresent(Id.class)) 
				return field.getName();
		}
		
		return null;
	}

	public String getSql() {
		return sql;
	}
	
	public abstract String getFullSql();

	public Criteria getCriteria() {
		return criteria;
	}

	public Class<?> getClazz() {
		return clazz;
	}
}
