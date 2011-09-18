package com.sol.kx.web.service.bean;

import java.util.ArrayList;
import java.util.List;

public class ImportResultBean {
	private int count;
	private int success;
	private List<String> errorMsg;
	
	public ImportResultBean() {
		this.count = 0;
		this.errorMsg = new ArrayList<String>();
	}
	
	public void anSuccess() {
		++ count;
		++ success;
	}
	
	public void anError(String msg) {
		++ count;
		errorMsg.add(msg);
	}

	public int getCount() {
		return count;
	}

	public int getSuccess() {
		return success;
	}

	public List<String> getErrorMsg() {
		return errorMsg;
	}
}
