package com.sol.kx.web.action.compare;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.sol.kx.web.dao.pojo.CargoCompare;
import com.sol.kx.web.service.bean.PagerBean;

public class CargoBean extends PagerBean<CargoCompare>{
	private List<CargoCompare> resultStocktype1List;
	private List<CargoCompare> resultStocktype2List;
	private List<CargoCompare> stockStocktype1List;
	private List<CargoCompare> stockStocktype2List;

	@Override
	public String getJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("success", getException() == null);
			if(getException() != null) {
				json.put("msg", getException().getMessage());
			}

			JSONObject stocktype1 = new JSONObject();
			if(getResultStocktype1List() != null) {
				JSONObject obj = new JSONObject();
				obj.put("rows", new JSONArray(getResultStocktype1List()));
				stocktype1.put("result", obj);
			}
			if(getStockStocktype1List() != null) {
				JSONObject obj = new JSONObject();
				obj.put("rows", new JSONArray(getStockStocktype1List()));
				stocktype1.put("stock", obj);
			}
			json.put("stocktype1",stocktype1);
			
			JSONObject stocktype2 = new JSONObject();
			if(getResultStocktype2List() != null) {
				JSONObject obj = new JSONObject();
				obj.put("rows", new JSONArray(getResultStocktype2List()));
				stocktype2.put("result", obj);
			}
			if(getStockStocktype2List() != null) {
				JSONObject obj = new JSONObject();
				obj.put("rows", new JSONArray(getStockStocktype2List()));
				stocktype2.put("stock", obj);
			}
			json.put("stocktype2",stocktype2);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json.toString();
	}

	public List<CargoCompare> getResultStocktype1List() {
		return resultStocktype1List;
	}

	public void setResultStocktype1List(List<CargoCompare> resultStocktype1List) {
		this.resultStocktype1List = resultStocktype1List;
	}

	public List<CargoCompare> getResultStocktype2List() {
		return resultStocktype2List;
	}

	public void setResultStocktype2List(List<CargoCompare> resultStocktype2List) {
		this.resultStocktype2List = resultStocktype2List;
	}

	public List<CargoCompare> getStockStocktype1List() {
		return stockStocktype1List;
	}

	public void setStockStocktype1List(List<CargoCompare> stockStocktype1List) {
		this.stockStocktype1List = stockStocktype1List;
	}

	public List<CargoCompare> getStockStocktype2List() {
		return stockStocktype2List;
	}

	public void setStockStocktype2List(List<CargoCompare> stockStocktype2List) {
		this.stockStocktype2List = stockStocktype2List;
	}
}
