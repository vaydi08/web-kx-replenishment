package com.sol.kx.replenishment.action;

import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sol.kx.replenishment.dao.pojo.SysEnvironment;
import com.sol.kx.replenishment.service.bean.EnvironmentFactory;
import com.sol.kx.replenishment.service.bean.PageResult;

public abstract class BaseAction<T> extends ActionSupport {

	private static final long serialVersionUID = 1L;

	protected PageResult<T> result;
	
	protected String queryName;
	protected String queryValue;
	
	@Autowired
	protected EnvironmentFactory environmentFactory;
	
	public PageResult<T> getResult() {
		return result;
	}

	public SysEnvironment getSysEnvironment() {
		return environmentFactory.getSysEnvironment();
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public String getQueryValue() {
		return queryValue;
	}

	public void setQueryValue(String queryValue) {
		this.queryValue = queryValue;
	}

}
