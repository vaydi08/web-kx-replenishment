package org.sol.util.c3p0.dataEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 2011-08-28 通过pojo生成SQL和参数列表
 * @author sol
 *
 */
public class InsertDataEntity extends DataEntity{
	
	public InsertDataEntity(Class<?> clazz) {
		super(clazz);
	}
	
	public InsertDataEntity(Object obj) throws Exception {
		super(obj.getClass());
		
		buildConditionMap(obj);
	}
	
	@Override
	protected void buildSql() {
		StringBuilder sql = new StringBuilder();
		
		// 加上表名
		sql.append("insert into ").append(tablename).append("(");
		
		// 拼合insert字段
		buildInsert(sql);
		
		sql.append(") values(");
		
		for(int i = 0; i < params.size(); i ++)
			sql.append("?,");
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		
		super.sql = sql.toString();
	}

	protected void buildConditionMap(Object obj) throws Exception {
		Field[] fields = clazz.getDeclaredFields();
		for(Field field : fields) {
			if(field.isAnnotationPresent(Id.class)) {
				Object value = getFieldValue(obj, field.getName(), false);
				if(value != null)
					conditionMap.put(field.getName(), new Object[]{value});
			} else if(field.isAnnotationPresent(Column.class)) {
				Object value = getFieldValue(obj, field.getName(), !field.getAnnotation(Column.class).columnDefinition().isEmpty());
				if(value != null)
					conditionMap.put(field.getName(), new Object[]{value});
			}
		}
	}
	
	private void buildInsert(StringBuilder sql) {
		if(conditionMap.size() > 0) {
			// 查询参数列表
			List<Object> list = new ArrayList<Object>(conditionMap.size());
			for(Entry<String,Object[]> en : conditionMap.entrySet()) {
				sql.append(en.getKey()).append(",");
				
				for(Object obj : en.getValue())
					list.add(obj);
			}

			sql.deleteCharAt(sql.length() - 1);
			this.params = list;
		}
	}
}
