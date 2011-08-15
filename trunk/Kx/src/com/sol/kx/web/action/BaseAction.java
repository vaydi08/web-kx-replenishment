package com.sol.kx.web.action;

import org.sol.util.c3p0.Condition;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sol.kx.web.common.Logger;
import com.sol.kx.web.service.InfoCategoryService;
import com.sol.kx.web.service.InfoProductService;
import com.sol.kx.web.service.bean.PagerBean;

public abstract class BaseAction<T> extends ActionSupport{

	private static final long serialVersionUID = 1L;
	// 全局常量
	protected static final String DATA = "data";
	protected static final String RESULT = "result";
	protected static final String COMBOBOX = "combobox";
	
	
	
	@Autowired
	protected InfoProductService infoProductService;
	
	@Autowired
	protected InfoCategoryService infoCategoryService;
	
	
	// 分页对象
	protected PagerBean<T> pagerBean;
	
	// 通用查询条件类型
	protected String queryType;
	// 通用查询条件值
	protected String queryValue;
	
	// 提交请求执行结果
	protected boolean resultSuccess;
	// 提交请求错误信息
	protected String resultErrMsg;
	
	
	
	public BaseAction() {
		pagerBean = new PagerBean<T>();
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
					condition.addDefault(queryType.substring(0,queryType.length()), queryValue);
				
				return condition;
			} catch (Exception e) {
				Logger.SYS.info("查询条件输入格式错误");
				return null;
			}
		}
	}
	
	/**
	 * 提交请求 成功
	 * @return
	 */
	protected String RESULT_SUCCESS() {
		this.resultSuccess = true;
		return RESULT;
	}
	/**
	 * 提交请求 失败
	 * @param msg
	 * @return
	 */
	protected String RESULT_ERR(String msg) {
		this.resultSuccess = false;
		this.resultErrMsg = msg;
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

	public boolean isResultSuccess() {
		return resultSuccess;
	}

	public String getResultErrMsg() {
		return resultErrMsg;
	}
}
