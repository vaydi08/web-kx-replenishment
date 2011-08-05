package com.megajoy.thirdpart.report.thread;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.sol.util.thread.BaseThread;
import org.springframework.beans.factory.annotation.Autowired;

import com.megajoy.thirdpart.report.common.Constants;
import com.megajoy.thirdpart.report.service.ThirdpartService;
import com.megajoy.thirdpart.report.service.bean.ThirdpartReportBean;
import com.megajoy.thirdpart.report.service.queue.QueueManager;

//@Controller
@Deprecated
public class QueryResendThread extends BaseThread{

	public QueryResendThread() {
		super(Constants.THREAD_ACTIVE_DELAY,Constants.THREAD_RUN_DELAY, "扫描重发数据 工作线程");
	}

	@Autowired
	private ThirdpartService thirdpartService;
	
	@Autowired
	private QueueManager queueManager;
	
	@Override
	public void dowhileAction() {
		Map<Integer, ThirdpartReportBean> map = thirdpartService.queryResend();
		
		if(map == null)
			return;

		Set<Entry<Integer, ThirdpartReportBean>> set = map.entrySet();
		for(Entry<Integer, ThirdpartReportBean> en : set) 
			queueManager.addWork(en.getValue());
		
	}

}
