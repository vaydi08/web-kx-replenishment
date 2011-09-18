package com.sol.kx.web.service;

import java.util.Map;

import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.dao.pojo.StockCheck;
import com.sol.kx.web.service.bean.PagerBean;

public interface StockCheckService extends BaseService<StockCheck>{

	public PagerBean<InfoProduct> findProducts(PagerBean<InfoProduct> bean,
			Map<String, Integer> map,int shopid);
}