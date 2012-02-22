package org.sol.util.c3p0_v1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 标准JDBC连接获取代理
 * @author HUYAO
 *
 */
public class JdbcProvider implements IConnectionProvider {
	private String DBDriver;
	private String DBUrl;
	private String username;
	private String password;
	
	public JdbcProvider(String DBDriver,String DBUrl,String username,String password){
		this.DBDriver = DBDriver;
		this.DBUrl = DBUrl;
		this.username = username;
		this.password = password;
		
		try {
			Class.forName(DBDriver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Connection getConnection(String sourceName) throws SQLException {
		return DriverManager.getConnection(DBUrl + ";DatabaseName=" + sourceName,username,password);
	}

	public String getDBUrl() {
		return DBUrl;
	}

	public void setDBUrl(String dBUrl) {
		DBUrl = dBUrl;
	}

	public String getDBDriver() {
		return DBDriver;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
}