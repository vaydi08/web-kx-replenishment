package com.sol.kx.web.action.order;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.OrderFeedback;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.order.OrderService;

@Controller
@Scope("session")
public class FeedbackAction extends BaseAction<OrderFeedback>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private OrderService orderService;
	
	public String manager() {
		pagerBean.setDataList(orderService.selectFeedback(input));
		OrderFeedback lastFeedback = pagerBean.getDataList().get(0);
		for(OrderFeedback feedback : pagerBean.getDataList())
			if(feedback.getSettime().getTime() >= lastFeedback.getSettime().getTime())
				lastFeedback = feedback;
		pagerBean.setReserve(new Object[]{lastFeedback.getId()});
		return JSONDATA;
	}
	
	@Override
	public String edit() {
		input.setFeedbacktime(new Timestamp(new Date().getTime()));
		result = orderService.updateFeedback(input);
		return RESULT;
	}
	
	@Override
	protected OrderFeedback newPojo() {
		return new OrderFeedback();
	}

	@Override
	protected BaseService injectService() {
		return orderService;
	}

}
