package com.sol.kx.web.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "info_product")
public class InfoProduct extends Pojo{

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer id;
	@Column(columnDefinition = "id,cname", name = "type1")
	private InfoCategory type1;
	@Column(columnDefinition = "id,cname", name = "type2")
	private InfoCategory type2;
	@Column(columnDefinition = "id,cname", name = "type3")
	private InfoCategory type3;
	@Column(columnDefinition = "id,cname", name = "type4")
	private InfoCategory type4;
	@Column(name = "pname")
	private String pname;
	@Column(name = "pcode")
	private String pcode;
	@Column(name = "quality")
	private String quality;
	@Column(name = "image")
	private String image;
	@Column(name = "pweight")
	private Double pweight;
	@Column(name = "stand")
	private String stand;
	@Column(name = "unit")
	private String unit;
	@Column(name = "premark")
	private String premark;
	
	public InfoProduct() {
		type1 = new InfoCategory();
		type2 = new InfoCategory();
		type3 = new InfoCategory();
		type4 = new InfoCategory();
		unit = "件";
	}
	
	public InfoProduct(Integer type1, Integer type2,
			Integer type3, Integer type4, String pname, String pcode,
			String quality, String image, Double pweight, String stand,
			String unit, String premark) {
		this();
		this.type1.setId(type1);
		this.type2.setId(type2);
		this.type3.setId(type3);
		this.type4.setId(type4);
		this.pname = pname;
		this.pcode = pcode;
		this.quality = quality;
		this.image = image;
		this.pweight = pweight;
		this.stand = stand;
		this.unit = unit;
		this.premark = premark;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "InfoProduct [id=" + id + ", image=" + image + ", pcode="
				+ pcode + ", pname=" + pname + ", premark=" + premark
				+ ", pweight=" + pweight + ", quality=" + quality + ", stand="
				+ stand + ", type1=" + type1.getId() + ", type2=" + type2.getId() + ", type3="
				+ type3.getId() + ", type4=" + type4.getId() + ", unit=" + unit + "]";
	}

	
}
