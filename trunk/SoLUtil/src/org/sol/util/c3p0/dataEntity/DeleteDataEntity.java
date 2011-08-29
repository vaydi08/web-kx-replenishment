package org.sol.util.c3p0.dataEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 2011-08-28 通过pojo生成SQL和参数列表
 * @author sol
 *
 */
public class DeleteDataEntity extends DataEntity{
	
	public DeleteDataEntity(Object obj) throws Exception {
		Class<?> clazz = obj.getClass();
		// 字段
		Field[] fields = clazz.getDeclaredFields();
		// sql
		StringBuilder sql = new StringBuilder();
		// 输出字段映射表
		List<Object> list = new ArrayList<Object>(1);
		
		// 获取表名
		String tablename = clazz.getAnnotation(Table.class).name();
		
		// 开始拼接SQL
		sql.append("delete from ").append(tablename).append(" where ");
		
		// 遍历字段
		for(Field field : fields) {
			// 如果是列字段
			if(field.isAnnotationPresent(Id.class)) {
				sql.append(field.getName()).append("=?");
				list.add(getFieldValue(obj, field.getName(), false));
				break;
			}
		}
		
		this.sql = sql.toString();
		this.params = list;
	}

}
