package com.sol.kx.web.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "info_product")
public class InfoProduct extends Pojo{

	@Id
	private Integer id;
	@Column(name = "type1")
	private Integer type1;
	@Column(name = "type2")
	private Integer type2;
	@Column(name = "type3")
	private Integer type3;
	@Column(name = "type4")
	private Integer type4;
	@Column(name = "pname")
	private String pname;
	@Column(name = "pcode")
	private String pcode;
	@Column(name = "unit")
	private String unit;
	@Column(name = "image")
	private String image;
	
	private String type1name;
	private String type2name;
	private String type3name;
	private String type4name;
	
	public InfoProduct() {
	}
	
	public InfoProduct(Integer type1, Integer type2,Integer type3, 
			Integer type4, String pname, String pcode,String unit) {
		this();
		this.type1 = type1;
		this.type2 = type2;
		this.type3 = type3;
		this.type4 = type4;
		this.pname = pname;
		this.pcode = pcode;
		this.unit = unit;
	}



	public String getPcode() {
		return pcode;
	}
	public void setPcode(String pcode) {
		this.pcode = pcode;
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

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getType1() {
		return type1;
	}

	public void setType1(Integer type1) {
		this.type1 = type1;
	}

	public Integer getType2() {
		return type2;
	}

	public void setType2(Integer type2) {
		this.type2 = type2;
	}

	public Integer getType3() {
		return type3;
	}

	public void setType3(Integer type3) {
		this.type3 = type3;
	}

	public Integer getType4() {
		return type4;
	}

	public void setType4(Integer type4) {
		this.type4 = type4;
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

	public String getType3name() {
		return type3name;
	}

	public void setType3name(String type3name) {
		this.type3name = type3name;
	}

	public String getType4name() {
		return type4name;
	}

	public void setType4name(String type4name) {
		this.type4name = type4name;
	}

}
