package com.sol.kx.web.dao.info;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;
import org.sol.util.mybatis.BaseMapper;
import org.sol.util.mybatis.SelectTemplate;

import com.sol.kx.web.dao.pojo.InfoProduct;

public interface InfoProductMapper extends BaseMapper<InfoProduct>{

	@SelectProvider(type = SelectTemplate.class,method = "select")
	public List<InfoProduct> select(InfoProduct obj);
	
	public List<InfoProduct> selectByPage(InfoProduct obj);
	
	public List<InfoProduct> selectExport(InfoProduct obj);
	
//	public boolean checkExists(String pcode) throws SQLException;
	
//	public List<InfoProduct> findFuzzy(int page,int pageSize,String value) throws Exception;
	
//	public int findCountFuzzy(String value) throws SQLException;
		
//	public List<InfoProduct> findCode2Id() throws Exception;
	
//	public List<StockCheck> findQuickLocator(Integer pid) throws Exception;
	
//	public List<InfoProduct> findExport() throws Exception;
}
