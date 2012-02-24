package com.sol.kx.web.action.order;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.SessionAware;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.pojo.OrderFeedback;
import com.sol.kx.web.dao.pojo.OrderType;
import com.sol.kx.web.dao.pojo.SysUser;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.order.OrderService;

@Controller
@Scope("session")
@Results({
	@Result(name = "json",location = "/jsonString.jsp")})
public class OrderAction extends BaseAction<OrderType> implements SessionAware{

	private static final long serialVersionUID = 1L;

	@Autowired
	private OrderService orderService;
	
	// 添加订单
	@Override
	public String add() {
		input.setUserid(((SysUser)session.get(Constants.SESSION_USER)).getId());
		return super.add();
	}
	
	// 首页 未处理订单
	public String selectUntake() {
		orderService.selectUntake(pagerBean,input);
		return JSONDATA;
	}
	
	// 首页 自身订单
	public String selectSelf() {
		input.setUserid(((SysUser)session.get(Constants.SESSION_USER)).getId());
		orderService.selectSelf(pagerBean,input);
		return JSONDATA;
	}
	
	public String selectAll() {
		orderService.selectAll(pagerBean, input);
		return JSONDATA;
	}
	
	private String jsonString;
	
	public String take() {
		input = orderService.get(input);
		input.setStatus(OrderType.STATUS_2_Taked);
		input.setUserid(((SysUser)session.get(Constants.SESSION_USER)).getId());
		input.setUsername(((SysUser)session.get(Constants.SESSION_USER)).getUsername());
		input.setGettime(new Timestamp((new Date()).getTime()));
		
		JSONObject json = new JSONObject(input);
		jsonString = json.toString();
		return "json";
	}
	
	public String orderCancel() {
		input.setStatus(OrderType.STATUS_x1_Cancel);
		
		input.setUserid(((SysUser)session.get(Constants.SESSION_USER)).getId());
		input.setCanceltime(new Timestamp((new Date()).getTime()));
		result = orderService.update(input);
		
		return RESULT;
	}
	
	private Integer feedbackid;
	private String supplier;
	private String contact;
	private String feedback;
	
	public String accept() {
		input.setStatus(OrderType.STATUS_2_Taked);
		OrderFeedback of = new OrderFeedback();
		of.setOrderid(input.getId());
		of.setOrdernum(0);
		of.setContact(contact);
		of.setSupplier(supplier);
		result = orderService.accecpt(input, of);
		
		return RESULT;
	}
	
	public String repostOk() {
		input.setStatus(OrderType.STATUS_4_OrderOver);
		OrderFeedback feedback = new OrderFeedback();
		feedback.setId(feedbackid);
		feedback.setFeedback(this.feedback);
		feedback.setFeedbacktime(new Timestamp(new Date().getTime()));
		result = orderService.repostOk(input,feedback);
		
		return RESULT;
	}
	
	public String repostFail() {	
		input.setStatus(OrderType.STATUS_3_Repost);
		OrderFeedback of1 = new OrderFeedback();
		of1.setId(feedbackid);
		of1.setFeedback(this.feedback);
		of1.setFeedbacktime(new Timestamp(new Date().getTime()));

		OrderFeedback newfeedback = new OrderFeedback();
		newfeedback.setOrderid(input.getId());
		newfeedback.setSupplier(supplier);
		newfeedback.setContact(contact);
		newfeedback.setOrdernum(0);
		result = orderService.repostFail(input, of1, newfeedback);
		
		return RESULT;
	}
	
	public String orderInfo() {
		input = orderService.get(input);
		
		OrderFeedback feedback = new OrderFeedback();
		feedback.setOrderid(input.getId());
		List<OrderFeedback> feedbackList = orderService.selectFeedback(feedback);
		
		JSONObject json = new JSONObject();
		try {
			json.put("orderType", new JSONObject(input));
			json.put("feedbackList", new JSONArray(feedbackList));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		this.jsonString = json.toString();
		return "json";
	}
	
	
	
	@Override
	protected OrderType newPojo() {
		return new OrderType();
	}

	@Override
	protected BaseService injectService() {
		return orderService;
	}

	private transient Map<String, Object> session;
	
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setFeedbackid(Integer feedbackid) {
		this.feedbackid = feedbackid;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}


}
