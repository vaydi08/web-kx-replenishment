package com.megajoy.thirdpart.report.service.bean;

import java.io.Serializable;
import java.util.List;

import com.megajoy.thirdpart.report.dao.pojo.ThirdpartReport;

public class WaitForSendData implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private String xml;
	
	private int readyForSend;
	
	private int deduction;
		
	private List<ThirdpartReport> list;

	private boolean httpResponse;
	
	private boolean needSendHttp;
	
	private boolean readyToSend;
	
	private Integer which_service;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("which_service:").append(which_service).append("|");
		sb.append("readyToSend:").append(readyToSend).append("|");
		sb.append("needSendHttp:").append(needSendHttp).append("|");
		sb.append("httpResponse:").append(httpResponse).append("|");
		sb.append("readyForSend length:").append(readyForSend).append("|");
		sb.append("deduction length:").append(deduction).append("|");
		sb.append("readyToSend:").append(readyToSend).append("|");
		sb.append("list:[");
		for(ThirdpartReport thirdpartReport : list)
			sb.append(thirdpartReport.toString()).append(",");
		sb.append("]");
		
		return sb.toString();
	}
	
	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public List<ThirdpartReport> getList() {
		return list;
	}

	public void setList(List<ThirdpartReport> list) {
		this.list = list;
	}

	public int getReadyForSend() {
		return readyForSend;
	}

	public void setReadyForSend(int readyForSend) {
		this.readyForSend = readyForSend;
	}

	public int getDeduction() {
		return deduction;
	}

	public void setDeduction(int deduction) {
		this.deduction = deduction;
	}

	public boolean isHttpResponse() {
		return httpResponse;
	}

	public void setHttpResponse(boolean httpResponse) {
		this.httpResponse = httpResponse;
	}

	public boolean isNeedSendHttp() {
		return needSendHttp;
	}

	public void setNeedSendHttp(boolean needSendHttp) {
		this.needSendHttp = needSendHttp;
	}

	public boolean isReadyToSend() {
		return readyToSend;
	}

	public void setReadyToSend(boolean readyToSend) {
		this.readyToSend = readyToSend;
	}

	public Integer getWhich_service() {
		return which_service;
	}

	public void setWhich_service(Integer whichService) {
		which_service = whichService;
	}
}
