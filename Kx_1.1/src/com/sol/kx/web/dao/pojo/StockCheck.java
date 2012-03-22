package com.sol.kx.web.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sol.util.mybatis.MyBatisPojo;

@Table(name = "stock_check")
public class StockCheck extends MyBatisPojo{

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	
	@Column
	private Integer pid;
	private String pname;
	private String pcode;
	private String image;
	
	@Column
	private Integer shopid;
	private String shopname;
	
	@Column
	private Double minweight;
	
	@Column
	private Double maxweight;
	
	@Column
	private Integer stock_type1;
	
	@Column
	private Integer stock_type2;
	
	private Integer clevel;

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

	public Integer getClevel() {
		return clevel;
	}

	public void setClevel(Integer clevel) {
		this.clevel = clevel;
	}

	public String getPcode() {
		return pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
