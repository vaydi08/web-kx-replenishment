package com.sol.kx.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.OrderFeedbackDao;
import com.sol.kx.web.dao.pojo.OrderFeedback;
import com.sol.kx.web.service.OrderFeedbackService;

@Service
public class OrderFeedbackServiceImpl extends BaseServiceImpl<OrderFeedback> implements OrderFeedbackService {

	@Autowired
	private OrderFeedbackDao orderFeedbackDao;
	
	@Override
	protected BaseDao getDao() {
		return orderFeedbackDao;
	}

}
