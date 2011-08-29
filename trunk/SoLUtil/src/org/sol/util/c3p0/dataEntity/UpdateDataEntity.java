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
public class UpdateDataEntity extends DataEntity{
	
	public UpdateDataEntity(Object obj) throws Exception {
		Class<?> clazz = obj.getClass();
		// 字段
		Field[] fields = clazz.getDeclaredFields();
		// sql
		StringBuilder sql = new StringBuilder();
		// 输出字段映射表
		List<Object> list = new ArrayList<Object>(fields.length);
		
		// 获取表名
		String tablename = clazz.getAnnotation(Table.class).name();
		
		// 开始拼接SQL
		sql.append("update ").append(tablename).append(" set ");
		
		String idname = null;
		Object idvalue = null;
		// 遍历字段
		for(Field field : fields) {
			// 如果是列字段
			if(field.isAnnotationPresent(Column.class)) {
				Column col = field.getAnnotation(Column.class);
				Object value = getFieldValue(obj, field.getName(),!col.columnDefinition().isEmpty());
				if(value != null) {
					// 存入映射名 - 字段类型
					sql.append(col.name()).append("=?,");
					list.add(value);
				}
			} else if(field.isAnnotationPresent(Id.class)) {
				idname = field.getName();
				idvalue = getFieldValue(obj, field.getName(), false);
			}
		}
		
		// 去掉最后一个逗号
		sql.deleteCharAt(sql.length() - 1);
		
		// 添加where条件
		sql.append(" where ").append(idname).append("=?");
		list.add(idvalue);
		
		this.sql = sql.toString();
		this.params = list;
	}

}
