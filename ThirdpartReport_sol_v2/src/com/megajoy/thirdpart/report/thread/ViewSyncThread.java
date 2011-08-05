package com.megajoy.thirdpart.report.thread;

import org.sol.util.thread.BaseThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.megajoy.thirdpart.report.common.Constants;
import com.megajoy.thirdpart.report.service.ViewSyncService;

@Controller
public class ViewSyncThread extends BaseThread{

	public ViewSyncThread() {
		super(Constants.THREAD_ACTIVE_DELAY,Constants.THREAD_RUN_DELAY, "View(view_thirdpart) synchronized thread");
	}

	@Autowired
	private ViewSyncService viewSyncService;
	
	@Override
	public void dowhileAction() {
		viewSyncService.viewSync();
	}

}
