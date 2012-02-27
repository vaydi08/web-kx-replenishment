package com.sol.kx.web.dao.compare;

import java.util.List;

import org.sol.util.mybatis.BaseMapper;

import com.sol.kx.web.dao.pojo.CargoCompare;
import com.sol.kx.web.dao.pojo.Compare;

public interface CompareMapper extends BaseMapper<Compare>{
	public String createSupplyTempTable();
	
	public void insertSupplyTempTable(Compare obj);
	
	public List<Compare> compareSupply(Compare obj);
	
	public void removeSupplyTempTable(Compare obj);
	
	
	
	
	
	public String createCargoSupplyTempTable();
	public void updateCargoSupply(CargoCompare obj);
	public void removeCargoSupplyTempTable(CargoCompare obj);
	
	public String createCargoSaleTempTable();
	public void insertCargoSale(CargoCompare obj);
	public void removeCargoSaleTempTable(CargoCompare obj);
	
	public String createCargoStockTempTable();
	public void insertCargoStock(CargoCompare obj);
	public void updateCargoStock(CargoCompare obj);
	public void removeCargoStockTempTable(CargoCompare obj);
}
