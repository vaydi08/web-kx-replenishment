package com.sol.kx.web.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "info_category")
public class InfoCategory extends Pojo{

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	@Column(name = "ccode")
	private String ccode;
	@Column(name = "cname")
	private String cname;
	@Column(name = "clevel")
	private Integer clevel;
	
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

	@Override
	public String toString() {
		return "InfoCategory [ccode=" + ccode + ", clevel=" + clevel
				+ ", cname=" + cname + ", id=" + id + "]";
	}

}
