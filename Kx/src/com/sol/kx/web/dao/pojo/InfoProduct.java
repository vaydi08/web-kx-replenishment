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
	@Column(name = "unit")
	private String unit;
	
	public InfoProduct() {
		type1 = new InfoCategory();
		type2 = new InfoCategory();
		type3 = new InfoCategory();
		type4 = new InfoCategory();
		unit = "件";
	}
	
	public InfoProduct(Integer type1, Integer type2,Integer type3, 
			Integer type4, String pname, String pcode,String unit) {
		this();
		this.type1.setId(type1);
		this.type2.setId(type2);
		this.type3.setId(type3);
		this.type4.setId(type4);
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

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "InfoProduct [" + (id != null ? "id=" + id + ", " : "")
				+ (pcode != null ? "pcode=" + pcode + ", " : "")
				+ (pname != null ? "pname=" + pname + ", " : "")
				+ (type1 != null ? "type1=" + type1 + ", " : "")
				+ (type2 != null ? "type2=" + type2 + ", " : "")
				+ (type3 != null ? "type3=" + type3 + ", " : "")
				+ (type4 != null ? "type4=" + type4 + ", " : "")
				+ (unit != null ? "unit=" + unit : "") + "]";
	}
	
}
