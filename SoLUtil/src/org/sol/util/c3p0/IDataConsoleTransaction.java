package org.sol.util.c3p0;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDataConsoleTransaction {
	public void run(Connection connection) throws SQLException;
}
