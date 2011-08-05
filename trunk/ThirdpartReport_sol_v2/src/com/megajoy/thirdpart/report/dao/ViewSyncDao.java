package com.megajoy.thirdpart.report.dao;

import java.sql.SQLException;

public interface ViewSyncDao {

	/**
	 * 执行同步视图到Thirdpart_Report表
	 * @param which_service
	 * @return
	 * @throws SQLException
	 */
	public int viewSync(int groupid,int which_service) throws SQLException;

}