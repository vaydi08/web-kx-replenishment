package com.sol.kx.web.dao.compare;

import java.util.List;

import org.sol.util.mybatis.BaseMapper;

import com.sol.kx.web.dao.pojo.Compare;

public interface CompareMapper extends BaseMapper<Compare>{
	public String createSupplyTempTable();
	
	public void insertSupplyTempTable(Compare obj);
	
	public List<Compare> compareSupply(Compare obj);
	
	public void removeSupplyTempTable(Compare obj);
}
