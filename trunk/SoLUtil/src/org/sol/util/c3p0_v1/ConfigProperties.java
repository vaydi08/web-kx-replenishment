package org.sol.util.c3p0_v1;

public class ConfigProperties {

	private ConfigProperties() {
	}

	public static DynamicProperties console = new DynamicProperties(
			"/console.properties");

	public static String TransactionId = "JDBC_TRANSACTION";

	final public static String CONTENT_ENCODING = "UTF-8";

}
