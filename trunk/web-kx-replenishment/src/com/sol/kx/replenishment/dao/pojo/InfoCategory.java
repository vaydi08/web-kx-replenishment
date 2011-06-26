package com.sol.kx.replenishment.dao.pojo;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * InfoCategory entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "info_category")
public class InfoCategory implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	// Fields

	private Integer id;
	private String ccode;
	private String name;
	private Integer clevel;
	private Integer parent;
	private Timestamp lastUpdateTime;

	// Constructors

	/** default constructor */
	public InfoCategory() {
	}

	/** minimal constructor */
	public InfoCategory(Integer id, String ccode, String name, Integer clevel) {
		this.id = id;
		this.ccode = ccode;
		this.name = name;
		this.clevel = clevel;
	}

	/** full constructor */
	public InfoCategory(Integer id, String ccode, String name, Integer clevel,
			Integer parent, Timestamp lastUpdateTime) {
		this.id = id;
		this.ccode = ccode;
		this.name = name;
		this.clevel = clevel;
		this.parent = parent;
		this.lastUpdateTime = lastUpdateTime;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "ccode", nullable = false, length = 20)
	public String getCcode() {
		return this.ccode;
	}

	public void setCcode(String ccode) {
		this.ccode = ccode;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "clevel", nullable = false)
	public Integer getClevel() {
		return this.clevel;
	}

	public void setClevel(Integer clevel) {
		this.clevel = clevel;
	}

	@Column(name = "parent",insertable = false)
	public Integer getParent() {
		return this.parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	@Column(name = "lastUpdateTime", length = 23,insertable = false)
	public Timestamp getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}