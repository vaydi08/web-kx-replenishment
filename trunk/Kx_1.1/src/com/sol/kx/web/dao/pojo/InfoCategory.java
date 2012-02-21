package com.sol.kx.web.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sol.util.mybatis.MyBatisPojo;

@Table(name = "info_category")
public class InfoCategory extends MyBatisPojo{

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer id;
	@Column
	private String ccode;
	@Column
	private String cname;
	@Column
	private Integer clevel;
	@Column
	private Integer parent;
	@Column
	private String image;
	
	public InfoCategory() {
	}
	
	public InfoCategory(String ccode, String cname, Integer clevel) {
		this.ccode = ccode;
		this.cname = cname;
		this.clevel = clevel;
	}
	
	public String getCcode() {
		return ccode;
	}
	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	public Integer getClevel() {
		return clevel;
	}
	public void setClevel(Integer clevel) {
		this.clevel = clevel;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent == null ? 0 : parent;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
