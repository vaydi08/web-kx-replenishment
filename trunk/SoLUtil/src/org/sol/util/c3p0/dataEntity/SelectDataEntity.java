package org.sol.util.c3p0.dataEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Id;
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
	
	public void setSmap(Map<String, Class<?>> smap) {
		this.smap = smap;
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
			} else if(field.isAnnotationPresent(Id.class)) {
				map.put(field.getName(),field.getType());
				idname = field.getName();
			}
		}
		
		if(condition != null) {
			sql += condition.getWhere();
			this.params = condition.getParams();
		}
		this.sql = sql;
		this.smap = map;
	}
	
	public SelectDataEntity(Class<?> clazz) {
		super(clazz);
		orderList = new ArrayList<String>();
	}
	public SelectDataEntity(String sql) {
		this.sql = sql;
		orderList = new ArrayList<String>();
	}
	
	public SelectDataEntity(Object obj) throws Exception {
		this(obj.getClass());
		
		buildConditionMap(obj);
	}
	
	private void buildSmap(Field[] fields,StringBuilder sql) {
		// 输出字段映射表
		Map<String,Class<?>> map = new HashMap<String, Class<?>>(fields.length);
		
		for(Field field : fields) {
			if(field.isAnnotationPresent(Id.class)) {
				sql.append(field.getName()).append(',');
				map.put(field.getName(), field.getType());
			} else if(field.isAnnotationPresent(Column.class)) {
				sql.append(field.getName()).append(',');
				map.put(field.getAnnotation(Column.class).name(), field.getType());
			}
		}
		this.smap = map;
	}
	@Override
	protected void buildSql() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("select ");
		
		Field[] fields = clazz.getDeclaredFields();
		buildSmap(fields, sql);
		
		// 去掉最后一个逗号
		sql.deleteCharAt(sql.length() - 1);
		// 加上表名
		sql.append(" from ").append(tablename);
		
		// 拼合where条件
		sql.append(getWhereSql());
		
		// 拼合Order
		sql.append(getOrderString());
		
		super.sql = sql.toString();
	}

	/**
	 * 排序列表
	 * @return
	 */
	protected List<String> orderList;
	
	public void order(String fieldname) {
		orderList.add(fieldname);
	}
	
	public void orderDesc(String fieldname) {
		orderList.add(fieldname + " desc");
	}
	
	public String getOrderString() {
		if(orderList.size() > 0) {
			StringBuilder orderStr = new StringBuilder();
			orderStr.append(" order by ");
			
			for(String order : orderList)
				orderStr.append(order).append(",");

			orderStr.deleteCharAt(orderStr.length() - 1);
		
		
			return orderStr.toString();
		} else
			return "";
	}

}
