package org.sol.util.c3p0;

import java.sql.SQLException;

public class DisconnectException extends SQLException {

	private static final long serialVersionUID = 1L;

	public DisconnectException(String msg) {
		super(msg);
	}
}
