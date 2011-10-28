package com.sol.lx.mainfesto.service.bean;

import java.util.List;

public class PagerBean<T> {
	private List<T> dataList;
	
	private int page;
	
	private int pageSize;
	
	private int count;
	
	private Throwable exception;
	
	private Object[] reserve;
	
	public PagerBean() {
		page = 1;
		pageSize = 10;
		count = 0;
		
		exception = null;
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
