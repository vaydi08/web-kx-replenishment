package com.megajoy.thirdpart.report.thread;

import org.sol.util.common.StringUtil;
import org.sol.util.log.Logger;
import org.sol.util.thread.BaseThreadWithNodelay;
import org.springframework.beans.factory.annotation.Autowired;

import com.megajoy.thirdpart.report.common.Util;
import com.megajoy.thirdpart.report.service.ThirdpartService;
import com.megajoy.thirdpart.report.service.queue.QueueManager;

public abstract class NoDelayThread extends BaseThreadWithNodelay{

	@Autowired
	protected QueueManager queueManager;
	
	@Autowired
	protected ThirdpartService thirdpartService;
	
	private boolean inwait;
	private long inrun_timestamp;
	
	public NoDelayThread(String threadName) {
		super(threadName);
		
		inrun_timestamp = 0;
	}

	@Override
	public void dowhileAction() {
		try {
			inwait = true;
			runAction();
		} catch (InterruptedException e) {
			Logger.SYS.info(threadName + " 线程已中断");
			stop();
		}
	}
	

	@Override
	public String threadStatus() {
		StringBuilder sb = new StringBuilder();
		sb.append(threadName).append("\t");
		if(inrun_timestamp > 0)
			sb.append("wait in ").append(System.currentTimeMillis() - inrun_timestamp);
		sb.append(StringUtil.newLine()).append("\twait:").append(inwait).
			append("\tlast_run:").append(Util.parseDate(inrun_timestamp));
		return sb.toString();
	}
	
	protected void gorun() {
		inwait = false;
		inrun_timestamp = System.currentTimeMillis();
	}
	
	public boolean isWait() {
		return inwait;
	}
	
	protected abstract void runAction() throws InterruptedException;
}
