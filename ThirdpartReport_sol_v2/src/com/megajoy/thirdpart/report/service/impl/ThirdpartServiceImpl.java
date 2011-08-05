package com.megajoy.thirdpart.report.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sol.util.textCounter.Counter;
import org.sol.util.textCounter.RandomData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.megajoy.thirdpart.report.common.Constants;
import com.megajoy.thirdpart.report.common.Logger;
import com.megajoy.thirdpart.report.dao.ThirdpartReportDao;
import com.megajoy.thirdpart.report.dao.pojo.ThirdpartReport;
import com.megajoy.thirdpart.report.service.ThirdpartService;
import com.megajoy.thirdpart.report.service.bean.ThirdpartReportBean;
import com.megajoy.thirdpart.report.service.bean.WaitForSendData;

@Service("thirdpartService")
public class ThirdpartServiceImpl extends BaseServiceImpl implements ThirdpartService{
	@Autowired
	private ThirdpartReportDao thirdpartReportDao;
	
	@Value("${groupid}")
	private int groupid;
	
	/* (non-Javadoc)
	 * @see com.megajoy.thirdpart.report.service.impl.ThirdpartService#queryUnsend(com.megajoy.thirdpart.report.common.Constants.SENDSTATE)
	 */
	public Map<Integer,ThirdpartReportBean> queryUnsend() {
		Logger.SCAN.debug("开始检索需要发送的数据");
		return query(Constants.SENDSTATE.READY);
	}
	
	/* (non-Javadoc)
	 * @see com.megajoy.thirdpart.report.service.impl.ThirdpartService#queryResend(com.megajoy.thirdpart.report.common.Constants.SENDSTATE)
	 */
	public Map<Integer,ThirdpartReportBean> queryResend() {
		Logger.SCAN.debug("开始检索需要重发的数据");
		return query(Constants.SENDSTATE.NEEDRESEND);
	}
	
	private Map<Integer,ThirdpartReportBean> query(Constants.SENDSTATE sendstate) {
		// 查询当前groupid的记录
		List<ThirdpartReport> list = null;
		try {
			list = thirdpartReportDao.findThirdpartReport(groupid,sendstate);
		} catch (Exception e) {
			exceptionHandler.onQuery(sendstate.code, e);
		}
		
		if(list == null || list.size() == 0) 
			return null;
		
		Logger.SCAN.debug(sendstate + "检索到数据:" + list.size());
		
		// 建立数据的映射
		Map<Integer,ThirdpartReportBean> map = new HashMap<Integer, ThirdpartReportBean>();
		for(ThirdpartReport po : list) {
			Integer which_serivce = po.getWhich_service();
			ThirdpartReportBean bean = map.get(which_serivce);
			if(bean == null) {
				bean = new ThirdpartReportBean(which_serivce,sendstate);
				map.put(which_serivce, bean);
			}
			
			bean.addThirdpart(po);
		}
		
		return map;
	}
	
	/* (non-Javadoc)
	 * @see com.megajoy.thirdpart.report.service.ThirdpartService#updateSendstate(java.util.List)
	 */
	public void updateSendstate(List<ThirdpartReport> updateList) {
		Logger.DB.ldebug(updateList.get(0).getWhich_service(), "更新Sendstate标记 数量:", updateList.size());
		
		// 获取批量更新 参数列表 <sendstate,thirdpart_id>
		List<Object[]> list = new ArrayList<Object[]>(updateList.size());
		for(ThirdpartReport po : updateList)
			list.add(new Object[]{po.getSendstate(),po.getThirdpart_id()});
		
		try {
			thirdpartReportDao.updateSendState(list);
		} catch (SQLException e) {
			exceptionHandler.onUpdateSendstate(updateList.get(0).getWhich_service(), e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.megajoy.thirdpart.report.service.ThirdpartService#resumeUnsendData()
	 */
	public void resumeUnsendData() {
		Logger.SYS.debug("恢复未成功发送的记录");
		try {
			thirdpartReportDao.resumeUnsendData();
		} catch (SQLException e) {
			exceptionHandler.onResumeUnsendData(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.megajoy.thirdpart.report.service.ThirdpartService#sendHttp(com.megajoy.thirdpart.report.service.bean.ThirdpartReportBean)
	 */
	public List<WaitForSendData> parseData(ThirdpartReportBean bean) {
		Integer which_service = bean.getWhich_service();
		
		// 分拆记录为50条一个包
		List<List<ThirdpartReport>> listAll = takeApart(bean.getThirdpartReportList());
		
		// 循环处理所有包
//		WaitForSendData[] result = new WaitForSendData[listAll.size()];
		List<WaitForSendData> result = new ArrayList<WaitForSendData>(listAll.size());
		int sendcount = configBeanFactory.getConfigBean().get(which_service).getAllCounter().readCount();
		
		for(int i = 0; i < listAll.size(); i ++) {
			// 处理待发数据
			WaitForSendData data = (bean.isReadyToSend()) ? 
						parseData(which_service, listAll.get(i),sendcount) : 
						parseResendData(which_service, listAll.get(i));

			if(data.isNeedSendHttp())  {
				result.add(data);
				sendcount += data.getList().size();
			} else
				Logger.SEND.linfo(which_service, "已超过日流量限制,忽略操作");
			
		}
		
		return result;
	}
	
	// 分拆记录
	private List<List<ThirdpartReport>> takeApart(List<ThirdpartReport> list) {
		List<List<ThirdpartReport>> outlist = new ArrayList<List<ThirdpartReport>>((list.size() / 50) + 1);
		
		// 不足50条直接返回这个包
		if(list.size() <= 50) {
			outlist.add(list);
			return outlist;
		}
		
		// 循环出50条一个的包
		int start = 0;
		int end = 50;
		while(start < end) {
			List<ThirdpartReport> list50 = list.subList(start, end);
			outlist.add(list50);
			start = end;
			end = (end + 50 > list.size()) ? list.size() : end + 50;
		}
		
		return outlist;
	}
	
	// 构成到xml字符串,并设置记录的sendstate标记
	private WaitForSendData parseData(Integer which_service,List<ThirdpartReport> list50,int sendcount) {
		// 计数器
		Counter counter = configBeanFactory.getConfigBean().get(which_service).getSuccessCounter();
		// 扣量数据
		RandomData randomData = configBeanFactory.getConfigBean().get(which_service).getDeductionRandom();
		
		
		// 是否超过当日流量
		if (counter.checkNeedIgnoreThirdpartReport()) {
			Logger.SEND.linfo(which_service, "超量 忽略发送");
			for(ThirdpartReport thirdpartReport : list50)
				thirdpartReport.setSendstate(Constants.SENDSTATE.DAYACCESS.code);
			WaitForSendData data = new WaitForSendData();
			data.setList(list50);
			data.setNeedSendHttp(false);
			data.setHttpResponse(true);
			
			return data;
		}

			
		int bodyCount = 0;
		// xml数据
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<MASP><HEAD><VERSION>1.0</VERSION></HEAD><DATA>");
		
		for (int i = 0; i < list50.size(); i++) {
			ThirdpartReport thirdpartReport = list50.get(i);

			// 如果存在于扣量数组中
			if(randomData.getRandom((sendcount + i) % 100)) {
				thirdpartReport.setSendstate(Constants.SENDSTATE.DEDUCTION.code);
			} else {
				++bodyCount;
				// 正常发送
				xml.append("<REPORT>");
				xml.append("<MOBILE>" + thirdpartReport.getDest_terminal_id() + "</MOBILE>");
				xml.append("<LINKID>" + thirdpartReport.getLinkid() + "</LINKID>");
				xml.append("<CONTENT><![CDATA[" + thirdpartReport.getContent() + "]]></CONTENT>");
				xml.append("<LONGNUMBER>" + thirdpartReport.getSrc_terminal_id() + "</LONGNUMBER>");
				xml.append("<STATUS>1</STATUS>");
				xml.append("</REPORT>");
				thirdpartReport.setSendstate(Constants.SENDSTATE.SUCCESS.code);
			}
		}
		
		xml.append("</DATA></MASP>");
		
		WaitForSendData data = new WaitForSendData();
		data.setReadyForSend(bodyCount);
		data.setDeduction(list50.size() - bodyCount);
		data.setList(list50);
		data.setXml(xml.toString());
		data.setNeedSendHttp(true);
		data.setReadyToSend(true);
		data.setWhich_service(which_service);
		
		return data;
	}
	
	// 构成到xml字符串,并设置记录的sendstate标记 [重发数据]
	private WaitForSendData parseResendData(Integer which_service,List<ThirdpartReport> list50) {

		// xml数据
		StringBuilder xml = new StringBuilder();
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<MASP><HEAD><VERSION>1.0</VERSION></HEAD><DATA>");
		
		for (int i = 0; i < list50.size(); i++) {
			ThirdpartReport thirdpartReport = list50.get(i);

			// 正常发送
			xml.append("<REPORT>");
			xml.append("<MOBILE>" + thirdpartReport.getDest_terminal_id() + "</MOBILE>");
			xml.append("<LINKID>" + thirdpartReport.getLinkid() + "</LINKID>");
			xml.append("<CONTENT><![CDATA[" + thirdpartReport.getContent() + "]]></CONTENT>");
			xml.append("<LONGNUMBER>" + thirdpartReport.getSrc_terminal_id() + "</LONGNUMBER>");
			xml.append("<STATUS>1</STATUS>");
			xml.append("</REPORT>");
			thirdpartReport.setSendstate(Constants.SENDSTATE.SUCCESS.code);
		}
		
		xml.append("</DATA></MASP>");
		
		WaitForSendData data = new WaitForSendData();
		data.setReadyForSend(list50.size());
		data.setDeduction(0);
		data.setList(list50);
		data.setXml(xml.toString());
		data.setNeedSendHttp(true);
		data.setReadyToSend(false);
		data.setWhich_service(which_service);
		
		return data;
	}
	
	
}
