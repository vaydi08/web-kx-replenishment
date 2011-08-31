package com.sol.kx.web.service;

import org.sol.util.c3p0.Condition;

import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.service.bean.PagerBean;

public interface InfoProductService extends BaseService<InfoProduct>{
	public PagerBean<InfoProduct> find(PagerBean<InfoProduct> bean,Condition condition);
}