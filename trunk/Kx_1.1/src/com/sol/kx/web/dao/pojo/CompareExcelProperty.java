package com.sol.kx.web.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.sol.util.mybatis.MyBatisPojo;

@Table(name = "compare_excel_property")
public class CompareExcelProperty extends MyBatisPojo{

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	
	@Column(name = "category")
	private String category;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "col")
	private Integer col;
	
	@Column(name = "remark")
	private String remark;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCol() {
		return col;
	}

	public void setCol(Integer col) {
		this.col = col;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
