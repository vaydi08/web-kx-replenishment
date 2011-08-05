package com.megajoy.thirdpart.report.thread;

import java.util.List;

import org.springframework.stereotype.Controller;

import com.megajoy.thirdpart.report.dao.pojo.ThirdpartReport;

@Controller
public class UpdateProcessThread extends NoDelayThread{

	public UpdateProcessThread() {
		super("Update work success(sendstate) thread");
	}

	@Override
	protected void runAction() throws InterruptedException {
		List<ThirdpartReport> list = queueManager.takeUpdate();
		
		gorun();
		
		thirdpartService.updateSendstate(list);
	}

}
