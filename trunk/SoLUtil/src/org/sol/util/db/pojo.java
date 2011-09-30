package org.sol.util.db;

public class pojo {
	private Integer id;
	private String text;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return "pojo [" + (id != null ? "id=" + id + ", " : "")
				+ (text != null ? "text=" + text : "") + "]";
	}
	
}
