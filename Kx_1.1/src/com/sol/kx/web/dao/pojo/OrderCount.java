package com.sol.kx.web.dao.pojo;

import org.sol.util.mybatis.MyBatisPojo;

public class OrderCount extends MyBatisPojo{

	private static final long serialVersionUID = 1L;
	
	private Integer untake;
	private Integer mytake;
	private Integer alert;
	
	public Integer getUntake() {
		return untake;
	}
	public void setUntake(Integer untake) {
		this.untake = untake;
	}
	public Integer getMytake() {
		return mytake;
	}
	public void setMytake(Integer mytake) {
		this.mytake = mytake;
	}
	public Integer getAlert() {
		return alert;
	}
	public void setAlert(Integer alert) {
		this.alert = alert;
	}

}
