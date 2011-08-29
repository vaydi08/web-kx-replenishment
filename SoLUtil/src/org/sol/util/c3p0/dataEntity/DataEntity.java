package org.sol.util.c3p0.dataEntity;

import java.lang.reflect.Method;
import java.util.List;

import org.sol.util.common.StringUtil;

public class DataEntity {
	protected String sql;
	
	protected List<Object> params;
	
	protected Object getFieldValue(Object obj,String fieldname,boolean inherit) throws Exception {
		Method method = obj.getClass().getMethod(StringUtil.getMethod(fieldname));
		Object entityObj = method.invoke(obj);
		if(inherit) {
			method = entityObj.getClass().getMethod("getId");
			entityObj = method.invoke(entityObj);
		}
		
		return entityObj;
	}

	public String getSql() {
		return sql;
	}

	public List<Object> getParams() {
		return params;
	}
}
