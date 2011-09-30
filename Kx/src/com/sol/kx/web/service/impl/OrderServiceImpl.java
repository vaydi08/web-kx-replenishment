package com.sol.kx.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.OrderTypeDao;
import com.sol.kx.web.dao.pojo.OrderType;
import com.sol.kx.web.service.OrderService;
import com.sol.kx.web.service.bean.PagerBean;

@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderType> implements OrderService{

	@Autowired
	private OrderTypeDao orderTypeDao;
	
	public PagerBean<OrderType> findUntake(PagerBean<OrderType> bean) {
		Logger.SERVICE.ldebug("查询[order_type]数据",bean.getPage(),bean.getPageSize());

		try {
			return setBeanValue(bean, 
					orderTypeDao.findUntake(bean.getPage(), bean.getPageSize()), 
					orderTypeDao.findUntakeCount());
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询order_type错误", e);
			bean.setException(e);
			return bean;
		}
	}
	
	public OrderType findOrder(Integer id) {
		return null;
	}
	
	protected BaseDao getDao() {
		return orderTypeDao;
	}

}
