package com.sol.kx.web.service;

import org.sol.util.c3p0.Condition;

import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.service.bean.PagerBean;

public interface InfoProductService {

	/**
	 * 查询产品
	 * @param bean
	 * @param condition
	 * @return
	 */
	public PagerBean<InfoProduct> findProducts(PagerBean<InfoProduct> bean,
			Condition condition);

}