package com.sol.kx.web.dao.pojo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sol.util.mybatis.MyBatisPojo;

@Table(name = "order_feedback")
public class OrderFeedback extends MyBatisPojo{

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	
	@Column
	private Integer orderid;
	
	@Column
	private String supplier;
	
	@Column
	private String contact;
	
	@Column
	private Integer ordernum;
	
	@Column
	private Timestamp settime;
	
	@Column
	private String feedback;
	
	@Column
	private Timestamp feedbacktime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderid() {
		return orderid;
	}

	public void setOrderid(Integer orderid) {
		this.orderid = orderid;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Timestamp getSettime() {
		return settime;
	}

	public void setSettime(Timestamp settime) {
		this.settime = settime;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public Integer getOrdernum() {
		return ordernum;
	}

	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}

	public Timestamp getFeedbacktime() {
		return feedbacktime;
	}

	public void setFeedbacktime(Timestamp feedbacktime) {
		this.feedbacktime = feedbacktime;
	}

}
