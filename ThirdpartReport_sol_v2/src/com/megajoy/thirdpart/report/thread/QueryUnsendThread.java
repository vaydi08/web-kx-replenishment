package com.megajoy.thirdpart.report.thread;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.sol.util.thread.BaseThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.megajoy.thirdpart.report.common.Constants;
import com.megajoy.thirdpart.report.service.ThirdpartService;
import com.megajoy.thirdpart.report.service.bean.ThirdpartReportBean;
import com.megajoy.thirdpart.report.service.queue.QueueManager;

@Controller
public class QueryUnsendThread extends BaseThread{

	public QueryUnsendThread() {
		super(Constants.THREAD_ACTIVE_DELAY,Constants.THREAD_RUN_DELAY, "Scan working(thirdpart_report) thread");
	}

	@Autowired
	private ThirdpartService thirdpartService;
	
	@Autowired
	private QueueManager queueManager;
	
	@Override
	public void dowhileAction() {
		// 正常发送
		Map<Integer, ThirdpartReportBean> map = thirdpartService.queryUnsend();
		
		if(map == null)
			return;

		Set<Entry<Integer, ThirdpartReportBean>> set = map.entrySet();
		for(Entry<Integer, ThirdpartReportBean> en : set) 
			queueManager.addWork(en.getValue());
		
		
		// 重发
		map = thirdpartService.queryResend();
		
		if(map == null)
			return;

		set = map.entrySet();
		for(Entry<Integer, ThirdpartReportBean> en : set) 
			queueManager.addWork(en.getValue());
		
	}

}
