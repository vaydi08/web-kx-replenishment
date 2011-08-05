package com.megajoy.thirdpart.report.service.impl;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.megajoy.thirdpart.report.common.Logger;
import com.megajoy.thirdpart.report.dao.ViewSyncDao;
import com.megajoy.thirdpart.report.service.ViewSyncService;
import com.megajoy.thirdpart.report.service.bean.ConfigBean;
import com.megajoy.thirdpart.report.service.bean.ConfigBeanFactory;

@Service("viewSyncService")
public class ViewSyncServiceImpl extends BaseServiceImpl implements ViewSyncService{
	@Autowired
	private ConfigBeanFactory configBeanFactory;
	
	@Autowired
	private ViewSyncDao viewSyncDao;
	
	private int[] which_services;
	
	@Value("${groupid}")
	private int groupid;
	
	public void init() {
		Map<Integer, ConfigBean> map = configBeanFactory.getConfigBean();
		which_services = new int[map.size()];
		Set<Entry<Integer, ConfigBean>> set = map.entrySet();
		
		int i = 0;
		for(Entry<Integer, ConfigBean> en : set) 
			which_services[i ++] = en.getKey();
	}
	
	/* (non-Javadoc)
	 * @see com.megajoy.thirdpart.report.service.impl.ViewSyncService#viewSync()
	 */
	public void viewSync() {
		for(int which_service : which_services) {
			try {
				Logger.SCAN.ldebug(which_service, "扫描View_thirdpart");
				int result = viewSyncDao.viewSync(groupid,which_service);
				if(result > 0)
					Logger.SCAN.linfo(which_service, "扫描 View_thirdpart 到 Thirdpart_report,数量:", result);
			} catch (SQLException e) {
				exceptionHandler.onViewSync(which_service, e);
			}
		}
	}
}
