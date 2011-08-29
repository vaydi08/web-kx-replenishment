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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sol.util.c3p0.dataEntity.CountDataEntity;
import org.sol.util.c3p0.dataEntity.DeleteDataEntity;
import org.sol.util.c3p0.dataEntity.InsertDataEntity;
import org.sol.util.c3p0.dataEntity.SelectDataEntity;
import org.sol.util.c3p0.dataEntity.UpdateDataEntity;
import org.sol.util.common.StringUtil;
import org.sol.util.log.Logger;

public class DataConsoleAnnotation {
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
	 * 连接获取器
	 */
	private ConnectionProvider connectionProvider;

	private int queryTime;
	
	private Log log = LogFactory.getLog(DataConsole.class);
	
	public DataConsoleAnnotation(String sourceName,C3p0Source ce,int queryTime) {
		this.sourceName = sourceName;
		this.connectionProvider = new C3p0Provider(ce);
		this.queryTime = queryTime;
	}
	
	public DataConsoleAnnotation(String sourceName,int queryTime) {
		this.sourceName = sourceName;
		this.connectionProvider = new TomcatProvider();
		this.queryTime = queryTime;
	}
	
	
	/**
	 * 统计语句查询 直接返回值
	 * @param sql
	 * @param objs
	 * @param returnType
	 * @return
	 * @throws SQLException
	 */
	public Object findReturn(String sql,int returnType,List<Object> objs) throws SQLException {
		try {
			getConnection();
			
			ps = connection.prepareStatement(sql);
			ps.setQueryTimeout(queryTime);
			
			if(objs != null) {
				for(int i = 0 ; i < objs.size(); i ++)
					ps.setObject(i + 1, objs.get(i));
			}
			rs = ps.executeQuery();
			if(rs.next())
				return rs.getObject(1);
			else
				return null;
		} catch (SQLException e) {
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
			rs = call(sql, (params != null && params.length > 0) ? Arrays.asList(params) : null);
			
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
	 * 查询单个对象 并映射到对象上,以POJO映射
	 * @param <X> 
	 * @param clazz 映射对象
	 * @param params 参数列表
	 * @return
	 * @throws Exception
	 */
	public <X> X get(Class<X> clazz,Object obj) throws Exception {
		SelectDataEntity selectDataEntity = new SelectDataEntity(obj);
		
		return get(clazz,selectDataEntity.getSql(),selectDataEntity.getSmap(),selectDataEntity.getParams());
	}
	public <X> X get(Class<X> clazz,String sql,Condition condition) throws Exception {
		SelectDataEntity selectDataEntity = new SelectDataEntity(sql,condition,clazz);
		
		return get(clazz,selectDataEntity.getSql(),selectDataEntity.getSmap(),selectDataEntity.getParams());
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
	public <X> X get(Class<X> clazz,String sql,Map<String,Class<?>> smap,List<Object> params) throws Exception {
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
	 * 执行带有返回值的存储过程
	 * @param call 存储
	 * @param returnType 返回参数类型 [Types.XXXX]
	 * @param params 参数列表
	 * @return
	 * @throws SQLException
	 */
	public Object callWithReturn(String call,int returnType,Object... params) throws SQLException {
		try {
			getConnection();

			cs = connection.prepareCall(call);
			cs.setQueryTimeout(queryTime);
			
			cs.registerOutParameter(1, returnType);
			for(int i = 0; i < params.length; i ++)
				cs.setObject(i+2,params[i]);
			
			cs.execute();
			return cs.getObject(1);
		} finally {
			close();
		}
	}
	
	/**
	 * 执行预编译SQL,并返回生成的主键,以POJO生成SQL和参数列表
	 * @param obj
	 * @return
	 * @throws SQLException
	 */
	public int insertAndReturnKey(Object obj) throws SQLException {
		InsertDataEntity insertDataEntity;
		
		try {
			insertDataEntity = new InsertDataEntity(obj);
		} catch (Exception e) {
			throw new SQLException(e);
		}
		
		return insertPrepareSQLAndReturnKey(insertDataEntity.getSql(), insertDataEntity.getParams().toArray());
	}
	
	public int insert(Object obj) throws Exception {
		InsertDataEntity insertDataEntity = new InsertDataEntity(obj);
		
		return updatePrepareSQL(insertDataEntity.getSql(), insertDataEntity.getParams());
	}

	/**
	 * 执行预编译SQL,并返回生成的主键
	 * @param sql SQL语句
	 * @param objs 参数列表
	 * @return 执行影响条数
	 * @throws SQLException
	 */
	public int insertPrepareSQLAndReturnKey(String sql,Object... objs) throws SQLException {
		int countRow = 0;
		int key = 0;
		try {
			getConnection();
			
			connection.setAutoCommit(false);

			ps = connection.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setQueryTimeout(queryTime);
			for(int i = 0; i < objs.length; i ++)
				ps.setObject(i+1,objs[i]);
			
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
	 * 执行update操作,以pojo生成SQL和参数列表
	 * @param obj
	 * @return
	 * @throws SQLException
	 */
	public int update(Object obj) throws SQLException {
		UpdateDataEntity updateDataEntity;
		try {
			updateDataEntity = new UpdateDataEntity(obj);
		} catch (Exception e) {
			throw new SQLException(e);
		}
		
		return updatePrepareSQL(updateDataEntity.getSql(), updateDataEntity.getParams());
	}
	
	public int delete(Object obj) throws Exception {
		DeleteDataEntity deleteDataEntity = new DeleteDataEntity(obj);
		
		return updatePrepareSQL(deleteDataEntity.getSql(), deleteDataEntity.getParams());
	}
	
	/**
	 * 执行预编译SQL 
	 * @param sql SQL语句
	 * @param objs 参数列表
	 * @return 执行影响条数
	 * @throws SQLException
	 */
	public int updatePrepareSQL(String sql,List<Object> objs) throws SQLException {
		int countRow = 0;
		try {
			getConnection();

			connection.setAutoCommit(false);

			ps = connection.prepareStatement(sql);
			ps.setQueryTimeout(queryTime);
			if(objs != null) {
				for(int i = 0; i < objs.size(); i ++)
					ps.setObject(i+1,objs.get(i));
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
	public <X> List<X> find(Class<X> clazz,String sql,Condition condition) throws Exception {
		SelectDataEntity dataEntity = new SelectDataEntity(sql,condition,clazz);
		
		return find(dataEntity.getSql(), clazz, dataEntity.getSmap(), dataEntity.getParams());
	}
	public <X> List<X> find(Class<X> clazz,Object obj) throws Exception {
		SelectDataEntity dataEntity = new SelectDataEntity(obj);
		
		return find(dataEntity.getSql(), clazz, dataEntity.getSmap(), dataEntity.getParams());
	}
	public <X> List<X> findByPage(Class<X> clazz,Object obj,int page,int pageSize) throws Exception {
		SelectDataEntity dataEntity = new SelectDataEntity(obj);
		
		String sqlpage = dataEntity.getSql().substring(6);
		
		StringBuilder sb = new StringBuilder();
		sb.append("select * from(select top ").append(pageSize).append(" * From (");
		sb.append("select top ").append(page * pageSize).append(sqlpage).append(" order by id desc) find1 order by id asc) find2 order by id desc");

		return find(sb.toString(), clazz, dataEntity.getSmap(), dataEntity.getParams());
	}
	public <X> List<X> findByPage(Class<X> clazz,String sql,Condition condition,int page,int pageSize) throws Exception {
		SelectDataEntity dataEntity = new SelectDataEntity(sql,condition,clazz);
		
		String sqlpage = dataEntity.getSql().substring(6);
		
		StringBuilder sb = new StringBuilder();
		sb.append("select * from(select top ").append(pageSize).append(" * From (");
		sb.append("select top ").append(page * pageSize).append(sqlpage).append(" order by id desc) find1 order by id asc) find2 order by id desc");

		return find(sb.toString(), clazz, dataEntity.getSmap(), dataEntity.getParams());
	}
	public <X> List<X> find(String sql,Class<X> clazz,Map<String,Class<?>> smap,List<Object> params) throws Exception {
		try {
			log.debug("Query:" + sql + " Class:" + clazz.getName());
			
			rs = query(sql, params);
			
			List<X> list = new ArrayList<X>();
			
			while(rs != null && rs.next()) {
				X obj = returnObject(clazz,smap);
				
				list.add(obj);
			}
			
			return list;
		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}
	}
	private ResultSet query(String sql,List<Object> params) throws Exception {
		getConnection();

		ps = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ps.setQueryTimeout(queryTime);
		
		if(params != null) {
			for(int i = 0; i < params.size(); i ++)
				ps.setObject(i + 1, params.get(i));
		}
		return ps.executeQuery();
	}
	private <X> X returnObject(Class<X> clazz,Map<String,Class<?>> smap) throws InstantiationException,  SecurityException, NoSuchMethodException, SQLException, IllegalAccessException {
		X obj = clazz.newInstance();
		
		Set<Entry<String,Class<?>>> set = smap.entrySet();
		for(Entry<String,Class<?>> en : set) {
			try {
				setField(obj,en.getKey(),en.getValue(),rs.getObject(en.getKey()));
			} catch (IllegalArgumentException e1) {
				Logger.DB.lerror("不正确的对象映射.", en.getKey(),en.getValue().getName(),rs.getObject(en.getKey()).getClass().getName(),clazz.getName());
			} catch (InvocationTargetException e1) {
				e1.printStackTrace();
			}
		}
		
		return obj;
	}
	private void setField(Object obj,String fieldname,Class<?> type,Object value) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException  {
		if(fieldname.indexOf('_') > 0) {
			String thisfieldname = fieldname.substring(0,fieldname.indexOf('_'));
			Method thismethod = obj.getClass().getMethod(StringUtil.getMethod(thisfieldname));
			setField(thismethod.invoke(obj),fieldname.substring(fieldname.indexOf('_') + 1),type,value);
		} else {
			Method thismethod = obj.getClass().getMethod(StringUtil.setMethod(fieldname),type);
			thismethod.invoke(obj, value);
		}
	}
	
	/**
	 * 查询统计
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public int findCount(Object obj) throws Exception {
		CountDataEntity countDataEntity = new CountDataEntity(obj);
		
		return (Integer) findReturn(countDataEntity.getSql(), Types.INTEGER, countDataEntity.getParams());
	}
	public int findCount(String sql,Condition condition) throws Exception {
		String where = sql.substring(6);
		where += condition.getWhere();
		
		return (Integer) findReturn(where, Types.INTEGER, condition.getParams());
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
				Times++;
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
	
	/**
	 * 连接获取代理接口
	 * @author sol
	 *
	 */
	private interface ConnectionProvider {
		public Connection getConnection(String sourceName) throws SQLException;
	}
	/**
	 * C3P0连接池代理
	 * @author sol
	 *
	 */
	private class C3p0Provider implements ConnectionProvider{
		private C3p0Source ce; 
		public C3p0Provider(C3p0Source ce) {
			this.ce = ce;
		}
		
		@Override
		public Connection getConnection(String sourceName) throws SQLException {
			return ce.getConnection(sourceName);
		}
	}
	
	private class TomcatProvider implements ConnectionProvider {
		@Override
		public Connection getConnection(String sourceName) throws SQLException {
			try {
				Context ctx = new InitialContext();
				return ((DataSource)ctx.lookup("java:comp/env/" + sourceName)).getConnection();
			} catch (Exception e) {
				throw new SQLException(e);
			}
		}
	}
}
