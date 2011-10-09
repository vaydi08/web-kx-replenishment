package com.sol.kx.web.service.impl;

import org.sol.util.c3p0.dataEntity2.SelectEntity;
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
		Logger.SERVICE.ldebug("查询[order_type]未处理数据",bean.getPage(),bean.getPageSize());

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
	
	public PagerBean<OrderType> findSelf(PagerBean<OrderType> bean,Integer userid) {
		Logger.SERVICE.ldebug("查询[order_type]个人数据",bean.getPage(),bean.getPageSize(),userid);

		try {
			return setBeanValue(bean, 
					orderTypeDao.findSelf(bean.getPage(), bean.getPageSize(),userid), 
					orderTypeDao.findSelfCount(userid));
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询order_type错误", e);
			bean.setException(e);
			return bean;
		}
	}
	
	public OrderType get(OrderType input) {
		Logger.SERVICE.ldebug("查询[order_type]指定数据",input.getId());
		SelectEntity entity = new SelectEntity();
		try {
			entity.init(input);
			return orderTypeDao.get(entity);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询order_type指定数据错误", e);
			return null;
		}
	}
	
	protected BaseDao getDao() {
		return orderTypeDao;
	}

}
