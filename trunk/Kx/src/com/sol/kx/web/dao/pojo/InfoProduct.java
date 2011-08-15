package com.sol.kx.web.dao.pojo;

import java.sql.Timestamp;

public class InfoProduct extends Pojo{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private InfoCategory type1;
	private InfoCategory type2;
	private InfoCategory type3;
	private InfoCategory type4;
	private String pname;
	private String pcode;
	private String quality;
	private String image;
	private Double pweight;
	private String stand;
	private String unit;
	private String premark;
	private Timestamp lastUpdateTime;
	
	public InfoProduct() {
		type1 = new InfoCategory();
		type2 = new InfoCategory();
		type3 = new InfoCategory();
		type4 = new InfoCategory();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	public String getStand() {
		return stand;
	}
	public void setStand(String stand) {
		this.stand = stand;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public Double getPweight() {
		return pweight;
	}

	public void setPweight(Double pweight) {
		this.pweight = pweight;
	}

	public String getPremark() {
		return premark;
	}

	public void setPremark(String premark) {
		this.premark = premark;
	}

	public InfoCategory getType1() {
		return type1;
	}

	public void setType1(InfoCategory type1) {
		this.type1 = type1;
	}

	public InfoCategory getType2() {
		return type2;
	}

	public void setType2(InfoCategory type2) {
		this.type2 = type2;
	}

	public InfoCategory getType3() {
		return type3;
	}

	public void setType3(InfoCategory type3) {
		this.type3 = type3;
	}

	public InfoCategory getType4() {
		return type4;
	}

	public void setType4(InfoCategory type4) {
		this.type4 = type4;
	}

	
}
