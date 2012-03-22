package com.sol.kx.web.service.bean;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sol.kx.web.dao.pojo.Compare;

public class CompareBean {
	// 未匹配名称列表
	private List<Compare> unmatcherList;
	
	// 比对错误列表
	private List<Compare> compareErrorList;
	
	// 比对数据
	private List<Compare> dataList;

	private Throwable exception;
	
	public String getJson() {
		JSONObject json = new JSONObject();
		
		try {
			if(exception != null) 
				json.put("msg", this.exception.getMessage());
			 else 
				 json.put("success", true);
			
			JSONObject dataListJson = new JSONObject();
			if(this.dataList != null)
				dataListJson.put("rows", new JSONArray(this.dataList));
			else
				dataListJson.put("rows", new JSONArray());
			json.put("dataList", dataListJson);
			
			JSONObject unmatcherJson = new JSONObject();
			if(this.unmatcherList != null)
				unmatcherJson.put("rows", new JSONArray(this.unmatcherList));
			else
				unmatcherJson.put("rows", new JSONArray());
			json.put("unmatcherList", unmatcherJson);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json.toString();
	}
	
	public List<Compare> getUnmatcherList() {
		return unmatcherList;
	}

	public void setUnmatcherList(List<Compare> unmatcherList) {
		this.unmatcherList = unmatcherList;
	}

	public List<Compare> getCompareErrorList() {
		return compareErrorList;
	}

	public void setCompareErrorList(List<Compare> compareErrorList) {
		this.compareErrorList = compareErrorList;
	}

	public List<Compare> getDataList() {
		return dataList;
	}

	public void setDataList(List<Compare> dataList) {
		this.dataList = dataList;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}
}
