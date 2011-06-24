package com.sol.kx.replenishment.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * InfoTessera entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "info_tessera")
public class InfoTessera implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	// Fields

	private Integer id;
	private Integer pid;
	private String name;
	private Double weight;
	private String clarity;
	private String color;
	private String cut;

	// Constructors

	/** default constructor */
	public InfoTessera() {
	}

	/** minimal constructor */
	public InfoTessera(Integer id, Integer pid, String name) {
		this.id = id;
		this.pid = pid;
		this.name = name;
	}

	/** full constructor */
	public InfoTessera(Integer id, Integer pid, String name, Double weight,
			String clarity, String color, String cut) {
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.weight = weight;
		this.clarity = clarity;
		this.color = color;
		this.cut = cut;
	}

	// Property accessors
	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "pid", nullable = false)
	public Integer getPid() {
		return this.pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "weight", precision = 53, scale = 0)
	public Double getWeight() {
		return this.weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	@Column(name = "clarity")
	public String getClarity() {
		return this.clarity;
	}

	public void setClarity(String clarity) {
		this.clarity = clarity;
	}

	@Column(name = "color")
	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Column(name = "cut")
	public String getCut() {
		return this.cut;
	}

	public void setCut(String cut) {
		this.cut = cut;
	}

}