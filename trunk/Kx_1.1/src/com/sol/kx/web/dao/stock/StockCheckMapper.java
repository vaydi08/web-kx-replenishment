package com.sol.kx.web.dao.stock;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;
import org.sol.util.mybatis.BaseMapper;
import org.sol.util.mybatis.SelectTemplate;

import com.sol.kx.web.dao.pojo.StockCheck;
import com.sol.kx.web.dao.pojo.StockCheckSum;
import com.sol.kx.web.dao.pojo.StockChecked;

public interface StockCheckMapper extends BaseMapper<StockCheck>{
	@SelectProvider(type = SelectTemplate.class,method = "select")
	public List<StockCheck> select(StockCheck obj);
	
	public StockCheckSum stockCheckSum(StockCheck obj);
	
	public void copyCheck(StockCheck obj);
	
	public List<StockChecked> selectChecked(StockChecked obj);
	
	public List<StockCheck> selectCheckedDetail(StockCheck obj);
}
