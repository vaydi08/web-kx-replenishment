package com.megajoy.thirdpart.report.dao.pojo;

import java.io.Serializable;

public class Config implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer which_service;
	private Short discount;
	private String sync_url;
	private String remark;
	private String gateway;
	private String longnumber;
	private String command;
	private Integer countDayAccess;
	private String counterDocpath;
	
	public Integer getWhich_service() {
		return which_service;
	}
	public void setWhich_service(Integer whichService) {
		which_service = whichService;
	}
	public Short getDiscount() {
		return discount;
	}
	public void setDiscount(Short discount) {
		this.discount = discount;
	}
	public String getSync_url() {
		return sync_url;
	}
	public void setSync_url(String syncUrl) {
		sync_url = syncUrl;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getLongnumber() {
		return longnumber;
	}
	public void setLongnumber(String longnumber) {
		this.longnumber = longnumber;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public Integer getCountDayAccess() {
		return countDayAccess;
	}
	public void setCountDayAccess(Integer countDayAccess) {
		this.countDayAccess = countDayAccess;
	}
	public String getCounterDocpath() {
		return counterDocpath;
	}
	public void setCounterDocpath(String counterDocpath) {
		this.counterDocpath = counterDocpath;
	}
	
}
