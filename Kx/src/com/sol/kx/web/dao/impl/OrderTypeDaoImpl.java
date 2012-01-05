package com.sol.kx.web.dao.impl;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.sol.kx.web.dao.OrderTypeDao;
import com.sol.kx.web.dao.pojo.OrderCount;
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
				dataConsole.parseSmap(OrderType.class, "id","pname","shopname","pcode","fromwho","ordertime","num"),
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
		List<Object> params = new ArrayList<Object>(1);
		params.add(userid);
		if(page > 1)
			params.add(userid);
		
		StringBuilder sb = new StringBuilder();
		if(page > 1) {
			sb.append("declare @id nvarchar(100);");
			sb.append("select @id=min(id) from (select top ")
			.append(page * pageSize - pageSize)
			.append(" id from order_type where status>1 and userid=? order by id desc) tb1;");
		}
		sb.append("set ROWCOUNT ").append(pageSize).append(";");
		sb.append(SQL_MAIN_SELF);
		if(page > 1)
			sb.append(" and id<@id");
		sb.append(" order by od.id desc;");
		sb.append("set ROWCOUNT 0");
		
		return dataConsole.find(sb.toString(),OrderType.class,
				dataConsole.parseSmap(OrderType.class,
						"id","pname","shopname","pcode","fromwho","ordertime","status","num","weight"),
				params);
	}
	
	@Value("${sql.order.self.findcount}")
	private String SQL_MAIN_SELF_COUNT;
	
	public int findSelfCount(Integer userid) throws SQLException {
		List<Object> params = new ArrayList<Object>(1);
		params.add(userid);
		return (Integer)dataConsole.findReturn(SQL_MAIN_SELF_COUNT, Types.INTEGER, params);
	}
	
	
	@Value("${sql.order.all.find}")
	private String SQL_MAIN_ALL;
	
	public List<OrderType> findAll(int page,int pageSize) throws Exception {
		StringBuilder sb = new StringBuilder();

		sb.append("select * from(select top ").append(pageSize).append(" * From (");
		sb.append("select top ").append(page * pageSize).append(SQL_MAIN_ALL.substring(6)).append(") find1 order by id asc) find2 order by id desc");
		
		return dataConsole.find(sb.toString(),OrderType.class,
				dataConsole.parseSmap(OrderType.class, "id","pname","shopname","pcode","fromwho","ordertime","status","username","num"),
				null);
	}
	
	@Value("${sql.order.all.findcount}")
	private String SQL_MAIN_ALL_COUNT;
	
	public int findAllCount() throws SQLException {
		return (Integer)dataConsole.findReturn(SQL_MAIN_ALL_COUNT, Types.INTEGER, null);
	}
	
	@Value("${sql.order.get}")
	private String SQL_GET;
	
	public OrderType get(Integer id) throws Exception {
		Map<String,Class<?>> smap = dataConsole.parseSmap(OrderType.class,
				"id","fromwho","pname","pcode","weight",
				"image","shopname","ordertime","num",
				"requesttime","username","gettime","status");
		List<Object> params = new ArrayList<Object>(1);
		params.add(id);
		return dataConsole.get(OrderType.class, SQL_GET, smap, params);
	}
	
	// 选择产品
	@Value("${sql.order.product.get}")
	private String SQL_CHOOSEPRODUCT;
	
	public List findChooseProduct(String pcode) throws Exception {
		List<Object> params = new ArrayList<Object>(1);
		params.add(pcode);
		
		return null;
//		return dataConsole.find(SQL_CHOOSEPRODUCT, InfoProductDetail.class, 
//				dataConsole.parseSmap(InfoProductDetail.class, 
//						"id","quality","image","pweight","stand","pname"), params);
	}
	
	// 订购统计
	@Value("${sql.order.count}")
	private String SQL_ORDERCOUNT;
	
	public OrderCount findOrderCount(Integer userid) throws Exception {
		List<Object> params = new ArrayList<Object>(1);
		params.add(userid);
		params.add(userid);
		
		return dataConsole.get(OrderCount.class, SQL_ORDERCOUNT, dataConsole.parseSmap(OrderCount.class, 
				"untake","mytake","alert"), params);
	}
	
	
	
	@Override
	protected <X> X get(Class<X> clazz, String sql, Integer id)
			throws Exception {
		// TODO Auto-generated method stub
		return super.get(clazz, sql, id);
	}
}
