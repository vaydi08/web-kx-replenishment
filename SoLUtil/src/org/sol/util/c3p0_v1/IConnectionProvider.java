package org.sol.util.c3p0_v1;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionProvider {
	public Connection getConnection(String sourceName) throws SQLException;
}
