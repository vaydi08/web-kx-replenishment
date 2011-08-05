package com.megajoy.thirdpart.report.service.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.megajoy.thirdpart.report.common.Constants;
import com.megajoy.thirdpart.report.dao.pojo.ThirdpartReport;

public class ThirdpartReportBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer which_service;
	private List<ThirdpartReport> thirdpartReportList;
	private Constants.SENDSTATE sendstate;
	
	public ThirdpartReportBean(Integer which_service,Constants.SENDSTATE sendstate) {
		this.which_service = which_service;
		thirdpartReportList = new ArrayList<ThirdpartReport>(50);
		this.sendstate = sendstate;
	}
	
	public Integer getWhich_service() {
		return which_service;
	}
	public void setWhich_service(Integer whichService) {
		which_service = whichService;
	}
	public List<ThirdpartReport> getThirdpartReportList() {
		return thirdpartReportList;
	}
	public void setThirdpartReportList(List<ThirdpartReport> thirdpartReportList) {
		this.thirdpartReportList = thirdpartReportList;
	}
	
	public void addThirdpart(ThirdpartReport thirdpartReport) {
		this.thirdpartReportList.add(thirdpartReport);
	}

	public Constants.SENDSTATE getSendstate() {
		return sendstate;
	}

	public void setSendstate(Constants.SENDSTATE sendstate) {
		this.sendstate = sendstate;
	}
	
	/**
	 * 用于鉴别此包是一般发送还是重发
	 * @return
	 */
	public boolean isReadyToSend() {
		return this.sendstate != null && this.sendstate == Constants.SENDSTATE.READY;
	}
}
