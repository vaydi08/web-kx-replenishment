package com.sol.kx.web.dao.order;

import java.util.List;

import org.sol.util.mybatis.BaseMapper;

import com.sol.kx.web.dao.pojo.OrderCount;
import com.sol.kx.web.dao.pojo.OrderType;

public interface OrderTypeMapper extends BaseMapper<OrderType>{
	
	public List<OrderType> untake(OrderType obj);
	public int untakeCount();
	
	public List<OrderType> self(OrderType obj);
	public int selfCount(OrderType obj);
	
	public List<OrderType> all(OrderType obj);
	public int allCount();
	
	public OrderType get(OrderType obj);
	
	public OrderCount orderCount(OrderType obj);
}
