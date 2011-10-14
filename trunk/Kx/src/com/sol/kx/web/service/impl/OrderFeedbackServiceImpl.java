package com.sol.kx.web.service.impl;

import java.util.List;

import javax.persistence.Table;

import org.sol.util.c3p0.dataEntity2.SelectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.OrderFeedbackDao;
import com.sol.kx.web.dao.pojo.OrderFeedback;
import com.sol.kx.web.service.OrderFeedbackService;
import com.sol.kx.web.service.bean.PagerBean;

@Service
public class OrderFeedbackServiceImpl extends BaseServiceImpl<OrderFeedback> implements OrderFeedbackService {

	@Autowired
	private OrderFeedbackDao orderFeedbackDao;
	
	@Override
	public PagerBean<OrderFeedback> find2(OrderFeedback obj) {
		Logger.SERVICE.ldebug("查询[" + obj.getClass().getAnnotation(Table.class).name() + "]数据",obj.toString());
		PagerBean<OrderFeedback> bean = new PagerBean<OrderFeedback>();
		try {
			SelectEntity selectEntity = new SelectEntity();
			selectEntity.init(obj);
			selectEntity.getCriteria().orderDesc("id");
			bean.setDataList((List<OrderFeedback>) getDao().find2(selectEntity));
			return bean;
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询" + obj.getClass().getAnnotation(Table.class).name() + "错误", e);
			bean.setException(e);
			return bean;
		}
	}
	
	@Override
	protected BaseDao getDao() {
		return orderFeedbackDao;
	}

}
