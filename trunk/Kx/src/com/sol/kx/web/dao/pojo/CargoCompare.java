package com.sol.kx.web.dao.pojo;

import java.sql.Timestamp;

public class CargoCompare {
	private int id;
	private String pname;
	private int pid;
	private String pcode;
	private String shopname;
	private int shopid;
	private Double minweight;
	private Double maxweight;
	private int stock;
	private int stocknow;
	private Timestamp saletime;
	private String serial;
	private int num;
	private Double weight;
	
	public CargoCompare() {
		
	}
	
	public CargoCompare(int pid, String serial, int num, Double weight) {
		this.pid = pid;
		this.serial = serial;
		this.num = num;
		this.weight = weight;
	}
	
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public String getShopname() {
		return shopname;
	}

	public void setShopname(String shopname) {
		this.shopname = shopname;
	}

	public int getShopid() {
		return shopid;
	}

	public void setShopid(int shopid) {
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

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getStocknow() {
		return stocknow;
	}

	public void setStocknow(int stocknow) {
		this.stocknow = stocknow;
	}

	public Timestamp getSaletime() {
		return saletime;
	}

	public void setSaletime(Timestamp saletime) {
		this.saletime = saletime;
	}
}
