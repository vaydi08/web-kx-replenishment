package com.sol.kx.replenishment.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * InfoManufactory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "info_manufactory")
public class InfoManufactory implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	// Fields

	private Integer id;
	private Integer pid;
	private String name;
	private String mfCode;
	private String remark;

	private InfoProduct infoProduct;
	
	// Constructors

	/** default constructor */
	public InfoManufactory() {
	}

	/** minimal constructor */
	public InfoManufactory(Integer id, Integer pid, String name) {
		this.id = id;
		this.pid = pid;
		this.name = name;
	}

	/** full constructor */
	public InfoManufactory(Integer id, Integer pid, String name, String mfCode,
			String remark) {
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.mfCode = mfCode;
		this.remark = remark;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "pid", nullable = false)
	public Integer getPid() {
		return this.pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "mf_code",nullable = true)
	public String getMfCode() {
		return this.mfCode;
	}

	public void setMfCode(String mfCode) {
		this.mfCode = mfCode;
	}

	@Column(name = "remark",nullable = true)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@ManyToOne
	@JoinColumn(name = "pid",insertable = false,updatable = false)
	public InfoProduct getInfoProduct() {
		return infoProduct;
	}

	public void setInfoProduct(InfoProduct infoProduct) {
		this.infoProduct = infoProduct;
	}

}