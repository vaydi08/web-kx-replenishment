package org.sol.util.db;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DataConsole {
	/**
	 * 连接
	 */
	private Connection connection;
	/**
	 * 预处理对象
	 */
	private PreparedStatement ps;
	/**
	 * 存储过程处理对象
	 */
	private CallableStatement cs;

	/**
	 * 结果集对象
	 */
	private ResultSet rs;
	/**
	 * 数据源
	 */
	private String sourceName;
	
	/**
	 * 连接获取代理
	 */
	private IConnectionProvider connectionProvider;

	/**
	 * 事务超时事件
	 */
	private int queryTime;
	
	private static final Log log = LogFactory.getLog(DataConsole.class);
	
	/**
	 * 生成一个JDBC封装对象
	 * @param connectionProvider 连接提供代理
	 * @param sourceName 数据源名称,connectionProvider根据此名称切换数据源
	 * @param queryTime 事务处理超时时间 0:无超时
	 */
	public DataConsole(IConnectionProvider connectionProvider,String sourceName,int queryTime) {
		this.connectionProvider = connectionProvider;
		this.sourceName = sourceName;
		this.queryTime = queryTime;
	}
	
	/**
	 * 统计语句查询 直接返回唯一值
	 * @param sql
	 * @param objs
	 * @return
	 * @throws SQLException
	 */
	public Object findReturn(String sql,Object... objs) throws SQLException {
		log.debug("Query return value:[" + sql + "] " + (objs != null ? Arrays.deepToString(objs) : ""));

		try {
			getConnection();
			
			ps = connection.prepareStatement(sql);
			ps.setQueryTimeout(queryTime);
			
			if(objs != null) {
				for(int i = 0 ; i < objs.length; i ++)
					ps.setObject(i + 1, objs[i]);
			}
			rs = ps.executeQuery();
			if(rs.next())
				return rs.getObject(1);
			else
				return null;
		} finally {
			close();
		}
	}
	
	/**
	 * 执行存储过程 首字段用于处理返回值 所以存储过程写法必须是 {?=call PRODUCENAME(?,?...,?)}
	 * @param call 存储过程
	 * @param returnType 返回参数类型 [Types.XXXX]
	 * @param objs 参数列表
	 * @return
	 * @throws SQLException
	 */
	public Object callWithReturn(String call,int returnType,Object... objs) throws SQLException {
		log.debug("Call return value:[" + call + "] " + (objs != null ? Arrays.deepToString(objs) : ""));
		try {
			getConnection();

			cs = connection.prepareCall(call);
			cs.setQueryTimeout(queryTime);
			
			cs.registerOutParameter(1, returnType);
			for(int i = 0; i < objs.length; i ++)
				cs.setObject(i+2,objs[i]);
			
			cs.execute();
			return cs.getObject(1);
		} finally {
			close();
		}
	}
	
	/**
	 * 用于执行返回列表的存储过程  并映射到对象列表上
	 * @param <X> 
	 * @param clazz 映射对象
	 * @param sql 查询语句
	 * @param smap 映射配置表<字段名,类型>
	 * @param objs 参数列表
	 * @return
	 * @throws Exception
	 */
	public <X> List<X> call(Class<X> clazz,String sql,Map<String,Class<?>> smap,Object... objs) throws Exception {
		log.debug("Call Query produce:[" + sql + "] " + (objs != null ? Arrays.deepToString(objs) : ""));
		try {
			rs = call(sql, objs);
			
			List<X> list = new ArrayList<X>();
			
			while(rs != null && rs.next()) {
				X obj = returnObject(clazz,smap);
				
				list.add(obj);
			}
			
			return list;
		} catch (Exception e) {
			close();
			throw e;
		} finally {
			close();
		}
	}
	
	private ResultSet call(String sql,Object... params) throws SQLException {
		getConnection();
		
		cs = connection.prepareCall(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		cs.setQueryTimeout(queryTime);
		
		cs.registerOutParameter(1, Types.REAL);
		for(int i = 0; i < params.length; i ++)
			cs.setObject(i + 2, params[i]);
		
		return cs.executeQuery();
	}
	
	/**
	 * 查询单个对象 并映射到对象上
	 * @param <X> 
	 * @param clazz 映射对象
	 * @param sql 查询语句
	 * @param smap 映射配置表<字段名,类型>
	 * @param objs 参数列表
	 * @return
	 * @throws Exception
	 */
	public <X> X get(Class<X> clazz,String sql,Map<String,Class<?>> smap,Object... objs) throws SQLException {
		log.debug("Get Entity:[" + sql + "] " + (objs != null ? Arrays.deepToString(objs) : ""));
		try {
			rs = query(sql, objs);
			
			if(rs != null && rs.next()) {
				X obj = returnObject(clazz,smap);
				
				return obj;
			} else {
				return null;
			}
			
		} catch (Exception e) {
			throw new SQLException(e);
		} finally {
			close();
		}
	}
	
	private ResultSet query(String sql,Object... objs) throws Exception {
		getConnection();

		ps = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ps.setQueryTimeout(queryTime);
		
		if(objs != null) {
			for(int i = 0; i < objs.length; i ++)
				ps.setObject(i + 1, objs[i]);
		}
		return ps.executeQuery();
	}
	/**
	 * 用于解析查询结果 并映射到对象
	 * @param <X>
	 * @param clazz 映射对象
	 * @param smap 映射表
	 * @return
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws SQLException
	 * @throws IllegalAccessException
	 */
	private <X> X returnObject(Class<X> clazz,Map<String,Class<?>> smap) throws InstantiationException,  SecurityException, NoSuchMethodException, SQLException, IllegalAccessException {
		X obj = clazz.newInstance();
		
		for(Entry<String,Class<?>> en : smap.entrySet()) {
			try {
				Object value = rs.getObject(en.getKey());
				
				setField(obj,en.getKey(),en.getValue(),(en.getValue().equals(String.class) ? (value != null ? value : null) : value));
			} catch (IllegalArgumentException e1) {
				log.error("不正确的对象映射. 映射类:" + clazz.getName() + 
						" 配置字段名:" + en.getKey() + 
						" 类型:" + en.getValue().getName() + 
						" 数据库字段类型:" + rs.getObject(en.getKey()).getClass().getName());
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
			}
		}
		
		return obj;
	}
	
	/**
	 * 设置对象上字段的值
	 * 现在只支持简单的对象类型 String Integer Short Double 等标准对象
	 * 可以扩展这个方法用来支持一些比较复杂的对象格式
	 * @param obj 映射的对象
	 * @param fieldname 字段名称 将调用它的set方法进行设置
	 * @param type 字段类型
	 * @param value 字段值
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	protected void setField(Object obj,String fieldname,Class<?> type,Object value) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException  {
		Method method = obj.getClass().getMethod(setMethod(fieldname),type);
		method.invoke(obj, value);
	}

	/**
	 * 执行插入语句,并返回生成的主键
	 * @param sql 插入语句
	 * @param objs 参数列表
	 * @return 插入语句返回的主键值
	 * @throws SQLException
	 */
	public int insertAndReturnKey(String sql,Object... objs) throws SQLException {
		int countRow = 0;
		int key = 0;
		
		log.debug("Insert and return Id:[" + sql + "] " + (objs != null ? Arrays.deepToString(objs) : ""));
		try {
			getConnection();
			
			connection.setAutoCommit(false);

			ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
			ps.setQueryTimeout(queryTime);
			
			if(objs != null) {
				for(int i = 0; i < objs.length; i ++)
					ps.setObject(i+1,objs[i]);
			}
			
			countRow = ps.executeUpdate();
			if(countRow > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if(rs.next())
					key = rs.getInt(1);
			}
			connection.commit();
		} catch (SQLException e) {
			countRow = 0;
			connection.rollback();
			closeConnection();
			throw e;
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			close();
		}
		return key;
	}
	
	/**
	 * 执行预编译SQL 
	 * @param sql SQL语句
	 * @param objs 参数列表
	 * @return 执行影响条数
	 * @throws SQLException
	 */
	public int updatePrepareSQL(String sql,Object... objs) throws SQLException {
		log.debug("Update:[" + sql + "]" + (objs != null ? objs.toString() : ""));
		
		int countRow = 0;
		try {
			getConnection();

			connection.setAutoCommit(false);

			ps = connection.prepareStatement(sql);
			ps.setQueryTimeout(queryTime);
			if(objs != null) {
				for(int i = 0; i < objs.length; i ++)
					ps.setObject(i+1,objs[i]);
			}
			countRow = ps.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			countRow = 0;
			connection.rollback();
			closeConnection();
			throw e;
		} finally {
			if (connection != null) {
				connection.setAutoCommit(true);
			}
			close();
		}
		return countRow;
	}

	public <X> List<X> find(String sql,Class<X> clazz,Map<String,Class<?>> smap,Object... objs) throws SQLException {
		try {
			log.debug("Query:" + sql + " Class:" + clazz.getName() + (objs != null ? Arrays.deepToString(objs) : ""));
			
			rs = query(sql, objs);
			
			List<X> list = new ArrayList<X>();
			
			while(rs != null && rs.next()) {
				X obj = returnObject(clazz,smap);
				
				list.add(obj);
			}
			
			return list;
		} catch (Exception e) {
			throw new SQLException(e);
		} finally {
			close();
		}
	}

	public void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
			connection = null;
		} catch (Exception e) {
		}
	}
	
	public Connection getConnection() throws SQLException {
		if(sourceName == null)
			throw new SQLException("没有设置数据源");
		
		int Times = 0;
		while (connection == null || connection.isClosed()) {
			try {
				closeConnection();
				connection = connectionProvider.getConnection(sourceName);
				break;
			} catch (Exception sqle) {
				log.error("error getConnection():" + sqle.getMessage(),sqle);
			}finally{
				if(Times>5){
					throw new SQLException("获取连接次数已经超过6次。不再尝试重新获取");
				}
				++Times;
			}
		}
		return connection;
	}
	
	public void close() {
		try {
			super.finalize();
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (ps != null) {
				ps.close();
				ps = null;
			}
			if (cs != null) {
				cs.close();
				cs = null;
			}
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (Throwable te) {
		}
	}

	public static Map<String,Class<?>> parseSmap(Class<?> clazz,String... paramNames) {
		Map<String,Class<?>> smap = new HashMap<String, Class<?>>(paramNames.length);
		
		for(String name : paramNames) {
			try {
				Field field = clazz.getDeclaredField(name);
				smap.put(name, field.getType());
			} catch (Exception e) {
				throw new RuntimeException(e);
			} 
		}
		
		return smap;
	}
	
	public static String getMethod(String name) {
		return "get" + name.replaceFirst(name.substring(0,1), name.substring(0,1).toUpperCase());
	}
	
	public static String setMethod(String name) {
		return "set" + name.replaceFirst(name.substring(0,1), name.substring(0,1).toUpperCase());
	}
}
