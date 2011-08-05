package org.sol.util.c3p0;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sol.util.log.Logger;


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
//	private Statement st;
	/**
	 * 结果集对象
	 */
	private ResultSet rs;
	/**
	 * 数据源
	 */
	private String sourceName;
	/**
	 * C3P0池
	 */
	private C3p0Source ce = null; 

	private int queryTime;
	
	private Log log = LogFactory.getLog(DataConsole.class);
	
	public DataConsole(String sourceName,C3p0Source ce,int queryTime) {
		this.sourceName = sourceName;
		this.ce = ce;
		this.queryTime = queryTime;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public void setCe(C3p0Source ce) {
		this.ce = ce;
	}
	
	/**
	 * 获取连接 尝试5次
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		if(sourceName == null)
			throw new SQLException("没有设置数据源 运行setSourceName(String)");
		
		int Times = 0;
		while (connection == null || connection.isClosed()) {
			try {
				closeConnection();
//				ce = C3p0Source.getInstance();
				connection = ce.getConnection(sourceName);
			} catch (Exception sqle) {
				sqle.printStackTrace();
				log.error("error getConnection():"
						+ sqle.getMessage());
			} catch (Throwable ae) {
				log.error("error getConnection():" + ae.getMessage());
			}finally{
				if(Times>5){
					throw new SQLException("获取连接次数已经超过6次。不再尝试重新获取");
//					break;
				}
				Times++;
			}
		}
		return connection;// 
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;// 
	}
	
	/**
	 * 执行预编译SQL 
	 * @param sql SQL语句
	 * @param objs 参数列表
	 * @return 执行影响条数
	 * @throws SQLException
	 */
	public int updatePrepareSQL(String sql,Object... objs) throws SQLException {
		int countRow = 0;
		try {
			if (connection == null || connection.isClosed()) {
				getConnection();
			}
			connection.setAutoCommit(false);

			ps = connection.prepareStatement(sql);
			ps.setQueryTimeout(queryTime);
			for(int i = 0; i < objs.length; i ++)
				ps.setObject(i+1,objs[i]);
			
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
	
	/**
	 * 执行批处理更新
	 * @param sql
	 * @param paramList
	 * @return
	 * @throws SQLException
	 */
	public int[] updateBatch(String sql,List<Object[]> paramList) throws SQLException {
		int countRow[] = new int[paramList.size()];
		try {
			if (connection == null || connection.isClosed()) {
				getConnection();
			}
			connection.setAutoCommit(false);

			ps = connection.prepareStatement(sql);
			ps.setQueryTimeout(queryTime);
			
			for(Object[] objs : paramList) {
				for(int i = 0; i < objs.length; i ++)
					ps.setObject(i+1,objs[i]);
				
				ps.addBatch();
			}
			
			countRow = ps.executeBatch();
			connection.commit();
		} catch (SQLException e) {
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
	
	/**
	 * 返回INT的存储过程
	 * @param call 存储
	 * @param params 参数列表
	 * @return
	 * @throws SQLException
	 */
	public int callWithReturnInt(String call,Object... params) throws SQLException {
		try {
			if (connection == null || connection.isClosed()) {
				getConnection();
			}

			cs = connection.prepareCall(call);
			cs.setQueryTimeout(queryTime);
			
			cs.registerOutParameter(1, Types.INTEGER);
			for(int i = 0; i < params.length; i ++)
				cs.setObject(i+2,params[i]);
			
			cs.execute();
			return cs.getInt(1);
		} finally {
			close();
		}
	}
	
	/**
	 * 查询单个对象 并映射到对象上
	 * @param <X> 
	 * @param clazz 映射对象
	 * @param sql SQL语句
	 * @param smap 映射配置表<字段名,类型>
	 * @param params 参数列表
	 * @return
	 * @throws Exception
	 */
	public <X> X get(Class<X> clazz,String sql,Map<String,Class<?>> smap,Object... params) throws Exception {
		try {
			rs = query(sql, params);
			
			if(rs != null && rs.next()) {
				X obj = returnObject(clazz,smap);
				
				return obj;
			} else {
				return null;
			}
			
		} catch (Exception e) {
			close();
			throw e;
		} finally {
			close();
		}
	}
	
	/**
	 * 查询 并映射到对象列表上
	 * @param <X> 
	 * @param clazz 映射对象
	 * @param sql SQL语句
	 * @param smap 映射配置表<字段名,类型>
	 * @param params 参数列表
	 * @return
	 * @throws Exception
	 */
	public <X> List<X> find(Class<X> clazz,String sql,Map<String,Class<?>> smap,Object... params) throws Exception {
		try {
			rs = query(sql, params);
			
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
	
	/**
	 * 查询 并映射到对象列表上
	 * @param <X> 
	 * @param clazz 映射对象
	 * @param sql SQL语句
	 * @param smap 映射配置表<字段名,类型>
	 * @param params 参数列表
	 * @return
	 * @throws Exception
	 */
	public <X> List<X> call(Class<X> clazz,String sql,Map<String,Class<?>> smap,Object... params) throws Exception {
		try {
			rs = call(sql, params);
			
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
	
	private ResultSet query(String sql,Object... params) throws Exception {
		if (connection == null || connection.isClosed()) {
			getConnection();
		}
		ps = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ps.setQueryTimeout(queryTime);
		
		for(int i = 0; i < params.length; i ++)
			ps.setObject(i + 1, params[i]);
		
		return ps.executeQuery();
	}
	
	private ResultSet call(String sql,Object... params) throws SQLException {
		if (connection == null || connection.isClosed()) {
			getConnection();
		}
		cs = connection.prepareCall(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		cs.setQueryTimeout(queryTime);
		
		cs.registerOutParameter(1, Types.REAL);
		for(int i = 0; i < params.length; i ++)
			cs.setObject(i + 2, params[i]);
		
		return cs.executeQuery();
	}
	
	private <X> X returnObject(Class<X> clazz,Map<String,Class<?>> smap) throws InstantiationException,  SecurityException, NoSuchMethodException, SQLException, IllegalAccessException {
		X obj = clazz.newInstance();
		
		Set<Entry<String,Class<?>>> set = smap.entrySet();
		for(Entry<String,Class<?>> en : set) {
			String methodName = "set" + en.getKey().replaceFirst(en.getKey().substring(0, 1),en.getKey().substring(0, 1).toUpperCase());
			Method method = clazz.getMethod(methodName, en.getValue());
			try {
				method.invoke(obj, en.getValue().equals(String.class) ? rs.getObject(en.getKey()).toString() : rs.getObject(en.getKey()));
			} catch (IllegalArgumentException e) {
				Logger.DB.lerror("不正确的对象映射.", methodName,en.getValue().getName(),rs.getObject(en.getKey()).getClass().getName(),clazz.getName());
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		return obj;
	}
		
	/**
	 * 统计语句查询 直接返回值
	 * @param sql
	 * @param objs
	 * @return
	 * @throws SQLException
	 */
	public int findReturn(String sql,Object... objs) throws SQLException {
		try {
			if (connection == null || connection.isClosed()) {
				getConnection();
			}
			ps = connection.prepareStatement(sql);
			ps.setQueryTimeout(queryTime);
			
			for(int i = 0 ; i < objs.length; i ++)
				ps.setObject(i + 1, objs[i]);
			
			rs = ps.executeQuery();
			if(rs.next())
				return rs.getInt(1);
			else
				return 0;
		} catch (SQLException e) {
			close();
			throw e;
		} finally {
			close();
		}
	}
	
	public String findReturnStr(String sql,Object... objs) throws SQLException {
		try {
			if (connection == null || connection.isClosed()) {
				getConnection();
			}
			ps = connection.prepareStatement(sql);
			ps.setQueryTimeout(queryTime);
			
			for(int i = 0 ; i < objs.length; i ++)
				ps.setObject(i + 1, objs[i]);
			
			rs = ps.executeQuery();
			if(rs.next())
				return rs.getString(1);
			else
				return "";
		} catch (SQLException e) {
			close();
			throw e;
		} finally {
			close();
		}
	}
	
	public void closeResult() {
		try {
			if (rs != null) {
				rs.close();
			}
			rs = null;
		} catch (Exception e) {
		}
	}

	public void closePreparedStatement() {
		try {
			if (ps != null) {
				ps.close();
			}
			ps = null;
		} catch (Exception e) {
		}
	}

	public void closeCallStatement() {
		try {
			if (cs != null) {
				cs.close();
			}
			cs = null;
		} catch (Exception e) {
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
	
	public String getSourceName() {
		return sourceName;
	}
	
	public void destoryAll() {
		try {
			if(ce != null) {
				ce.finalize();
				ce= null; 
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
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
	
	protected void finalize() {
		try {
			ce.finalize();
			super.finalize();
			closeResult();
			closePreparedStatement();
			closeCallStatement();
			closeConnection();
		} catch (Throwable te) {
		}
	}

	public int getQueryTime() {
		return queryTime;
	}

	public void setQueryTime(int queryTime) {
		this.queryTime = queryTime;
	}

}
