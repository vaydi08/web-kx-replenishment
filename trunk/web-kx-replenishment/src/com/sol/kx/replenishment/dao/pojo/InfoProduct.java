package com.sol.kx.replenishment.dao.pojo;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * InfoProduct entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "info_product")
public class InfoProduct implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// Fields

	private Integer id;
	private Integer type1;
	private Integer type2;
	private Integer type3;
	private Integer type4;
	private String name;
	private String pcode;
	private String quality;
	private String image;
	private Double weight;
	private String stand;
	private String remark;
	private Timestamp lastUpdateTime;
	
	private List<InfoTessera> infoTesseras;
	private List<InfoManufactory> infoManufactories;

	// Constructors

	/** default constructor */
	public InfoProduct() {
	}

	/** minimal constructor */
	public InfoProduct(Integer id, Integer type1, Integer type2, Integer type3,
			Integer type4, String name, String pcode, Double weight,
			Timestamp lastUpdateTime) {
		this.id = id;
		this.type1 = type1;
		this.type2 = type2;
		this.type3 = type3;
		this.type4 = type4;
		this.name = name;
		this.pcode = pcode;
		this.weight = weight;
		this.lastUpdateTime = lastUpdateTime;
	}

	/** full constructor */
	public InfoProduct(Integer id, Integer type1, Integer type2, Integer type3,
			Integer type4, String name, String pcode, String quality,
			String image, Double weight, String stand, String remark,
			Timestamp lastUpdateTime) {
		this.id = id;
		this.type1 = type1;
		this.type2 = type2;
		this.type3 = type3;
		this.type4 = type4;
		this.name = name;
		this.pcode = pcode;
		this.quality = quality;
		this.image = image;
		this.weight = weight;
		this.stand = stand;
		this.remark = remark;
		this.lastUpdateTime = lastUpdateTime;
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

	@Column(name = "type1", nullable = false)
	public Integer getType1() {
		return this.type1;
	}

	public void setType1(Integer type1) {
		this.type1 = type1;
	}

	@Column(name = "type2", nullable = false)
	public Integer getType2() {
		return this.type2;
	}

	public void setType2(Integer type2) {
		this.type2 = type2;
	}

	@Column(name = "type3", nullable = false)
	public Integer getType3() {
		return this.type3;
	}

	public void setType3(Integer type3) {
		this.type3 = type3;
	}

	@Column(name = "type4", nullable = false)
	public Integer getType4() {
		return this.type4;
	}

	public void setType4(Integer type4) {
		this.type4 = type4;
	}

	@Column(name = "name", nullable = false)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "pcode", nullable = false, length = 20)
	public String getPcode() {
		return this.pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	@Column(name = "quality",nullable = true)
	public String getQuality() {
		return this.quality;
	}

	public void setQuality(String quality) {
		this.quality = quality;
	}

	@Column(name = "image",nullable = true)
	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Column(name = "weight", nullable = false, precision = 53, scale = 0)
	public Double getWeight() {
		return this.weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	@Column(name = "stand",nullable = true)
	public String getStand() {
		return this.stand;
	}

	public void setStand(String stand) {
		this.stand = stand;
	}

	@Column(name = "remark",nullable = true)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "lastUpdateTime", nullable = false, length = 23,insertable = false)
	public Timestamp getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Transient
	public List<InfoTessera> getInfoTesseras() {
		return infoTesseras;
	}

	public void setInfoTesseras(List<InfoTessera> infoTesseras) {
		this.infoTesseras = infoTesseras;
	}

	@Transient
	public List<InfoManufactory> getInfoManufactories() {
		return infoManufactories;
	}

	public void setInfoManufactories(List<InfoManufactory> infoManufactories) {
		this.infoManufactories = infoManufactories;
	}
	
}