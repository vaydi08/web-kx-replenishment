package com.sol.kx.web.service;

import com.sol.kx.web.dao.pojo.OrderType;
import com.sol.kx.web.service.bean.PagerBean;

public interface OrderService extends BaseService<OrderType>{
	public PagerBean<OrderType> findUntake(PagerBean<OrderType> bean);
	
	public PagerBean<OrderType> findSelf(PagerBean<OrderType> bean,Integer userid);
	
	public PagerBean<OrderType> findAll(PagerBean<OrderType> bean);
	
	public OrderType get(Integer id);
}
