package com.sol.kx.web.action.order;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.pojo.OrderType;
import com.sol.kx.web.dao.pojo.SysUser;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.OrderService;

@Controller
@Scope("session")
@Results({@Result(name = "orderTake",location = "/order/orderTake.jsp"),
		  @Result(name = "order3Repost",location = "/order/order3Repost.jsp")})
public class OrderAction extends BaseAction<OrderType>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private OrderService orderService;
	
	
	// 首页 未处理订单
	public String managerUntake() {		
		pagerBean = orderService.findUntake(pagerBean);
		return DATA;
	}
	
	// 首页 自身订单
	public String managerSelf() {
		OrderType order = new OrderType();
		order.setUserid(((SysUser) ActionContext.getContext().getSession().get(Constants.SESSION_USER)).getId());
		pagerBean = orderService.findSelf(pagerBean,order.getUserid());
		return DATA;
	}
	
	private OrderType order;
	
	public String orderTake() {
		input.setStatus(OrderType.STATUS_2_Taked);
		input.setGettime(new Timestamp((new Date()).getTime()));
		orderService.update2(input);
		return orderGoto();
	}
	
	public String order3Repost() {
		input.setStatus(OrderType.STATUS_3_Repost);
		orderService.update2(input);
		return orderGoto();
	}
	
	public String orderGoto() {
		order = orderService.get(input.getId());
		
		switch (order.getStatus()) {
		case OrderType.STATUS_2_Taked:
			return "orderTake";
		case OrderType.STATUS_3_Repost:
			return "order3Repost";
		default:
			return "orderTake";
		}
	}
	
	public String orderCancel() {
		input.setStatus(OrderType.STATUS_x1_Cancel);
		
		input.setUserid(((SysUser) ActionContext.getContext().getSession().get(Constants.SESSION_USER)).getId());
		input.setCanceltime(new Timestamp((new Date()).getTime()));
		result = orderService.update2(input);
		
		return RESULT;
	}
	
	
	@Override
	protected BaseService<OrderType> getService() {
		return orderService;
	}

	@Override
	protected OrderType newPojo() {
		return new OrderType();
	}

	public OrderType getOrder() {
		return order;
	}

	
}
