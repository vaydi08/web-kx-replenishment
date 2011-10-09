package com.sol.kx.web.dao;

import java.sql.SQLException;
import java.util.List;

import org.sol.util.c3p0.dataEntity2.SelectEntity;

import com.sol.kx.web.dao.pojo.OrderType;

public interface OrderTypeDao extends BaseDao {
	public List<OrderType> findUntake(int page,int pageSize) throws Exception;
	
	public int findUntakeCount() throws SQLException;
	
	public List<OrderType> findSelf(int page,int pageSize,Integer userid) throws Exception;
	public int findSelfCount(Integer userid) throws SQLException;
	
	public OrderType get(SelectEntity entity) throws Exception;
}
