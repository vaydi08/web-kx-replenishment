package com.sol.kx.web.dao.pojo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "order_feedback")
public class OrderFeedback extends Pojo{

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	
	@Column(name = "orderid")
	private Integer orderid;
	
	@Column(name = "supplier")
	private String supplier;
	
	@Column(name = "contact")
	private String contact;
	
	@Column(name = "ordernum")
	private Integer ordernum;
	
	@Column(name = "settime")
	private Timestamp settime;
	
	@Column(name = "feedback")
	private String feedback;
	
	@Column(name = "feedbacktime")
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

	@Override
	public String toString() {
		return "OrderFeedback ["
				+ (contact != null ? "contact=" + contact + ", " : "")
				+ (feedback != null ? "feedback=" + feedback + ", " : "")
				+ (feedbacktime != null ? "feedbacktime=" + feedbacktime + ", "
						: "") + (id != null ? "id=" + id + ", " : "")
				+ (orderid != null ? "orderid=" + orderid + ", " : "")
				+ (ordernum != null ? "ordernum=" + ordernum + ", " : "")
				+ (settime != null ? "settime=" + settime + ", " : "")
				+ (supplier != null ? "supplier=" + supplier : "") + "]";
	}
}
