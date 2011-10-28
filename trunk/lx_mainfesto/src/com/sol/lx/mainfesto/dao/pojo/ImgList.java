package com.sol.lx.mainfesto.dao.pojo;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "img_list")
public class ImgList extends Pojo{

	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;
	
	@Column(name = "image")
	private String image;
	
	@Column(name = "text")
	private String text;

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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "ImgList [" + (id != null ? "Id=" + id + ", " : "")
				+ (image != null ? "image=" + image + ", " : "")
				+ (text != null ? "text=" + text : "") + "]";
	}
}
