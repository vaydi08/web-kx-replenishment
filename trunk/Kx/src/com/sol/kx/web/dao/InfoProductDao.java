package com.sol.kx.web.dao;

import java.sql.SQLException;
import java.util.List;

import org.sol.util.c3p0.Condition;
import org.sol.util.c3p0.dataEntity2.InsertEntity;
import org.sol.util.c3p0.dataEntity2.SelectEntity;

import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.dao.pojo.InfoProductDetail;
import com.sol.kx.web.dao.pojo.StockCheck;

public interface InfoProductDao extends BaseDao{
	public List<InfoProduct> find(int page, int pageSize,Condition condition) throws Exception;
	public List<InfoProduct> findFuzzy(int page,int pageSize,String value) throws Exception;
	
	public int findCount(Condition condition) throws Exception;
	public int findCountFuzzy(String value) throws SQLException;
	
	public int addNotExists(InfoProduct infoProduct) throws SQLException;
	
	public List<InfoProduct> findCode2Id() throws Exception;
	
	public int addProductDetail(InsertEntity entity) throws Exception;
	
	public List<InfoProductDetail> findProductDetails(SelectEntity entity) throws Exception;
	
	public List<StockCheck> findQuickLocator(Integer pid) throws Exception;
	
	public List<InfoProduct> findExport() throws Exception;
}