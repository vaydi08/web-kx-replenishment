package com.sol.kx.web.action.order;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.pojo.OrderFeedback;
import com.sol.kx.web.dao.pojo.OrderType;
import com.sol.kx.web.dao.pojo.SysUser;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.OrderFeedbackService;
import com.sol.kx.web.service.OrderService;
import com.sol.kx.web.service.bean.PagerBean;

@Controller
@Scope("session")
@Results({@Result(name = "orderTake",location = "/order/orderTake.jsp"),
		  @Result(name = "order3Repost",location = "/order/order3Repost.jsp"),
		  @Result(name = "order4Over",location = "/order/self.html",type = "redirect"),
		  @Result(name = "orderInfo",location = "/html/order/info.jsp"),
		  @Result(name = "chooseProduct",location = "/order/chooseData.jsp")})
public class OrderAction extends BaseAction<OrderType>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderFeedbackService orderFeedbackService;
	
	// 首页 未处理订单
	public String managerUntake() {		
		pagerBean = orderService.findUntake(pagerBean,((SysUser) ActionContext.getContext().getSession().get(Constants.SESSION_USER)).getId());
		return JSONDATA;
	}
	
	// 首页 自身订单
	public String managerSelf() {
		pagerBean = orderService.findSelf(pagerBean,((SysUser) ActionContext.getContext().getSession().get(Constants.SESSION_USER)).getId());
		return JSONDATA;
	}
	
	public String managerAll() {
		pagerBean = orderService.findAll(pagerBean);
		return JSONDATA;
	}
	
	private OrderType order;
	private PagerBean<OrderFeedback> feedbackBean;
	
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
	
	public String order4Over() {
		input.setStatus(OrderType.STATUS_4_OrderOver);
		orderService.update2(input);
		return "order4Over";
	}
	
	public String orderGoto() {
		order = orderService.get(input.getId());
		
		switch (order.getStatus()) {
		case OrderType.STATUS_2_Taked:
			return "orderTake";
		case OrderType.STATUS_3_Repost:
			return "order3Repost";
		case OrderType.STATUS_4_OrderOver:
			return "order4Over";
		default:
			return "orderTake";
		}
	}
	
	public String orderInfo() {
		order = orderService.get(input.getId());
		
		OrderFeedback feedback = new OrderFeedback();
		feedback.setOrderid(input.getId());
		this.feedbackBean = orderFeedbackService.find2(feedback);
		return "orderInfo";
	}
	
	public String orderCancel() {
		input.setStatus(OrderType.STATUS_x1_Cancel);
		
		input.setUserid(((SysUser) ActionContext.getContext().getSession().get(Constants.SESSION_USER)).getId());
		input.setCanceltime(new Timestamp((new Date()).getTime()));
		result = orderService.update2(input);
		
		return RESULT;
	}
	
	private String pcode;
	private List productList;
	public String chooseProduct() {
//		productList = orderService.findChooseProduct(pcode);
		return "chooseProduct";
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

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public List getProductList() {
		return productList;
	}

	public PagerBean<OrderFeedback> getFeedbackBean() {
		return feedbackBean;
	}

	
}
