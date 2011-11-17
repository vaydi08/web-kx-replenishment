package com.sol.kx.web.dao;

import java.sql.SQLException;
import java.util.List;

import org.sol.util.c3p0.dataEntity2.Criteria;
import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.dao.pojo.StockCheck;

public interface InfoProductDao extends BaseDao{
	public List<InfoProduct> find(int page, int pageSize,Criteria criteria) throws Exception;
	public boolean checkExists(String pcode) throws SQLException;
	
	public List<InfoProduct> findFuzzy(int page,int pageSize,String value) throws Exception;
	
	public int findCountFuzzy(String value) throws SQLException;
	
	public int addNotExists(InfoProduct infoProduct) throws SQLException;
	
//	public List<InfoProduct> findCode2Id() throws Exception;
	
	public List<StockCheck> findQuickLocator(Integer pid) throws Exception;
	
	public List<InfoProduct> findExport() throws Exception;
}