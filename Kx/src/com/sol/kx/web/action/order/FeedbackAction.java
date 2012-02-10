package com.sol.kx.web.action.order;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.OrderFeedback;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.OrderFeedbackService;

@Controller
@Scope("session")
public class FeedbackAction extends BaseAction<OrderFeedback>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private OrderFeedbackService orderFeedbackService;
	
	public String manager() {
		pagerBean = orderFeedbackService.find2(input);
		OrderFeedback lastFeedback = pagerBean.getDataList().get(0);
		for(OrderFeedback feedback : pagerBean.getDataList())
			if(feedback.getSettime().getTime() >= lastFeedback.getSettime().getTime())
				lastFeedback = feedback;
		pagerBean.setReserve(new Object[]{lastFeedback.getId()});
		return JSONDATA;
	}
	
	@Override
	public String edit2() {
		input.setFeedbacktime(new Timestamp(new Date().getTime()));
		return super.edit2();
	}
	
	@Override
	protected BaseService<OrderFeedback> getService() {
		return orderFeedbackService;
	}

	@Override
	protected OrderFeedback newPojo() {
		return new OrderFeedback();
	}

}
