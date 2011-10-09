package com.sol.kx.web.dao;

import java.sql.SQLException;
import java.util.List;

import com.sol.kx.web.dao.pojo.OrderType;

public interface OrderTypeDao extends BaseDao {
	public List<OrderType> findUntake(int page,int pageSize) throws Exception;
	
	public int findUntakeCount() throws SQLException;
}