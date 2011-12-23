package com.sol.kx.web.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "stock_check")
public class StockCheck extends Pojo{

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	
	@Column(name = "pid")
	private Integer pid;
	private String pname;
	
	@Column(name = "shopid")
	private Integer shopid;
	private String shopname;
	
	@Column(name = "minweight")
	private Double minweight;
	
	@Column(name = "maxweight")
	private Double maxweight;
	
	@Column(name = "stock_type1")
	private Integer stock_type1;
	
	@Column(name = "stock_type2")
	private Integer stock_type2;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public Integer getShopid() {
		return shopid;
	}

	public void setShopid(Integer shopid) {
		this.shopid = shopid;
	}

	public Double getMinweight() {
		return minweight;
	}

	public void setMinweight(Double minweight) {
		this.minweight = minweight;
	}

	public Double getMaxweight() {
		return maxweight;
	}

	public void setMaxweight(Double maxweight) {
		this.maxweight = maxweight;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public Integer getStock_type1() {
		return stock_type1;
	}

	public void setStock_type1(Integer stockType1) {
		stock_type1 = stockType1;
	}

	public Integer getStock_type2() {
		return stock_type2;
	}

	public void setStock_type2(Integer stockType2) {
		stock_type2 = stockType2;
	}

}
