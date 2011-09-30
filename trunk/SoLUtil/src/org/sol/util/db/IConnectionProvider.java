package org.sol.util.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 用于获取连接的接口
 * @author SOL
 *
 */
public interface IConnectionProvider {
	public Connection getConnection(String sourceName) throws SQLException;
}
