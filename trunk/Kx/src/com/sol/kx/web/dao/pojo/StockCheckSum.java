package com.sol.kx.web.dao.pojo;

public class StockCheckSum extends Pojo{
	private String type1name;
	private String type2name;
	private Integer shop_stocktype1;
	private Integer shop_stocktype2;
	private Integer shop_product_stocktype1;
	private Integer shop_product_stocktype2;
	private Double sum_type1_stocktype1;
	private Double sum_type1_stocktype2;
	private Double sum_type2_stocktype1;
	private Double sum_type2_stocktype2;
	
	public Integer getShop_stocktype1() {
		return shop_stocktype1;
	}
	public void setShop_stocktype1(Integer shopStocktype1) {
		shop_stocktype1 = shopStocktype1;
	}
	public Integer getShop_stocktype2() {
		return shop_stocktype2;
	}
	public void setShop_stocktype2(Integer shopStocktype2) {
		shop_stocktype2 = shopStocktype2;
	}
	public Integer getShop_product_stocktype1() {
		return shop_product_stocktype1;
	}
	public void setShop_product_stocktype1(Integer shopProductStocktype1) {
		shop_product_stocktype1 = shopProductStocktype1;
	}
	public Integer getShop_product_stocktype2() {
		return shop_product_stocktype2;
	}
	public void setShop_product_stocktype2(Integer shopProductStocktype2) {
		shop_product_stocktype2 = shopProductStocktype2;
	}
	public Double getSum_type1_stocktype1() {
		return sum_type1_stocktype1;
	}
	public void setSum_type1_stocktype1(Double sumType1Stocktype1) {
		sum_type1_stocktype1 = sumType1Stocktype1;
	}
	public Double getSum_type1_stocktype2() {
		return sum_type1_stocktype2;
	}
	public void setSum_type1_stocktype2(Double sumType1Stocktype2) {
		sum_type1_stocktype2 = sumType1Stocktype2;
	}
	public Double getSum_type2_stocktype1() {
		return sum_type2_stocktype1;
	}
	public void setSum_type2_stocktype1(Double sumType2Stocktype1) {
		sum_type2_stocktype1 = sumType2Stocktype1;
	}
	public Double getSum_type2_stocktype2() {
		return sum_type2_stocktype2;
	}
	public void setSum_type2_stocktype2(Double sumType2Stocktype2) {
		sum_type2_stocktype2 = sumType2Stocktype2;
	}
	public String getType1name() {
		return type1name;
	}
	public void setType1name(String type1name) {
		this.type1name = type1name;
	}
	public String getType2name() {
		return type2name;
	}
	public void setType2name(String type2name) {
		this.type2name = type2name;
	}
}
