package com.sol.kx.web.dao;

import java.sql.SQLException;
import java.util.List;

import com.sol.kx.web.dao.pojo.CargoCompare;
import com.sol.kx.web.dao.pojo.Compare;

public interface CompareDao {
	public void startTransaction() throws SQLException;

	public void commit() throws SQLException;

	public void rollback() throws SQLException;

	public void close();

	public void createSupplyTempTable() throws SQLException;

	public void insertSupplyTempTable(String pcode, Double weight)
			throws SQLException;

	public List<Compare> compareSupply(int shopid) throws Exception;

	public void removeTempTable() throws SQLException;
	
	
	// CARGO
	public void cargoSupplyCreateTempTable(Integer stocktype) throws SQLException;
	public void cargoSupplyRemoveTempTable() throws SQLException;
	public void cargoSupplyDataUpdate(String pcode,String shopname,Double weight) throws SQLException;
	
	public void cargoSaleCreateTempTable() throws SQLException;
	public void cargoSaleRemoveTempTable() throws SQLException;
	public void cargoSaleDataUpdate(String serial,Integer sale,long saletime,String shopname,String pcode,Double weight,Integer stocktype) throws SQLException;
	public void cargoSaleDateDelete(String serial) throws SQLException;
	
	public void cargoStockCreateTempTable() throws SQLException;
	public void cargoStockRemoveTempTable() throws SQLException;
	public void cargoStockDataUpdate(String serial,Integer num,String pcode,Double weight) throws SQLException;
	
	// cargo compare
	public void cargoCompareCreateTb(Integer minallot) throws SQLException;
	public List<CargoCompare> cargoFindStock() throws SQLException;
	public void cargoStockUpdate(String shopname,String serial,Double weight) throws SQLException;
	public Object[] cargoCompareUpdateTb(CargoCompare cargoCompare) throws SQLException;
	public List<CargoCompare> cargoFindResult() throws SQLException;
	public Object[] cargoCompareUpdateTbNormal(CargoCompare cargoCompare) throws SQLException;
}