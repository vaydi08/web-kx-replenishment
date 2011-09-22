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
	
	@Column(name = "minweight")
	private Double minweight;
	
	@Column(name = "maxweight")
	private Double maxweight;
	
	@Column(name = "stock")
	private Integer stock;
	
	@Column(name = "stocktype")
	private Short stocktype;

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

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Short getStocktype() {
		return stocktype;
	}

	public void setStocktype(Short stocktype) {
		this.stocktype = stocktype;
	}

	@Override
	public String toString() {
		return "StockCheck [" + (id != null ? "id=" + id + ", " : "")
				+ (maxweight != null ? "maxweight=" + maxweight + ", " : "")
				+ (minweight != null ? "minweight=" + minweight + ", " : "")
				+ (pid != null ? "pid=" + pid + ", " : "")
				+ (shopid != null ? "shopid=" + shopid + ", " : "")
				+ (stock != null ? "stock=" + stock + ", " : "")
				+ (stocktype != null ? "stocktype=" + stocktype : "") + "]";
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}
	
}
