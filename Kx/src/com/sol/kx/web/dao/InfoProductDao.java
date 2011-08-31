package com.sol.kx.web.dao;

import java.util.List;

import org.sol.util.c3p0.Condition;

import com.sol.kx.web.dao.pojo.InfoProduct;

public interface InfoProductDao extends BaseDao{
	public List<InfoProduct> find(int page, int pageSize,Condition condition) throws Exception;
	
	public int findCount(Condition condition) throws Exception;
}