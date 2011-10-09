package com.sol.kx.web.dao.impl;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sol.util.c3p0.dataEntity2.SelectEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.sol.kx.web.dao.OrderTypeDao;
import com.sol.kx.web.dao.pojo.OrderType;

@Repository
public class OrderTypeDaoImpl extends BaseDaoImpl implements OrderTypeDao{
	@Value("${sql.order.untake.find}")
	private String SQL_MAIN_UNTAKE;
	
	public List<OrderType> findUntake(int page,int pageSize) throws Exception {
		StringBuilder sb = new StringBuilder();

		sb.append("select * from(select top ").append(pageSize).append(" * From (");
		sb.append("select top ").append(page * pageSize).append(SQL_MAIN_UNTAKE.substring(6)).append(") find1 order by id asc) find2 order by id desc");

		return dataConsole.find(sb.toString(),OrderType.class,
				dataConsole.parseSmap(OrderType.class, "id","pname","shopname","pcode","fromwho","ordertime"),
				null);
	}
	
	@Value("${sql.order.untake.findcount}")
	private String SQL_MAIN_UNTAKE_COUNT;
	
	public int findUntakeCount() throws SQLException {
		return (Integer)dataConsole.findReturn(SQL_MAIN_UNTAKE_COUNT, Types.INTEGER, null);
	}
	
	
	@Value("${sql.order.self.find}")
	private String SQL_MAIN_SELF;
	
	public List<OrderType> findSelf(int page,int pageSize,Integer userid) throws Exception {
		StringBuilder sb = new StringBuilder();

		sb.append("select * from(select top ").append(pageSize).append(" * From (");
		sb.append("select top ").append(page * pageSize).append(SQL_MAIN_SELF.substring(6)).append(") find1 order by id asc) find2 order by id desc");

		List<Object> params = new ArrayList<Object>(1);
		params.add(userid);
		
		return dataConsole.find(sb.toString(),OrderType.class,
				dataConsole.parseSmap(OrderType.class, "id","pname","shopname","pcode","fromwho","ordertime"),
				params);
	}
	
	@Value("${sql.order.self.findcount}")
	private String SQL_MAIN_SELF_COUNT;
	
	public int findSelfCount(Integer userid) throws SQLException {
		List<Object> params = new ArrayList<Object>(1);
		params.add(userid);
		return (Integer)dataConsole.findReturn(SQL_MAIN_SELF_COUNT, Types.INTEGER, params);
	}
	
	@Value("${sql.order.get}")
	private String SQL_GET;
	
	public OrderType get(SelectEntity entity) throws Exception {
		Map<String,Class<?>> smap = dataConsole.parseSmap(OrderType.class,
				"pname","pcode")
		return dataConsole.get(OrderType.class, SQL_GET, entity.getSmap(), entity.getCriteria().getParamList());
	}
	
	@Override
	protected <X> X get(Class<X> clazz, String sql, Integer id)
			throws Exception {
		// TODO Auto-generated method stub
		return super.get(clazz, sql, id);
	}
}
