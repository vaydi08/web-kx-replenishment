package com.sol.kx.web.dao.pojo;

public class Compare extends Pojo{

	private static final long serialVersionUID = 1L;
	
	private String pname;
	private String pcode;
	private Integer pid;
	private Double minweight;
	private Double maxweight;
	private Integer stock;
	private Integer kucun;
	private Integer need;
	
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
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
	public Integer getKucun() {
		return kucun;
	}
	public void setKucun(Integer kucun) {
		this.kucun = kucun;
	}
	public Integer getNeed() {
		return need;
	}
	public void setNeed(Integer need) {
		this.need = need;
	}
}
