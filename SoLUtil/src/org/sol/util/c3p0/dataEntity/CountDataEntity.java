package org.sol.util.c3p0.dataEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 2011-08-28 通过pojo生成SQL和参数列表
 * @author sol
 *
 */
public class CountDataEntity extends DataEntity{
	
	public CountDataEntity(Object obj) throws Exception {
		Class<?> clazz = obj.getClass();
		// 字段
		Field[] fields = clazz.getDeclaredFields();
		// sql
		StringBuilder sql = new StringBuilder();
		StringBuilder where = new StringBuilder();
		// 输出字段映射表
		List<Object> list = new ArrayList<Object>(fields.length);
		
		// 获取表名
		String tablename = clazz.getAnnotation(Table.class).name();
		
		// 开始拼接SQL
		sql.append("select count(@id) from ").append(tablename);
		
		String idname = null;
		// 遍历字段
		for(Field field : fields) {
			// 如果是列字段
			if(field.isAnnotationPresent(Column.class)) {
				Column col = field.getAnnotation(Column.class);
				Object value = getFieldValue(obj, field.getName(),!col.columnDefinition().isEmpty());
				if(value != null) {
					// 存入映射名 - 字段类型
					if(value instanceof String) {
						where.append(col.name()).append(" like ? and ");
						list.add("%" + (String)value + "%");
					} else {
						where.append(col.name()).append(" = ? and ");
						list.add(value);
					}
				}
			} else if(field.isAnnotationPresent(Id.class)) {
				idname = field.getName();
			}
		}
		
		
		// 添加where条件
		if(list.size() > 0) {
			// 去掉最后一个逗号
			where.delete(where.length() - 4, where.length());
			sql.append(" where ").append(where.toString());
		}
		
		this.sql = sql.toString().replace("@id", idname);
		this.params = list;
	}

}
