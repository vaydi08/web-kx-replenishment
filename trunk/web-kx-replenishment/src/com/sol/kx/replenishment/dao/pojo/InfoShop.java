package com.sol.kx.replenishment.dao.pojo;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * InfoShop entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "info_shop")
public class InfoShop implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private Integer id;
	private String scode;
	private String name;
	private String address;
	private String tel;
	private String remark;
	private Timestamp lastUpdateTime;

	// Constructors

	/** default constructor */
	public InfoShop() {
	}

	/** minimal constructor */
	public InfoShop(Integer id, String scode, String name,
			Timestamp lastUpdateTime) {
		this.id = id;
		this.scode = scode;
		this.name = name;
		this.lastUpdateTime = lastUpdateTime;
	}

	/** full constructor */
	public InfoShop(Integer id, String scode, String name, String address,
			String tel, String remark, Timestamp lastUpdateTime) {
		this.id = id;
		this.scode = scode;
		this.name = name;
		this.address = address;
		this.tel = tel;
		this.remark = remark;
		this.lastUpdateTime = lastUpdateTime;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "scode", nullable = false, length = 20)
	public String getScode() {
		return this.scode;
	}

	public void setScode(String scode) {
		this.scode = scode;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "address")
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "tel", length = 20)
	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	@Column(name = "remark", length = 50)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "lastUpdateTime", nullable = false, length = 23)
	public Timestamp getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}