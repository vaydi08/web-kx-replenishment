package com.megajoy.thirdpart.report.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.sol.util.c3p0.C3p0Source;
import org.sol.util.c3p0.DataConsole;
import org.sol.util.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.megajoy.thirdpart.report.common.Constants;


/**
 * 数据操作基类
 * @author HUYAO
 *
 */
public abstract class BaseDaoImpl {
	// 数据源
	@Autowired
	protected C3p0Source c3p0Source;
	
	// 数据库操作重试次数
	@Value("${db.retry}")
	protected int DB_RETRY;
	
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
		DataConsole dc = new DataConsole(datasource,c3p0Source,Constants.TRANS_TIMEOUT);
		
		return dc.get(clazz, sql, smap, params);
	}
	
	protected String findReturn(String datasource,String sql,Object... objs) throws SQLException {
		DataConsole dc = new DataConsole(datasource,c3p0Source,Constants.TRANS_TIMEOUT);
		
		return dc.findReturnStr(sql, objs);
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
	protected <X> List<X> find(String datasource,Class<X> clazz,String sql,Map<String,Class<?>> smap,Object... params) throws Exception {
		DataConsole dc = new DataConsole(datasource,c3p0Source,Constants.TRANS_TIMEOUT);
		
		return dc.find(clazz, sql, smap, params);
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
	protected <X> List<X> call(String datasource,Class<X> clazz,String sql,Map<String,Class<?>> smap,Object... params) throws Exception {
		DataConsole dc = new DataConsole(datasource,c3p0Source,Constants.TRANS_TIMEOUT);
		
		return dc.call(clazz, sql, smap, params);
	}
	
	/**
	 * 执行SQL更新命令,带出错重复执行功能
	 * @param datasource 数据源
	 * @param sql SQL语句
	 * @param preResult 预想的正常返回值(不为此值时,将重试)
	 * @param params 参数列表
	 * @throws Exception
	 */
	protected void executeUpdate(String datasource,String sql,int preResult,Object... params) throws Exception {
		DataConsole dc = new DataConsole(datasource,c3p0Source,Constants.TRANS_TIMEOUT);
		
		int i = 0;
		int result = 0;
		String message = null;
		
		do {
			try {
				 result = dc.updatePrepareSQL(sql, params);
			} catch (SQLException e) {
				Logger.DB.error(e.getMessage(),e);
				message = e.getMessage();
				result = 0;
			}
		} while (result == 0 && ++i < DB_RETRY);

		if(result == 0 && i == DB_RETRY) {
			Logger.UNCATCH.lerror("执行SQL出错 - " + message + " - " + sql, params);
			throw new Exception("重复执行SQL,依然无法恢复的错误");
		}
	}
	
	protected int executeUpdateAndReturn(String datasource,String sql,Object... params) throws SQLException {
		DataConsole dc = new DataConsole(datasource,c3p0Source,Constants.TRANS_TIMEOUT);
		
		return dc.updatePrepareSQL(sql, params);
	}
	
	/**
	 * 执行批处理更新
	 * @param datasource
	 * @param sql
	 * @param paramList
	 * @return
	 * @throws SQLException
	 */
	protected int[] executeBatch(String datasource,String sql,List<Object[]> paramList) throws SQLException {
		DataConsole dc = new DataConsole(datasource,c3p0Source,Constants.TRANS_TIMEOUT);
		
		return dc.updateBatch(sql, paramList);
	}
}
