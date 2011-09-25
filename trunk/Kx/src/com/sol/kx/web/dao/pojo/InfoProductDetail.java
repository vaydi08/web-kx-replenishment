package com.sol.kx.web.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "info_product_detail")
public class InfoProductDetail {
	@Column(name = "pid")
	private Integer pid;
	
	@Column(name = "quality")
	private String quality;
	
	@Column(name = "image")
	private String image;
	
	@Column(name = "pweight")
	private Double pweight;
	
	@Column(name = "stand")
	private String stand;
	
	@Column(name = "premark")
	private String premark;

	public Integer getPid() {
		return pid;
	}

	public void setPid(Integer pid) {
		this.pid = pid;
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

	public Double getPweight() {
		return pweight;
	}

	public void setPweight(Double pweight) {
		this.pweight = pweight;
	}

	public String getStand() {
		return stand;
	}

	public void setStand(String stand) {
		this.stand = stand;
	}

	public String getPremark() {
		return premark;
	}

	public void setPremark(String premark) {
		this.premark = premark;
	}

	@Override
	public String toString() {
		return "InfoProductDetail ["
				+ (image != null ? "image=" + image + ", " : "")
				+ (pid != null ? "pid=" + pid + ", " : "")
				+ (premark != null ? "premark=" + premark + ", " : "")
				+ (pweight != null ? "pweight=" + pweight + ", " : "")
				+ (quality != null ? "quality=" + quality + ", " : "")
				+ (stand != null ? "stand=" + stand : "") + "]";
	}
}
