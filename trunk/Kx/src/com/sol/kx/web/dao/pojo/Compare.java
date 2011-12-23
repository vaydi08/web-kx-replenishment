package com.sol.kx.web.dao.pojo;

public class Compare extends Pojo{
	
	private String pname;
	private String pcode;
	private Integer pid;
	private Double minweight;
	private Double maxweight;
	private Integer stock_type1;
	private Integer stock_type2;
	private Integer kucun;
	private Integer need_stocktype1;
	private Integer need_stocktype2;
	
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

	public Integer getKucun() {
		return kucun;
	}
	public void setKucun(Integer kucun) {
		this.kucun = kucun;
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
	public Integer getNeed_stocktype1() {
		return need_stocktype1;
	}
	public void setNeed_stocktype1(Integer needStocktype1) {
		need_stocktype1 = needStocktype1;
	}
	public Integer getNeed_stocktype2() {
		return need_stocktype2;
	}
	public void setNeed_stocktype2(Integer needStocktype2) {
		need_stocktype2 = needStocktype2;
	}

}
