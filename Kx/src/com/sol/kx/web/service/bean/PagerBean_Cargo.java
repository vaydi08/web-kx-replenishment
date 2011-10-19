package com.sol.kx.web.service.bean;

import java.util.List;

import com.sol.kx.web.dao.pojo.CargoCompare;

public class PagerBean_Cargo extends PagerBean<CargoCompare>{
	private List<CargoCompare> stockList;

	public List<CargoCompare> getStockList() {
		return stockList;
	}

	public void setStockList(List<CargoCompare> stockList) {
		this.stockList = stockList;
	}
}
