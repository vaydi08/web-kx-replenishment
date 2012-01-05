package com.sol.kx.web.service;

import java.util.Map;

import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.dao.pojo.StockCheck;
import com.sol.kx.web.dao.pojo.StockCheckSum;
import com.sol.kx.web.dao.pojo.StockChecked;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.bean.ResultBean;

public interface StockCheckService extends BaseService<StockCheck>{

	public PagerBean<InfoProduct> findProducts(PagerBean<InfoProduct> bean,
			Map<String, Integer> map,int shopid);
	
	public StockCheckSum findStockCheckSum(StockCheck obj);
	
	public ResultBean copyCheck(StockCheck obj,Integer clevel);
	
	public PagerBean<StockChecked> findCheckedType1(Integer shopid);
	public PagerBean<StockChecked> findCheckedType234(Integer shopid,Integer parent,Integer clevel,Integer parentlevel);
}