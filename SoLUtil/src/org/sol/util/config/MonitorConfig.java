package org.sol.util.config;

public class MonitorConfig {
	public static String MONITOR_HOST;
	
	public static int MONITOR_PORT;

	public static String getMONITOR_HOST() {
		return MONITOR_HOST;
	}

	public void setMONITOR_HOST(String mONITORHOST) {
		MONITOR_HOST = mONITORHOST;
	}

	public static int getMONITOR_PORT() {
		return MONITOR_PORT;
	}

	public void setMONITOR_PORT(int mONITORPORT) {
		MONITOR_PORT = mONITORPORT;
	}
}
