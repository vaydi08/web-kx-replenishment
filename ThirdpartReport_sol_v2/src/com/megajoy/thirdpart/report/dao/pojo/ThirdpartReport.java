package com.megajoy.thirdpart.report.dao.pojo;

import java.io.Serializable;

public class ThirdpartReport implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String content;
	private String dest_terminal_id;
	private String linkid;
	private String src_terminal_id;
	private Integer thirdpart_id ;
	private Integer which_service;
	private Integer sendstate;
	
	public ThirdpartReport() {
		sendstate = 0;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id:").append(id).append("|");
		sb.append("content:").append(content).append("|");
		sb.append("dest_terminal_id:").append(dest_terminal_id).append("|");
		sb.append("linkid:").append(linkid).append("|");
		sb.append("src_terminal_id:").append(src_terminal_id).append("|");
		sb.append("thirdpart_id:").append(thirdpart_id).append("|");
		sb.append("which_service:").append(which_service).append("|");
		sb.append("sendstate:").append(sendstate);
		
		return sb.toString();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDest_terminal_id() {
		return dest_terminal_id;
	}
	public void setDest_terminal_id(String destTerminalId) {
		dest_terminal_id = destTerminalId;
	}
	public String getLinkid() {
		return linkid;
	}
	public void setLinkid(String linkid) {
		this.linkid = linkid;
	}
	public String getSrc_terminal_id() {
		return src_terminal_id;
	}
	public void setSrc_terminal_id(String srcTerminalId) {
		src_terminal_id = srcTerminalId;
	}
	public Integer getThirdpart_id() {
		return thirdpart_id;
	}
	public void setThirdpart_id(Integer thirdpartId) {
		thirdpart_id = thirdpartId;
	}
	public Integer getWhich_service() {
		return which_service;
	}
	public void setWhich_service(Integer whichService) {
		which_service = whichService;
	}
	public Integer getSendstate() {
		return sendstate;
	}
	public void setSendstate(Integer sendstate) {
		this.sendstate = sendstate;
	}

}
