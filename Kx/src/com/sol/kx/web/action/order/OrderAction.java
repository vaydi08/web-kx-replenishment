package com.sol.kx.web.action.order;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.OrderType;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.OrderService;

@Controller
@Scope("session")
@Results({@Result(name = "orderTake",location = "/order/orderTake.jsp")})
public class OrderAction extends BaseAction<OrderType>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private OrderService orderService;
	
	
	// 首页 未处理订单
	public String managerUntake() {		
		pagerBean = orderService.findUntake(pagerBean);
		return DATA;
	}
	
	public String orderTake() {
		input.setStatus(OrderType.STATUS_2_Taked);
		orderService.update2(input);
		return "orderTake";
	}
	
	
	@Override
	protected BaseService<OrderType> getService() {
		return orderService;
	}

	@Override
	protected OrderType newPojo() {
		return new OrderType();
	}

	
}
