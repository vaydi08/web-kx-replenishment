package org.sol.util.c3p0.dataEntity2;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;

import org.sol.util.common.StringUtil;

public class Criteria {
	//protected Map<String,Object[]> conditionMap;
	protected List<CriteriaCondition> conditionList;
	protected List<String> orderList;
	
	public Criteria() {
		this.conditionList = new ArrayList<CriteriaCondition>();
		this.orderList = new ArrayList<String>();
	}
	
	protected String idname;
	protected Object id;
	protected Class<?> idclazz;
	
	public void id(String idname,Object value) {
		this.idname = idname;
		this.id = value;
	}
	public void eq(String fieldname,Object value) {
		conditionList.add(new CriteriaCondition(fieldname, "=?",value));
	}
	public void like(String fieldname,String value) {
		conditionList.add(new CriteriaCondition(fieldname, " like ?","%" + value + "%"));
	}
	public void between(String fieldname,Object value1,Object value2) {
		conditionList.add(new CriteriaCondition(fieldname, " between ? and ?", value1, value2));
	}

	
	public void byPojo(Object obj,boolean islike) throws Exception {
		Field[] fields = obj.getClass().getDeclaredFields();
		for(Field field : fields) {
			if(field.isAnnotationPresent(Id.class)) {
				Object value = getFieldValue(obj, field.getName(), false);
				idname = field.getName();
				idclazz = field.getType();
				if(value != null) 
					id = value;
					
			} else if(field.isAnnotationPresent(Column.class)) {
				Object value = getFieldValue(obj, field.getName(), !field.getAnnotation(Column.class).columnDefinition().isEmpty());
				if(value != null) {
					if(field.getType().equals(String.class) && islike)
						like(field.getName(), value.toString());
					else
						eq(field.getName(), value);
				}
			}
		}
	}
	
	public String getWhereSql() {
		// 拼合where条件
		if(conditionList.size() > 0) {
			StringBuilder sql = new StringBuilder();
			sql.append(" where ");
			for(CriteriaCondition condition : conditionList) 
				sql.append(condition.getName()).append(condition.getType()).append(" and ");

			if(id != null) 
				sql.append(idname).append("=? ");
			else
				sql.delete(sql.length() - 5, sql.length());
			
			return sql.toString();
		}
		
		return "";
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
	
	public void order(String fieldname) {
		orderList.add(fieldname);
	}
	
	public void orderDesc(String fieldname) {
		orderList.add(fieldname + " desc");
	}
	
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
	
	protected String getFieldString(Object obj,String fieldname) throws Exception {
		Object value = getFieldValue(obj,fieldname,false);
		return value == null ? "" : value.toString();
	}

	public List<Object> getParamList() {
		List<Object> list = getParamListWithoutId();
		
		if(id != null)
			list.add(id);
		
		return list;
	}
	public List<Object> getParamListWithoutId() {
		List<Object> list = new ArrayList<Object>(conditionList.size());
		
		for(CriteriaCondition condition : conditionList) {
			list.add(condition.getValue1());
			if(condition.getValue2() != null)
				list.add(condition.getValue2());
		}
		
		return list;
	}

	public List<CriteriaCondition> getConditionList() {
		return conditionList;
	}
	public String getIdname() {
		return idname;
	}
	public Object getId() {
		return id;
	}
	public Class<?> getIdclazz() {
		return idclazz;
	}

}
