package com.sol.kx.web.action.compare;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.service.bean.PagerBean;

public class SupplyBean extends PagerBean<Compare>{
	private List<Compare> stocktype2List;

	@Override
	public String getJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("success", getException() == null);
			if(getException() != null) {
				json.put("msg", getException().getMessage());
			}

			if(getStocktype1List() != null) {
				JSONObject obj = new JSONObject();
				obj.put("rows", new JSONArray(getStocktype1List()));
				json.put("stocktype1", obj);
			}
			if(getStocktype2List() != null) {
				JSONObject obj = new JSONObject();
				obj.put("rows", new JSONArray(getStocktype2List()));
				json.put("stocktype2", obj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json.toString();
	}
	
	public void setStocktype1List(List<Compare> stocktype1List) {
		setDataList(stocktype1List);
	}
	public void setStocktype2List(List<Compare> stocktype2List) {
		this.stocktype2List = stocktype2List;
	}
	
	public List<Compare> getStocktype1List() {
		return getDataList();
	}
	public List<Compare> getStocktype2List() {
		return stocktype2List;
	}
}
