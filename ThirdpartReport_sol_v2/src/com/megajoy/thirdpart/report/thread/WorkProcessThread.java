package com.megajoy.thirdpart.report.thread;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.megajoy.thirdpart.report.service.bean.ThirdpartReportBean;
import com.megajoy.thirdpart.report.service.bean.WaitForSendData;

@Controller
@Scope("prototype")
public class WorkProcessThread extends NoDelayThread{

	private static int serial = 1;
	
	public WorkProcessThread() {
		super("Data process thread -" + serial++);
	}

	@Override
	protected void runAction() throws InterruptedException {
		ThirdpartReportBean bean = queueManager.taskWork();
		
		gorun();
		
		for(WaitForSendData data : thirdpartService.parseData(bean))
			queueManager.addHttp(data);
	}

}
