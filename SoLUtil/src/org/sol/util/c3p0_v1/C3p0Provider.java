package org.sol.util.c3p0_v1;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * C3P0的连接池获取代理
 * @author SOL
 *
 */
public class C3p0Provider implements IConnectionProvider{
	private C3p0Source ce; 
	public C3p0Provider(C3p0Source ce) {
		this.ce = ce;
	}
	
	@Override
	public Connection getConnection(String sourceName) throws SQLException {
		return ce.getConnection(sourceName);
	}
}
