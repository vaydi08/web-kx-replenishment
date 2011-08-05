package com.megajoy.thirdpart.report.dao;

import java.sql.SQLException;
import java.util.List;

import com.megajoy.thirdpart.report.common.Constants;
import com.megajoy.thirdpart.report.dao.pojo.ThirdpartReport;

public interface ThirdpartReportDao {

	/**
	 * 查找待发记录
	 * @param which_service
	 * @return
	 * @throws Exception
	 */
	public List<ThirdpartReport> findThirdpartReport(int groupid,Constants.SENDSTATE sendstate)
			throws Exception;
	
	/**
	 * 更新发送状态
	 * @param updateList
	 * @return
	 * @throws SQLException
	 */
	public int[] updateSendState(List<Object[]> updateList) throws SQLException;

	/**
	 * 恢复发送失败的记录
	 * @throws SQLException
	 */
	public void resumeUnsendData() throws SQLException;
}