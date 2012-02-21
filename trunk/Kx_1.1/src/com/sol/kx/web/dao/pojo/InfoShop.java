package com.sol.kx.web.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sol.util.mybatis.MyBatisPojo;

@Table(name = "info_shop")
public class InfoShop extends MyBatisPojo{

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer id;
	@Column
	private String scode;
	@Column
	private String name;
	@Column
	private String address;
	@Column
	private String tel;
	@Column
	private String shorttel;
	@Column
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
