package com.megajoy.thirdpart.report.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.sol.util.c3p0.DataConsoleUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.megajoy.thirdpart.report.common.Constants;
import com.megajoy.thirdpart.report.dao.ThirdpartReportDao;
import com.megajoy.thirdpart.report.dao.pojo.ThirdpartReport;

@Repository
public class ThirdpartReportDaoImpl extends BaseDaoImpl implements ThirdpartReportDao{
	@Value("${sql.thirdpart_report.find}")
	private String SQL_FIND;
	
	/* (non-Javadoc)
	 * @see com.megajoy.thirdpart.report.dao.impl.ThirdpartReportDao#findThirdpartReport(int)
	 */
	public List<ThirdpartReport> findThirdpartReport(int groupid,Constants.SENDSTATE sendstate) throws Exception {
		return super.call(Constants.DB_LOGREPORT, ThirdpartReport.class, SQL_FIND, 
				DataConsoleUtil.getClassDefine(ThirdpartReport.class,"sendstate"),groupid, sendstate.code);
	}
	
	@Value("${sql.thirdpart_report.updateSendState}")
	private String SQL_UPDATESENDSTATE;
	
	/* (non-Javadoc)
	 * @see com.megajoy.thirdpart.report.dao.impl.ThirdpartReportDao#updateSendState(java.util.List)
	 */
	public int[] updateSendState(List<Object[]> updateList) throws SQLException {
		return super.executeBatch(Constants.DB_LOGREPORT, SQL_UPDATESENDSTATE, updateList);
	}

	@Value("${sql.thirdpart_report.resumeUnsendData}")
	private String SQL_RESUME;
	
	public void resumeUnsendData() throws SQLException {
		super.executeUpdateAndReturn(Constants.DB_LOGREPORT, SQL_RESUME);
	}
} 
