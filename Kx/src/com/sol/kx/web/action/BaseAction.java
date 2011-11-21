package com.sol.kx.web.action;

import org.sol.util.c3p0.Condition;

import com.opensymphony.xwork2.ActionSupport;
import com.sol.kx.web.common.Logger;
import com.sol.kx.web.common.Util;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.bean.ResultBean;

public abstract class BaseAction<T> extends ActionSupport{

	private static final long serialVersionUID = 1L;
	// 全局常量
	public static final String DATA = "data";
	public static final String RESULT = "result";
	public static final String COMBOBOX = "combobox";
	
	
	protected abstract BaseService<T> getService();
	
	
	// 分页对象
	protected PagerBean<T> pagerBean;
	
	// 提交请求结果
	protected ResultBean result;
	
	// 通用查询条件类型
	protected String queryType;
	// 通用查询条件值
	protected String queryValue;
	
	// pojo
	protected T input;
	
	public BaseAction() {
		pagerBean = new PagerBean<T>();
		input = newPojo();
	}
	
	/**
	 * 默认查询条件
	 * @return
	 */
	protected Condition defaultCondition() {
		if(queryType == null || queryValue == null || queryValue.equals(""))
			return null;
		else {
			try {
				Condition condition = new Condition();
				// 以 _f 结尾查询浮点 ,以_i结尾查询数值 , 其他查询字符
				if(queryType.endsWith("_f")) 
					condition.addDefault(queryType.substring(0,queryType.length() - 2), Double.valueOf(queryValue));
				else if(queryType.endsWith("_i"))
					condition.addDefault(queryType.substring(0,queryType.length() - 2), Integer.valueOf(queryValue));
				else	
					condition.addDefault(queryType, queryValue);
				
				return condition;
			} catch (Exception e) {
				Logger.SYS.info("查询条件输入格式错误");
				return null;
			}
		}
	}
	
	protected T defaultQuery() {
		T obj = newPojo();
		
		if(queryType == null || queryValue == null || queryValue.equals(""))
			return obj;
		else {
			try {
				// 以 _f 结尾查询浮点 ,以_i结尾查询数值 , 其他查询字符
				if(queryType.endsWith("_f")) 
					Util.setValue(obj, queryType.substring(0,queryType.length() - 2), Double.parseDouble(queryValue));
				else if(queryType.endsWith("_i"))
					Util.setValue(obj, queryType.substring(0,queryType.length() - 2), Integer.parseInt(queryValue));
				else	
					Util.setValue(obj, queryType, queryValue);
				
				return obj;
			} catch (Exception e) {
				Logger.SYS.info("查询条件输入格式错误");
				return obj;
			}
		}
	}
	protected T defaultQuery2() {
		T obj = input;
		
		if(queryType == null || queryValue == null || queryValue.equals(""))
			return obj;
		else {
			try {
				// 以 _f 结尾查询浮点 ,以_i结尾查询数值 , 其他查询字符
				if(queryType.endsWith("_f")) 
					Util.setValue(obj, queryType.substring(0,queryType.length() - 2), Double.parseDouble(queryValue));
				else if(queryType.endsWith("_i"))
					Util.setValue(obj, queryType.substring(0,queryType.length() - 2), Integer.parseInt(queryValue));
				else	
					Util.setValue(obj, queryType, queryValue);
				
				return obj;
			} catch (Exception e) {
				Logger.SYS.info("查询条件输入格式错误");
				return obj;
			}
		}
	}
	
	
	
	protected abstract T newPojo();
	
	public String manager() {
		T obj = defaultQuery();
		pagerBean = getService().find(pagerBean, obj);
		return DATA;
	}
	
	public String manager2() {
		T obj = defaultQuery2();
		pagerBean = getService().findByPage2(pagerBean, obj);
		return DATA;
	}
	
	public String add() {
		result = getService().add(input);
		return RESULT;
	}
	
	public String edit() {
		result = getService().update(input);
		return RESULT;
	}
	
	public String delete() {
		result = getService().delete(input);
		return RESULT;
	}
	
	public String add2() {
		result = getService().add2(input);
		return RESULT;
	}
	
	public String edit2() {
		result = getService().update2(input);
		return RESULT;
	}
	
	public String delete2() {
		result = getService().delete2(input);
		return RESULT;
	}
	
	public PagerBean<T> getPagerBean() {
		return pagerBean;
	}
	public void setPagerBean(PagerBean<T> pagerBean) {
		this.pagerBean = pagerBean;
	}

	public int getPage() {
		return pagerBean.getPage();
	}

	public void setPage(int page) {
		this.pagerBean.setPage(page);
	}
	
	public int getPageSize() {
		return pagerBean.getPageSize();
	}
	
	public void setRows(int rows) {
		this.pagerBean.setPageSize(rows);
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getQueryValue() {
		return queryValue;
	}

	public void setQueryValue(String queryValue) {
		this.queryValue = queryValue;
	}

	public ResultBean getResult() {
		return result;
	}

	public void setInput(T input) {
		this.input = input;
	}

	public T getInput() {
		return input;
	}

	public void setResult(ResultBean result) {
		this.result = result;
	}


}
