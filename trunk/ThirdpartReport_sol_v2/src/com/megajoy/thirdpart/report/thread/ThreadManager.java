package com.megajoy.thirdpart.report.thread;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.sol.util.common.StringUtil;
import org.sol.util.thread.BaseThread;
import org.sol.util.thread.BaseThreadWithNodelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import com.megajoy.thirdpart.report.service.ViewSyncService;
import com.megajoy.thirdpart.report.service.queue.QueueManager;


@Controller
public class ThreadManager {
	private ThreadGroup delayThreadGroup;
	private ThreadGroup noDelayThreadGroup;
	
	private Set<BaseThread> delayThreadSet;
	private Set<NoDelayThread> noDelayThreadSet;
	
	public ThreadManager() {
		this.delayThreadGroup = new ThreadGroup("执行线程池");
		this.noDelayThreadGroup = new ThreadGroup("队列处理线程池");
		
		this.delayThreadSet = new HashSet<BaseThread>();
		this.noDelayThreadSet = new LinkedHashSet<NoDelayThread>();
	}
		
	@Autowired
	private ApplicationContext ctx;
	
	@Autowired
	private ViewSyncService viewSyncService;
	
	@Autowired
	private QueueManager queueManager;
	
	@Value("${queueThreads}")
	private int queueThreads;
	
	public void initService() {
		viewSyncService.init();
	}
	public void startAllThreads() {
		// View
		startDelayThread(ViewSyncThread.class);

		// 扫描 thridpartreport
		startDelayThread(QueryUnsendThread.class);
		
		// 发送处理
		for(int i = 0 ; i < queueThreads; i ++) 
			startNoDelayThread(WorkProcessThread.class);
		
		// HTTP处理
		for(int i = 0 ; i < queueThreads; i ++) 
			startNoDelayThread(HttpProcessThread.class);
		
		
		// 更新Sendstate标记
		startNoDelayThread(UpdateProcessThread.class);
		
		// 恢复未成功发送的数据标志
		startDelayThread(ResumeUnsendThread.class);
		
		// 扫描 重发
//		startDelayThread(QueryResendThread.class);
	}
	
	private void startDelayThread(Class<? extends BaseThread> runClass) {
		BaseThread runBase = ctx.getBean(runClass);
		Thread thread = new Thread(delayThreadGroup, runBase,runBase.getThreadName());
		thread.start();
		
		delayThreadSet.add(runBase);
	}
	
	private void startNoDelayThread(Class<? extends NoDelayThread> runClass) {
		NoDelayThread runBase = ctx.getBean(runClass);
		Thread thread = new Thread(noDelayThreadGroup, runBase,runBase.getThreadName());
		thread.start();
		
		noDelayThreadSet.add(runBase);
	}
	
	public void serializeQueue() {
		queueManager.serialize();
	}
	
	public String queueStatus() {
		return queueManager.queueStatus();
	}
	
	public String threadsStatus() {
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtil.newLine());
		sb.append("base delay thread status:").append(StringUtil.newLine());
		for(BaseThread gbt : delayThreadSet) {
			sb.append(gbt.threadStatus()).append(StringUtil.newLine());
		}
		sb.append(StringUtil.newLine());
		
		sb.append("queue process thread status:").append(StringUtil.newLine());
		for(BaseThreadWithNodelay ndt : noDelayThreadSet) {
			sb.append(ndt.threadStatus()).append(StringUtil.newLine());
		}
		sb.append(StringUtil.newLine());
		
		return sb.toString();
	}

	public ThreadGroup getDelayThreadGroup() {
		return delayThreadGroup;
	}

	public ThreadGroup getNoDelayThreadGroup() {
		return noDelayThreadGroup;
	}

	public Set<BaseThread> getDelayThreadSet() {
		return delayThreadSet;
	}

	public Set<NoDelayThread> getNoDelayThreadSet() {
		return noDelayThreadSet;
	}

}
