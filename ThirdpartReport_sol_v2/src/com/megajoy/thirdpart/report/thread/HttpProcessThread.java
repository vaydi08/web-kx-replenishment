package com.megajoy.thirdpart.report.thread;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.megajoy.thirdpart.report.dao.pojo.ThirdpartReport;
import com.megajoy.thirdpart.report.service.bean.WaitForSendData;
import com.megajoy.thirdpart.report.service.impl.HttpExecutorService;

@Controller
@Scope("prototype")
public class HttpProcessThread extends NoDelayThread{

	private static int serial = 1;
	
	public HttpProcessThread() {
		super("HTTP Data queue process thread -" + serial++);
	}

	@Autowired
	private HttpExecutorService httpExecutorService;
	
	@Override
	protected void runAction() throws InterruptedException {
		WaitForSendData data = queueManager.taskHttp();
		
		gorun();
		
		List<ThirdpartReport> list = httpExecutorService.takeHttpTask(data);
		if(list != null)
			queueManager.addUpdate(list);
	}

}
