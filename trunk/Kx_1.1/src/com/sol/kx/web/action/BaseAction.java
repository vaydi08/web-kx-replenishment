package com.sol.kx.web.action;

import org.sol.util.mybatis.MyBatisPojo;

import com.opensymphony.xwork2.ActionSupport;
import com.sol.kx.web.common.Logger;
import com.sol.kx.web.common.Util;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.bean.ComboBoxBean;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.bean.ResultBean;

public abstract class BaseAction<T extends MyBatisPojo> extends ActionSupport{

	private static final long serialVersionUID = 1L;
	// 全局常量
	public static final String JSONDATA = "jsondata";
	public static final String RESULT = "result";
	public static final String COMBOBOX = "combobox";
		
	// 分页对象
	protected PagerBean<T> pagerBean;
	
	// 提交请求结果
	protected ResultBean result;
	
	// combo
	protected ComboBoxBean comboboxBean;
	
	// 通用查询条件类型
	protected String queryType;
	// 通用查询条件值
	protected String queryValue;
	
	// pojo
	protected T input;
	
	// baseService
	protected BaseService baseService;
	
	public BaseAction() {
		pagerBean = new PagerBean<T>();
		input = newPojo();
	}
	
	protected T defaultQuery() {
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
	
	public String select() {
		T obj = defaultQuery();
		injectService().select(pagerBean, obj);
		return JSONDATA;
	}
	public String manager() {
		T obj = defaultQuery();
		injectService().selectByPage(pagerBean, obj);
		return JSONDATA;
	}
	public String add() {
		result = injectService().insert(input);
		return RESULT;
	}
	
	public String edit() {
		result = injectService().update(input);
		return RESULT;
	}
	
	public String delete() {
		result = injectService().delete(input);
		return RESULT;
	}
	
	
	
	protected abstract T newPojo();
	
	protected abstract BaseService injectService();
	
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

	public ComboBoxBean getComboboxBean() {
		return comboboxBean;
	}
}
