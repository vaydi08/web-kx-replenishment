package com.sol.kx.web.dao;

import java.sql.SQLException;
import java.util.List;

import com.sol.kx.web.dao.pojo.InfoProductDetail;
import com.sol.kx.web.dao.pojo.OrderCount;
import com.sol.kx.web.dao.pojo.OrderType;

public interface OrderTypeDao extends BaseDao {
	public List<OrderType> findUntake(int page,int pageSize) throws Exception;
	
	public int findUntakeCount() throws SQLException;
	
	public List<OrderType> findSelf(int page,int pageSize,Integer userid) throws Exception;
	public int findSelfCount(Integer userid) throws SQLException;
	
	public List<OrderType> findAll(int page,int pageSize) throws Exception;
	public int findAllCount() throws SQLException;
	
	public OrderType get(Integer id) throws Exception;
	
	public List<InfoProductDetail> findChooseProduct(String pcode) throws Exception;
	public OrderCount findOrderCount(Integer userid) throws Exception;
}
