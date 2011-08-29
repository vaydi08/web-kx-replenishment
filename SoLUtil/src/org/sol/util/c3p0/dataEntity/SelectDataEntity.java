package org.sol.util.c3p0.dataEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sol.util.c3p0.Condition;

/**
 * 实体对象映射
 * 2011-08-28 增加直接对象的SQL生成和映射生成
 * 2011-08-28 增加间接对象的映射生成,子实体以 _ 连接
 * @author sol
 *
 */
public class SelectDataEntity extends DataEntity{

	private Map<String,Class<?>> smap;
	
	public Map<String, Class<?>> getSmap() {
		return smap;
	}
	
	public SelectDataEntity(String sql,Condition condition,Class<?> clazz) throws SecurityException, NoSuchFieldException {
		// 字段
		Field[] fields = clazz.getDeclaredFields();
		// 输出字段映射表
		Map<String,Class<?>> map = new HashMap<String, Class<?>>(fields.length);
		
		// 遍历字段
		for(Field field : fields) {
			// 如果是列字段 存入映射名 - 字段类型
			if(field.isAnnotationPresent(Column.class)) {
				Column col = field.getAnnotation(Column.class);
				if(col.columnDefinition().isEmpty())
					map.put(col.name(), field.getType());
				else {
					// 子实体映射
					String[] subNames = col.columnDefinition().split(",");
					
					for(String subName : subNames) {
						// 父实体名_子实体名
						String entityName = field.getName();
						
						Class<?> subClazz = field.getType().getDeclaredField(subName).getType();
						map.put(entityName + "_" + subName, subClazz);
					}
				}
			} else if(field.isAnnotationPresent(Id.class))
				map.put(field.getName(),field.getType());
		}
		
		if(condition != null) {
			sql += condition.getWhere();
			this.params = condition.getParams();
		}
		this.sql = sql;
		this.smap = map;
	}
	
	public SelectDataEntity(Object obj) throws Exception {
		Class<?> clazz = obj.getClass();
		// 字段
		Field[] fields = clazz.getDeclaredFields();
		// sql
		StringBuilder sql = new StringBuilder();
		StringBuilder where = new StringBuilder();
		// 输出字段映射表
		Map<String,Class<?>> map = new HashMap<String, Class<?>>(fields.length);
		// 查询参数列表
		List<Object> list = new ArrayList<Object>(fields.length);
		
		// 获取表名
		String tablename = clazz.getAnnotation(Table.class).name();
		
		// 开始拼接SQL
		sql.append("select ");
		where.append(" where ");
		
		// 遍历字段
		for(Field field : fields) {
			// 如果是列字段
			if(field.isAnnotationPresent(Column.class)) {
				Column col = field.getAnnotation(Column.class);
				// 存入映射名 - 字段类型
				sql.append(col.name()).append(',');
				map.put(col.name(), field.getType());
				// where
				Object fieldvalue = getFieldValue(obj, field.getName(), !col.columnDefinition().isEmpty());
				if(fieldvalue != null) {
					where.append(col.name()).append("=? and ");
					list.add(fieldvalue);
				}
			} else if(field.isAnnotationPresent(Id.class)) {
				sql.append(field.getName()).append(',');
				map.put(field.getName(),field.getType());
			}
		}
		
		// 去掉最后一个逗号
		sql.deleteCharAt(sql.length() - 1);
		// 加上表名
		sql.append(" from ").append(tablename);
		if(where.length() > 7) {
			where.delete(where.length()-5, where.length());
			sql.append(where.toString());
			this.params = list;
		}
		this.sql = sql.toString();
		this.smap = map;
	}

}
