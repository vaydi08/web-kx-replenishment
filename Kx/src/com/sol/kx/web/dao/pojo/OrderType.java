package com.sol.kx.web.dao.pojo;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.JSONObject;

@Table(name = "order_type")
public class OrderType extends Pojo{

	@Id
	private Integer id;
	
	@Column(name = "pid")
	private Integer pid;
	private String pname;
	private String pcode;
	private Double pweight;
	private String image;
	private String stand;
	
	@Column(name = "shopid")
	private Integer shopid;
	private String shopname;
	
	@Column(name = "fromwho")
	private String fromwho;
	
	@Column(name = "num")
	private Integer num;
	
	@Column(name = "weight")
	private Double weight;
	
	@Column(name = "ordertime")
	private Timestamp ordertime;
	
	@Column(name = "requesttime")
	private Timestamp requesttime;
	
	@Column(name = "userid")
	private Integer userid;
	private String username;
	
	@Column(name = "gettime")
	private Timestamp gettime;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "canceltime")
	private Timestamp canceltime;
	
	@Column(name = "cancelReason")
	private String cancelReason;

	public static final int STATUS_1_WaitTake = 1;
	public static final int STATUS_2_Taked = 2;
	public static final int STATUS_3_Repost = 3;
	public static final int STATUS_4_OrderOver = 4;
	public static final int STATUS_5_SendProduct = 5;
	public static final int STATUS_6_Over = 6;
	public static final int STATUS_x1_Cancel = -1;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getShopid() {
		return shopid;
	}

	public void setShopid(Integer shopid) {
		this.shopid = shopid;
	}

	public String getFromwho() {
		return fromwho;
	}

	public void setFromwho(String fromwho) {
		this.fromwho = fromwho;
	}

	public Timestamp getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(Timestamp ordertime) {
		this.ordertime = ordertime;
	}

	public Timestamp getRequesttime() {
		return requesttime;
	}

	public void setRequesttime(Timestamp requesttime) {
		this.requesttime = requesttime;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Timestamp getGettime() {
		return gettime;
	}

	public void setGettime(Timestamp gettime) {
		this.gettime = gettime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getCanceltime() {
		return canceltime;
	}

	public void setCanceltime(Timestamp canceltime) {
		this.canceltime = canceltime;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public Double getPweight() {
		return pweight;
	}

	public void setPweight(Double pweight) {
		this.pweight = pweight;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStand() {
		return stand;
	}

	public void setStand(String stand) {
		this.stand = stand;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	public String json() {
		return new JSONObject(this).toString();
	}

}
