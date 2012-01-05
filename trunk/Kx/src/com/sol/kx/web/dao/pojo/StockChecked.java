package com.sol.kx.web.dao.pojo;

public class StockChecked {
	private Integer stock_type1;
	private Double sum_stock_type1;
	private Integer stock_type2;
	private Double sum_stock_type2;
	
	private Integer ptype;
	private String cname;
	
	public Integer getStock_type1() {
		return stock_type1;
	}
	public void setStock_type1(Integer stockType1) {
		stock_type1 = stockType1;
	}
	public Double getSum_stock_type1() {
		return sum_stock_type1;
	}
	public void setSum_stock_type1(Double sumStockType1) {
		sum_stock_type1 = sumStockType1;
	}
	public Integer getStock_type2() {
		return stock_type2;
	}
	public void setStock_type2(Integer stockType2) {
		stock_type2 = stockType2;
	}
	public Double getSum_stock_type2() {
		return sum_stock_type2;
	}
	public void setSum_stock_type2(Double sumStockType2) {
		sum_stock_type2 = sumStockType2;
	}
	public Integer getPtype() {
		return ptype;
	}
	public void setPtype(Integer ptype) {
		this.ptype = ptype;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StockChecked [");
		if (cname != null)
			builder.append("cname=").append(cname).append(", ");
		if (ptype != null)
			builder.append("ptype=").append(ptype).append(", ");
		if (stock_type1 != null)
			builder.append("stock_type1=").append(stock_type1).append(", ");
		if (stock_type2 != null)
			builder.append("stock_type2=").append(stock_type2).append(", ");
		if (sum_stock_type1 != null)
			builder.append("sum_stock_type1=").append(sum_stock_type1).append(
					", ");
		if (sum_stock_type2 != null)
			builder.append("sum_stock_type2=").append(sum_stock_type2);
		builder.append("]");
		return builder.toString();
	}
}
