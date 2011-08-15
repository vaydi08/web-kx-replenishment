package com.sol.kx.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sol.kx.web.dao.InfoCategoryDao;
import com.sol.kx.web.dao.InfoProductDao;
import com.sol.kx.web.service.bean.PagerBean;

public abstract class BaseServiceImpl {
	@Autowired
	protected InfoProductDao infoProductDao;
	
	@Autowired
	protected InfoCategoryDao infoCategoryDao;

	
	@Autowired
	protected ExceptionHandler exceptionHandler;
	
	protected <X> PagerBean<X> setBeanValue(PagerBean<X> bean,List<X> dataList,int count) {
		if(bean == null)
			bean = new PagerBean<X>();
		
		bean.setException(null);
		bean.setDataList(dataList);
		bean.setCount(count);
		
		return bean;
	}
}
