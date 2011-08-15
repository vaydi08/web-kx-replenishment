package com.sol.kx.web.service.bean;

import java.util.ArrayList;
import java.util.List;

public class ComboBoxBean {
	private List<Data> dataList;
	
	public List<Data> getDataList() {
		return dataList;
	}

	public void setDataList(List<Data> dataList) {
		this.dataList = dataList;
	}
	
	public ComboBoxBean() {
		this.dataList = new ArrayList<Data>();
	}
	
	public void addData(String text,String value) {
		this.dataList.add(new Data(text,value));
	}
	public void addData(String text,String value,boolean selected) {
		this.dataList.add(new Data(text,value,selected));
	}
	public void addData(String text,int value) {
		this.dataList.add(new Data(text,Integer.toString(value)));
	}
	public void addData(String text,int value,boolean selected) {
		this.dataList.add(new Data(text,Integer.toString(value),selected));
	}
	
	public class Data {
		private String value;
		private String text;
		private boolean selected;
		public Data(String text,String value,boolean selected) {
			this.text = text;
			this.value = value;
			this.selected = selected;
		}
		public Data(String text,String value) {
			this(text,value,false);
		}
		
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public boolean isSelected() {
			return selected;
		}
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
	}
}
