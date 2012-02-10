package com.sol.kx.web.action.order;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;
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
import com.sol.kx.web.service.bean.ResultBean;

@Controller
@Scope("session")
@Results({@Result(name = "POJO",location = "/pojodata.jsp"),
		  @Result(name = "order3Repost",location = "/order/order3Repost.jsp"),
		  @Result(name = "order4Over",location = "/order/self.html",type = "redirect"),
		  @Result(name = "orderInfo",location = "/html/order/info.jsp"),
		  @Result(name = "chooseProduct",location = "/order/chooseData.jsp")})
public class OrderAction extends BaseAction<OrderType> implements SessionAware{

	private static final long serialVersionUID = 1L;

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderFeedbackService orderFeedbackService;
	
	// 添加订单
	@Override
	public String add2() {
		input.setUserid(((SysUser)session.get(Constants.SESSION_USER)).getId());
		return super.add2();
	}
	
	// 首页 未处理订单
	public String managerUntake() {		
		pagerBean = orderService.findUntake(pagerBean,((SysUser) session.get(Constants.SESSION_USER)).getId());
		return JSONDATA;
	}
	
	// 首页 自身订单
	public String managerSelf() {
		pagerBean = orderService.findSelf(pagerBean,((SysUser) session.get(Constants.SESSION_USER)).getId());
		return JSONDATA;
	}
	
	public String managerAll() {
		pagerBean = orderService.findAll(pagerBean);
		return JSONDATA;
	}
	
	private OrderType order;
	private Integer feedbackid;
	private String supplier;
	private String contact;
	private String feedback;
	private PagerBean<OrderFeedback> feedbackBean;
	
	public String take() {
		input = orderService.get(input.getId());
		input.setStatus(OrderType.STATUS_2_Taked);
		input.setUserid(((SysUser)session.get(Constants.SESSION_USER)).getId());
		input.setUsername(((SysUser)session.get(Constants.SESSION_USER)).getUsername());
		input.setGettime(new Timestamp((new Date()).getTime()));
		
		return "POJO";
	}
	
	public String accept() {
		result = orderService.update2(input);
		
		OrderFeedback feedback = new OrderFeedback();
		feedback.setOrderid(input.getId());
		feedback.setSupplier(supplier);
		feedback.setContact(contact);
		feedback.setOrdernum(0);
		result = orderFeedbackService.add2(feedback);
		return RESULT;
	}
	
	public String repostOk() {
		result = orderService.update2(input);
		
		OrderFeedback feedback = new OrderFeedback();
		feedback.setId(feedbackid);
		feedback.setFeedback(this.feedback);
		feedback.setFeedbacktime(new Timestamp(new Date().getTime()));
		result = orderFeedbackService.update2(feedback);
		
		return RESULT;
	}
	
	public String repostFail() {
		result = orderService.update2(input);
		
		OrderFeedback feedback = new OrderFeedback();
		feedback.setId(feedbackid);
		feedback.setFeedback(this.feedback);
		feedback.setFeedbacktime(new Timestamp(new Date().getTime()));
		result = orderFeedbackService.update2(feedback);
		
		feedback = new OrderFeedback();
		feedback.setOrderid(input.getId());
		feedback.setSupplier(supplier);
		feedback.setContact(contact);
		feedback.setOrdernum(0);
		result = orderFeedbackService.add2(feedback);
		
		return RESULT;
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
		case OrderType.STATUS_1_WaitTake:
			order.setStatus(OrderType.STATUS_2_Taked);
//			order.setUserid(((SysUser)session.get(Constants.SESSION_USER)).getId());
			order.setUsername(((SysUser)session.get(Constants.SESSION_USER)).getUsername());
			order.setGettime(new Timestamp((new Date()).getTime()));

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
		
		input.setUserid(((SysUser)session.get(Constants.SESSION_USER)).getId());
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

	private Map<String, Object> session;
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setFeedbackid(Integer feedbackid) {
		this.feedbackid = feedbackid;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

}
