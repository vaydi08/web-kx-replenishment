package org.sol.util.mybatis;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Table;

import org.json.JSONObject;
import org.sol.util.mybatis.exception.PojoStructureException;

/**
 * @author HUYAO
 *
 */
public class MyBatisPojo implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 获取POJO对应的表名
	 * 需要POJO中的属性定义@Table(name)
	 * @return
	 */
	public String tablename() {
		Table table = this.getClass().getAnnotation(Table.class);
		if(table != null)
			return table.name();
		else
			throw new PojoStructureException("undefine POJO @Table, need Tablename(@Table)");
	}
	
	public String id() {
		return "id";
	}

	private transient static Map<Class<? extends MyBatisPojo>,List<String>> columnMap = new HashMap<Class<? extends MyBatisPojo>, List<String>>();
	
	private boolean isNull(String fieldname) {
		try {
			Field field = this.getClass().getDeclaredField(fieldname);
			return isNull(field);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private boolean isNull(Field field) {
		try {
			field.setAccessible(true);
			return field.get(this) == null;
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * 用于计算类定义
	 * 需要POJO中的属性定义@Column(name)
	 */
	public void caculationColumnList() {
		if(columnMap.containsKey(this.getClass()))
			return;
		
		Field[] fields = this.getClass().getDeclaredFields();
		List<String> columnList = new ArrayList<String>(fields.length);
		
		for(Field field : fields) {
			if(field.isAnnotationPresent(Column.class))
				columnList.add(field.getName());
		}
		
		columnMap.put(this.getClass(), columnList);
	}
	
	/**
	 * 获取用于WHERE的 有值字段表
	 * @return
	 */
	public List<WhereColumn> returnWhereColumnsName() {
		Field[] fields = this.getClass().getDeclaredFields();
		List<WhereColumn> columnList = new ArrayList<WhereColumn>(fields.length);
		
		for(Field field : fields) {
			if(field.isAnnotationPresent(Column.class) && !isNull(field)) 
				columnList.add(new WhereColumn(field.getName(), field.getGenericType().equals(String.class)));
		}
		
		return columnList;
	}
	
	public class WhereColumn {
		public String name;
		public boolean isString;
		
		public WhereColumn(String name,boolean isString) {
			this.name = name;
			this.isString = isString;
		}
	}
	
	/**
	 * 用于获取Insert的字段累加
	 * @return
	 */
	public String returnInsertColumnsName() {
		StringBuilder sb = new StringBuilder();
		
		List<String> list = columnMap.get(this.getClass());
		int i = 0;
		for(String column : list) {
			if(isNull(column))
				continue;
			
			if(i++ != 0)
				sb.append(',');
			sb.append(column);
		}
		return sb.toString();
	}
	
	/**
	 * 用于获取Insert的字段映射累加
	 * @return
	 */
	public String returnInsertColumnsDefine() {
		StringBuilder sb = new StringBuilder();
		
		List<String> list = columnMap.get(this.getClass());
		int i = 0;
		for(String column : list) {
			if(isNull(column))
				continue;
			
			if(i++ != 0)
				sb.append(',');
			sb.append("#{").append(column).append('}');
		}
		return sb.toString();
	}
	
	/**
	 * 用于获取Update Set的字段累加
	 * @return
	 */
	public String returnUpdateSet() {
		StringBuilder sb = new StringBuilder();
		
		List<String> list = columnMap.get(this.getClass());
		int i = 0;
		for(String column : list) {
			if(isNull(column))
				continue;
			
			if(i++ != 0)
				sb.append(',');
			sb.append(column).append("=#{").append(column).append('}');
		}
		return sb.toString();
	}
	
	
	
	public Integer getId(){return 0;}

	public String toJSONString() {
		JSONObject json = new JSONObject(this);
		return json.toString();
	}
	
	@Override
	public String toString() {
		Field[] fields = this.getClass().getDeclaredFields();
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for(Field f : fields) {
			if(Modifier.isStatic(f.getModifiers()) || Modifier.isFinal(f.getModifiers()))
				continue;
			Object value = null;
			try {
				f.setAccessible(true);
				value = f.get(this);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if(value != null)
				sb.append(f.getName()).append('=').append(value).append(',');
		}
		sb.append(']');
		
		return sb.toString();
	}
	
	private int page;
	private int pageSize;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
