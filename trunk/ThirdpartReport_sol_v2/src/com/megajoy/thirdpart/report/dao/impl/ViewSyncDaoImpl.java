package com.megajoy.thirdpart.report.dao.impl;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.megajoy.thirdpart.report.common.Constants;
import com.megajoy.thirdpart.report.dao.ViewSyncDao;

@Repository
public class ViewSyncDaoImpl extends BaseDaoImpl implements ViewSyncDao{
	@Value("${sql.view.sync}")
	private String SQL_VIEWSYNC;
	
	/* (non-Javadoc)
	 * @see com.megajoy.thirdpart.report.dao.impl.ViewSyncDao#viewSync(int)
	 */
	public int viewSync(int groupid,int which_service) throws SQLException {
		return super.executeUpdateAndReturn(Constants.DB_LOGREPORT, SQL_VIEWSYNC,groupid, which_service);
	}
}
