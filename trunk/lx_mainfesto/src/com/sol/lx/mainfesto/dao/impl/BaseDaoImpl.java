package com.sol.lx.mainfesto.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;

import org.sol.util.c3p0.dataEntity2.CountEntity;
import org.sol.util.c3p0.dataEntity2.DeleteEntity;
import org.sol.util.c3p0.dataEntity2.InsertEntity;
import org.sol.util.c3p0.dataEntity2.SelectEntity;
import org.sol.util.c3p0.dataEntity2.UpdateEntity;
import org.sol.util.c3p0_v1.DataConsole;
import org.sol.util.c3p0_v1.IConnectionProvider;
import org.sol.util.c3p0_v1.TomcatProvider;
import org.sol.util.log.Logger;
import org.springframework.beans.factory.annotation.Value;


import com.sol.lx.mainfesto.common.Constants;
import com.sol.lx.mainfesto.dao.BaseDao;



/**
 * 数据操作基类
 * @author HUYAO
 *
 */
public abstract class BaseDaoImpl implements BaseDao{
	// 数据源
	private static final IConnectionProvider connectionProvider = new TomcatProvider();
	protected DataConsole dataConsole;
	
	// 数据库操作重试次数
	@Value("${db.retry}")
	protected int DB_RETRY;
	
	@PostConstruct
	public void init() {
		dataConsole = new DataConsole(connectionProvider,Constants.DB, Constants.TRANS_TIMEOUT,
				ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
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
				 result = dataConsole.updatePrepareSQL(sql, params);
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

	
	/**
	 * DAO公用函数
	 */
	public List find(SelectEntity entity) throws Exception {		
		return dataConsole.find(entity.getFullSql(), entity.getClazz(), entity.getSmap(), entity.getCriteria().getParamList());
	}
	public List findByPage(SelectEntity entity,int page,int pageSize,String order) throws Exception {
		StringBuilder sb = new StringBuilder();

		sb.append("select * from(select top ").append(pageSize).append(" * From (");
		sb.append("select top ").append(page * pageSize).append(entity.getFullSql().substring(6)).append(") find1 order by " + order + " asc) find2 order by " + order + " desc");

		return dataConsole.find(sb.toString(), entity.getClazz(), entity.getSmap(), entity.getCriteria().getParamList());
	}
	public int findCount(CountEntity entity) throws Exception {
		return (Integer)dataConsole.findReturn(entity.getFullSql(), Types.INTEGER, entity.getCriteria().getParamList());
	}
	public void insert(InsertEntity entity) throws SQLException {
		dataConsole.updatePrepareSQL(entity.getFullSql(), entity.getCriteria().getParamListWithoutId());
	}
	public void update(UpdateEntity entity) throws SQLException {
		dataConsole.updatePrepareSQL(entity.getFullSql(), entity.getCriteria().getParamList());
	}
	public void delete(DeleteEntity entity) throws SQLException {
		List<Object> list = new ArrayList<Object>(1);
		list.add(entity.getCriteria().getId());
		dataConsole.updatePrepareSQL(entity.getFullSql(), list);
	}
}
