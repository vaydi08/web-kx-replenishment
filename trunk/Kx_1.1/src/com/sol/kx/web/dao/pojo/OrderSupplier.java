package com.sol.kx.web.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sol.util.mybatis.MyBatisPojo;

@Table(name = "order_supplier")
public class OrderSupplier extends MyBatisPojo{

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	
	@Column
	private String name;
	
	@Column
	private String contact;
	
	@Column
	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
