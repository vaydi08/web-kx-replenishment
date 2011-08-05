package com.megajoy.thirdpart.report.service;

import java.util.List;
import java.util.Map;

import com.megajoy.thirdpart.report.dao.pojo.ThirdpartReport;
import com.megajoy.thirdpart.report.service.bean.ThirdpartReportBean;
import com.megajoy.thirdpart.report.service.bean.WaitForSendData;

public interface ThirdpartService {

	/**
	 * 扫描未发数据
	 * @return
	 */
	public Map<Integer, ThirdpartReportBean> queryUnsend();

	/**
	 * 扫描重发数据
	 * @return
	 */
	public Map<Integer, ThirdpartReportBean> queryResend();

	/**
	 * 解析需要发送的HTTP数据
	 * @param bean
	 * @return
	 */
	public List<WaitForSendData> parseData(ThirdpartReportBean bean);
	
	/**
	 * 更新Sendstate标记
	 * @param updateList
	 */
	public void updateSendstate(List<ThirdpartReport> updateList);
	
	/**
	 * 更新未成功发送的数据标志
	 */
	public void resumeUnsendData();
}