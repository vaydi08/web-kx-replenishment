package com.sol.kx.web.dao.pojo;

import java.sql.Timestamp;


public class InfoCategory extends Pojo{

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String ccode;
	private String cname;
	private Integer clevel;
	private Integer parent;
	private Timestamp lastUpdateTime;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCcode() {
		return ccode;
	}
	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public Integer getClevel() {
		return clevel;
	}
	public void setClevel(Integer clevel) {
		this.clevel = clevel;
	}
	public Integer getParent() {
		return parent;
	}
	public void setParent(Integer parent) {
		this.parent = parent;
	}
	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}

}
