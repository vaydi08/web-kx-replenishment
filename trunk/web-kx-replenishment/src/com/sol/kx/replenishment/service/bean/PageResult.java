package com.sol.kx.replenishment.service.bean;

import java.util.List;

/**
 * 分页数据封装
 * @author HUYAO
 *
 * @param <T>
 */
public class PageResult<T> {
	private List<T> result;
	
	private int pageSize;
	
	private int page;
	
	private int rowCount;

	public PageResult() {
		
	}
	
	public PageResult(List<T> result,int page, int pageSize, int rowCount) {
		this.result = result;
		this.pageSize = pageSize;
		this.page = page;
		this.rowCount = rowCount;
	}

	public PageResult(int page, int pageSize) {
		this.pageSize = pageSize;
		this.page = page;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

}
