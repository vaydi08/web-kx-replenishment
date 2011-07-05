package com.sol.kx.replenishment.dao;

public class DataSourceHolder {

	public enum DS {
		SMS("ds_sms"),
		SUPPORT_TEMP("ds_support_temp"),
		WEB2G("ds_web2g"),
		MSRV("ds_msrv");
		
		private DS(String ds) {
			this.dataSource = ds;
		}
		public String dataSource;
	}
	
	public enum DSTYPE {
		JOY("joy"),
		XT("xt");
		
		private DSTYPE(String dsType) {
			this.dsType = dsType;
		}
		public String dsType;
	}
	
	private static final ThreadLocal<String> holder = new ThreadLocal<String>();
	
	public static void setDataSource(DS ds,DSTYPE dsType) {
		holder.set(ds.dataSource + "_" + dsType.dsType);
	}
	
	public static String getDataSource() {
		return holder.get();
	}
	
	public static void clear() {
		holder.remove();
	}
}
