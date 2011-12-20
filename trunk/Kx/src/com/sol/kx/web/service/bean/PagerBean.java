package com.sol.kx.web.service.bean;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sol.kx.web.common.Constants;

public class PagerBean<T> {
	private List<T> dataList;
	
	private int page;
	
	private int pageSize;
	
	private int count;
	
	private Throwable exception;
	
	private Object[] reserve;
	
	public PagerBean() {
		page = 1;
		pageSize = Constants.pageSize;
		count = 0;
		
		exception = null;
	}
	
	public String getJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("success", exception == null);
			if(exception != null) {
				json.put("msg", this.exception.getMessage());
			}
			json.put("total", this.count);
			if(this.dataList != null)
				json.put("rows", new JSONArray(this.dataList));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json.toString();
	}

	public List<T> getDataList() {
		return dataList;
	}

	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}
	
	public boolean hasException() {
		return exception != null;
	}

	public boolean hasReserve() {
		return reserve != null && reserve.length > 0;
	}
	
	public Object[] getReserve() {
		return reserve;
	}

	public void setReserve(Object[] reserve) {
		this.reserve = reserve;
	}
}
