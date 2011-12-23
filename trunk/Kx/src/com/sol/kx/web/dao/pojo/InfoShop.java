package com.sol.kx.web.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "info_shop")
public class InfoShop extends Pojo{

	@Id
	private Integer id;
	@Column(name = "scode")
	private String scode;
	@Column(name = "name")
	private String name;
	@Column(name = "address")
	private String address;
	@Column(name = "tel")
	private String tel;
	@Column(name = "shorttel")
	private String shorttel;
	@Column(name = "remark")
	private String remark;
	
	public InfoShop() {
	}
	
	public InfoShop(String scode, String name, String address, String tel,
			String shorttel, String remark) {
		this.scode = scode;
		this.name = name;
		this.address = address;
		this.tel = tel;
		this.shorttel = shorttel;
		this.remark = remark;
	}
	
	public String getScode() {
		return scode;
	}
	public void setScode(String scode) {
		this.scode = scode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShorttel() {
		return shorttel;
	}
	public void setShorttel(String shorttel) {
		this.shorttel = shorttel;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
