package com.sol.kx.web.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.OrderTypeDao;
import com.sol.kx.web.dao.pojo.OrderCount;
import com.sol.kx.web.dao.pojo.OrderType;
import com.sol.kx.web.service.OrderService;
import com.sol.kx.web.service.bean.PagerBean;

@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderType> implements OrderService{

	@Autowired
	private OrderTypeDao orderTypeDao;
	
	public PagerBean<OrderType> findUntake(PagerBean<OrderType> bean,Integer userid) {
		Logger.SERVICE.ldebug("查询[order_type]未处理数据",bean.getPage(),bean.getPageSize());

		try {
			PagerBean<OrderType> out = setBeanValue(bean, 
					orderTypeDao.findUntake(bean.getPage(), bean.getPageSize()), 
					orderTypeDao.findUntakeCount());
			OrderCount count = orderTypeDao.findOrderCount(userid);
			out.setReserve(new Object[]{count.getUntake(),count.getMytake(),count.getAlert()});
			return out;
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
	
	public PagerBean<OrderType> findAll(PagerBean<OrderType> bean) {
		Logger.SERVICE.ldebug("查询[order_type]全部数据",bean.getPage(),bean.getPageSize());

		try {
			return setBeanValue(bean, 
					orderTypeDao.findAll(bean.getPage(), bean.getPageSize()), 
					orderTypeDao.findAllCount());
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询order_type错误", e);
			bean.setException(e);
			return bean;
		}
	}
	
	public OrderType get(Integer id) {
		Logger.SERVICE.ldebug("查询[order_type]指定数据",id);

		try {
			return orderTypeDao.get(id);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询order_type指定数据错误", e);
			return null;
		}
	}
	
	// 选择产品
	public List findChooseProduct(String pcode) {
		Logger.SERVICE.ldebug("查询[info_product_detail]对应pcode产品列表", pcode);
		try {
			return null;
		} catch (Exception e) {
			Logger.SERVICE.ldebug("查询[info_product_detail]对应pcode产品列表错误", e);
			return null;
		}
	}
	
	protected BaseDao getDao() {
		return orderTypeDao;
	}

}
