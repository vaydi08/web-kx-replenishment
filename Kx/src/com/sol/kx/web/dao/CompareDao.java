package com.sol.kx.web.dao;

import java.sql.SQLException;
import java.util.List;

import com.sol.kx.web.dao.pojo.Compare;

public interface CompareDao {

	public void startTransaction() throws SQLException;

	public void commit() throws SQLException;

	public void rollback() throws SQLException;

	public void close();

	public void createSupplyTempTable() throws SQLException;

	public void insertSupplyTempTable(String pcode, Double weight)
			throws SQLException;

	public List<Compare> compareSupply(int shopid,int stocktype) throws Exception;

	public void removeTempTable() throws SQLException;
}