package org.sol.util.c3p0_v1;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * TOMCAT JNDI连接池获取代理
 * @author SOL
 *
 */
public class TomcatProvider implements IConnectionProvider {
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