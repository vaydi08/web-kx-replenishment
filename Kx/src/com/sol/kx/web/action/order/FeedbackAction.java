package com.sol.kx.web.action.order;

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
		return DATA;
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
