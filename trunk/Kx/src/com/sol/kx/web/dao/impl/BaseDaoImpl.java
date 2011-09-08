package com.sol.kx.web.dao.impl;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.sol.util.c3p0.Condition;
import org.sol.util.c3p0.DataConsoleAnnotation;
import org.sol.util.log.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.BaseDao;


/**
 * 数据操作基类
 * @author HUYAO
 *
 */
public abstract class BaseDaoImpl implements BaseDao{
	// 数据源
//	@Autowired
//	protected C3p0Source c3p0Source;
	
	protected DataConsoleAnnotation dataConsole;
	
	// 数据库操作重试次数
	@Value("${db.retry}")
	protected int DB_RETRY;
	
	@PostConstruct
	public void init() {
		dataConsole = new DataConsoleAnnotation(Constants.DB, Constants.TRANS_TIMEOUT);
	}
	
	/**
	 * 查询并获取单个对象
	 * @param <X>
	 * @param datasource 数据源
	 * @param clazz 反射类
	 * @param sql SQL语句
	 * @param smap 映射表
	 * @param params 参数列表
	 * @return
	 * @throws Exception
	 */
	protected <X> X get(String datasource,Class<X> clazz,String sql,Map<String,Class<?>> smap,Object... params) throws Exception {
		return dataConsole.get(clazz, sql, smap, Arrays.asList(params));
	}
	
	protected Integer findReturnInt(String sql,Object... objs) throws SQLException {
		return (Integer)dataConsole.findReturn(sql,Types.INTEGER, Arrays.asList(objs));
	}
	
	protected String findReturnString(String sql,Object... objs) throws SQLException {
		return (String)dataConsole.findReturn(sql, Types.VARCHAR, Arrays.asList(objs));
	}
	
	/**
	 * 查询并获取对象列表
	 * @param <X>
	 * @param datasource 数据源
	 * @param clazz 反射类
	 * @param sql SQL语句
	 * @param smap 映射表
	 * @param params 参数列表
	 * @return
	 * @throws Exception
	 */
	protected <X> List<X> find(Class<X> clazz,Object obj) throws Exception {
		return dataConsole.find(clazz, obj);
	}
	
	protected <X> List<X> find(Class<X> clazz,String sql,Condition condition) throws Exception {
		return dataConsole.find(clazz,sql,condition);
	}
	
	protected <X> List<X> find(Class<X> clazz,String sql,Map<String,Class<?>> smap,Object... params) throws Exception {
		return dataConsole.find(sql,clazz,smap,Arrays.asList(params));
	}
	
	protected <X> List<X> findByPage(Class<X> clazz,Object obj,int page,int pageSize) throws Exception {
		return dataConsole.findByPage(clazz, obj, page, pageSize);
	}
	
	/**
	 * 查询并获取对象列表
	 * @param <X>
	 * @param datasource 数据源
	 * @param clazz 反射类
	 * @param sql SQL语句
	 * @param smap 映射表
	 * @param params 参数列表
	 * @return
	 * @throws Exception
	 */
	protected <X> List<X> call(Class<X> clazz,String sql,Map<String,Class<?>> smap,Object... params) throws Exception {
		return dataConsole.call(clazz, sql, smap, params);
	}
	
	/**
	 * 执行SQL更新命令,带出错重复执行功能
	 * @param datasource 数据源
	 * @param sql SQL语句
	 * @param preResult 预想的正常返回值(不为此值时,将重试)
	 * @param params 参数列表
	 * @throws Exception
	 */
	protected void executeUpdate(String sql,int preResult,Object... params) throws Exception {
		int i = 0;
		int result = 0;
		String message = null;
		
		do {
			try {
				 result = dataConsole.updatePrepareSQL(sql, Arrays.asList(params));
			} catch (SQLException e) {
				Logger.DB.error(e.getMessage(),e);
				message = e.getMessage();
				result = 0;
			}
		} while (result == 0 && ++i < DB_RETRY);

		if(result == 0 && i == DB_RETRY) {
			Logger.UNCATCH.lerror("执行SQL出错 - " + message + " - " + sql, params);
			throw new Exception(message);
		}
	}
	protected void executeUpdate(Object obj,int preResult) throws Exception {
		int i = 0;
		int result = 0;
		String message = null;
		
		do {
			try {
				 result = dataConsole.update(obj);
			} catch (SQLException e) {
				Logger.DB.error(e.getMessage(),e);
				message = e.getMessage();
				result = 0;
			}
		} while (result == 0 && ++i < DB_RETRY);

		if(result == 0 && i == DB_RETRY) {
			Logger.UNCATCH.lerror("执行SQL出错 - " + message + " - " + obj.toString());
			throw new Exception(message);
		}
	}
	
	protected int executeUpdateAndReturn(String sql,Object... params) throws SQLException {
		return dataConsole.updatePrepareSQL(sql, Arrays.asList(params));
	}
	protected int executeUpdateAndReturn(Object obj) throws SQLException {
		return dataConsole.update(obj);
	}
	
	protected int insertAndReturnKey(String sql,Object... params) throws SQLException {
		return dataConsole.insertPrepareSQLAndReturnKey(sql, params);
	}
	
	protected int insertAndReturnKey(Object obj) throws SQLException {
		return dataConsole.insertAndReturnKey(obj);
	}
	
	
	/**
	 * DAO公用函数
	 */
	
	public <X> List<X> find(Class<X> clazz,Object obj,int page,int pageSize) throws Exception {
		return findByPage(clazz, obj,page,pageSize);
	}
	public int findCount(Object obj) throws Exception {
		return dataConsole.findCount(obj);
	}
	public void add(Object obj) throws Exception {
		dataConsole.insert(obj);
	}
	public void update(Object obj) throws Exception {
		dataConsole.update(obj);
	}
	public void delete(Object obj) throws Exception {
		dataConsole.delete(obj);
	}
}