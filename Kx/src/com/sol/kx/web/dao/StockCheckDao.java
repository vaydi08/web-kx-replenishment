package com.sol.kx.web.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.dao.pojo.StockCheck;
import com.sol.kx.web.dao.pojo.StockCheckSum;
import com.sol.kx.web.dao.pojo.StockChecked;

public interface StockCheckDao extends BaseDao{
	public List<InfoProduct> findProductList(Map<String,Integer> map,int page,int pageSize) throws Exception;
	
	public int findProductListCount(Map<String,Integer> map) throws Exception;
	
	public String findShopNameById(int shopid) throws SQLException;
	
	public String findCategoryNameById(int id) throws SQLException;
	
	public void updateStockCheck(StockCheck obj) throws Exception;
	
	public void deleteStockCheck(StockCheck obj) throws Exception;
	
	public StockCheckSum findStockCheckSum(Integer shopid,Integer pid) throws Exception;
	
	public void copyCheck(Integer pid,Integer shopid,Integer clevel) throws SQLException;
	
	public List<StockChecked> findCheckedType1(Integer shopid) throws Exception;
	public List<StockChecked> findCheckedType234(Integer shopid,Integer parent,Integer clevel,Integer parentlevel) throws Exception;
}