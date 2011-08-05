package com.megajoy.thirdpart.report.common;

public class Constants {
	// 查询超时
	public static final int TRANS_TIMEOUT = 40;
	
	// 一般线程心跳检测时间
	public static final int THREAD_ACTIVE_DELAY = 1;
	
	// 一般线程执行间隔
	public static final int THREAD_RUN_DELAY = 10;
	
	public static final String DB_LOGREPORT = "log_report";
	
	
	public enum SENDSTATE {
		READY(0),
		SUCCESS(1),
		DEDUCTION(2),
		PASS(3),
		DAYACCESS(4),
		FAILED(5),
		NEEDRESEND(6),
		OPENFLAG(7);
				
		public int code;
		
		private SENDSTATE(int code) {
			this.code = code;
		}
	}
}
