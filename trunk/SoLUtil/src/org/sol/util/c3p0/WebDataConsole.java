package org.sol.util.c3p0;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class WebDataConsole extends DataConsole{

	public WebDataConsole(String sourceName, int queryTime) {
		super(sourceName, null, queryTime);
	}

	@Override
	public Connection getConnection() throws SQLException {
		if(sourceName == null)
			throw new SQLException("没有设置数据源 运行setSourceName(String)");
		
		int Times = 0;
		while (connection == null || connection.isClosed()) {
			try {
				closeConnection();
				connection = getDataSource().getConnection();
			} catch (Exception sqle) {
				log.error("error getConnection():" + sqle.getMessage(),sqle);
			} catch (Throwable ae) {
				log.error("error getConnection():" + ae.getMessage(),ae);
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
	
	private DataSource getDataSource() throws NamingException {
		Context initCtx = new InitialContext();
	    Context envCtx =(Context)initCtx.lookup("java:comp/env");

	    return (DataSource)envCtx.lookup(sourceName);
	}
}
