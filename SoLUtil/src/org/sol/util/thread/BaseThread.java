package org.sol.util.thread;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PreDestroy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sol.util.log.Logger;
import org.sol.util.threadMonitor.ActiveTestClient;

/**
 * 通用受监控线程 基类
 * 连接到线程监视器
 * @author HUYAO
 *
 */
public abstract class BaseThread implements Runnable {
	// 控制筏门
	protected int Swich = 1; 

	// 基础休息时间
	protected int baseSleep;
	
	// 线程睡眠时间
	protected int sleepTime;
	
	// 上一次线程时间时间
	private long lastRunTimestamp;
	
	// 心跳包客户连接
	protected ActiveTestClient activeTest;
	
	protected long heart;
		
	// 日志
	protected Log log;
	

	// 线程的名称
	protected String threadName;
	// 线程类的名称
	private String threadClassname;
	// 线程组的名称
	private String threadGroup;

	private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");
	
	/**
	 * 基本线程
	 * @param baseSleep 基本轮询时间 - 以此间隔发送心跳包 单位秒
	 * @param sleepTime 线程执行间隔时间 单位秒
	 * @param threadName 线程名
	 * @param threadClassname 线程类名
	 * @param threadGroup 线程组
	 * @param threadMonitorHost 线程监视器地址
	 * @param threadMonitorPort 线程监视器端口
	 */
	public BaseThread(int baseSleep,int sleepTime,
			String threadName,String threadClassname,String threadGroup,
			String threadMonitorHost,int threadMonitorPort,boolean debug) {
		log = LogFactory.getLog(getClass());
		activeTest = new ActiveTestClient(threadMonitorHost,threadMonitorPort,debug);
		
		this.baseSleep = baseSleep * 1000;
		this.sleepTime = sleepTime * 1000;
		this.threadName = threadName;
		this.threadClassname = threadClassname;
		this.threadGroup = threadGroup;
		
		this.lastRunTimestamp = System.currentTimeMillis();
		
		Swich = 2;
	}
	
	public BaseThread(int baseSleep,int sleepTime,String threadName) {
		log = LogFactory.getLog(getClass());
		
		this.baseSleep = baseSleep * 1000;
		this.sleepTime = sleepTime * 1000;
		this.threadName = threadName;
		
		this.heart = Long.MIN_VALUE;
		
		this.lastRunTimestamp = System.currentTimeMillis();
		
		Swich = 2;
	}
	
	public void run() {

		while (Swich == 2) {
			// 连接线程监视器
			tryConnectMonitor();
			
			try {
				beforwork();
				dowhileAction();
				afterwork();
			} catch (Exception e) {
				log.error("BaseThread Exception BIGERROR:" + e + "\n",e);
			} catch (Throwable e1) {
				log.error("BaseThread Throwable BIGERROR:" + e1 + "\n",e1);
			}
			
			yield();
			
			// 已小间隔发送心跳包,达到设定长间隔后执行下一轮操作
			while(checkContinueSleep() && Swich == 2) {
				// sleep同时,发送心跳包
				sleep(baseSleep);
			}

		}
		
		Logger.SYS.info("延迟类线程:" + threadName + " 已退出");
		Swich = 3;
	}
	
	private void tryConnectMonitor() {
		try {
			if(activeTest != null)
				activeTest.tryConnect(threadClassname,threadName,threadGroup);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected boolean checkContinueSleep() {
		Long nowUnixTimeStamp = System.currentTimeMillis();
		if ((nowUnixTimeStamp - lastRunTimestamp) >= sleepTime) {
			// 需要下一轮执行
			lastRunTimestamp = nowUnixTimeStamp;
			return false;
		}
		return true;
	}

	protected void sleep(int i) {
		try {
			Thread.sleep(i);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if(activeTest != null)
			activeTest.activeTest();
		else
			heart = (heart == Long.MAX_VALUE - 1) ? Long.MIN_VALUE : heart + 1;
		
	}
	
	public String threadStatus() {
		return String.format("%s\t%s %s",
			threadName,df.format(new Date(lastRunTimestamp)),
			((lastRunTimestamp - System.currentTimeMillis()) > sleepTime * 10) ? "Death" : "Live"
		);
	}
	
	@PreDestroy
	public void close() {
		Logger.SYS.info("尝试关闭处理线程 " + threadName);
		stop();
	}
	
	abstract public void dowhileAction();

	public void beforwork(){}

	public void afterwork(){}

	public boolean isover() {
		return Swich == 3;
	}

	public void stop() {
		Swich = 1;
	}

	public void over() {
		Swich = 3;
	}

	public boolean isstop() {
		if (Swich == 1)
			return true;
		return false;
	}
	
	public boolean isStarted() {
		return Swich == 2;
	}

	protected void yield() {
		Thread.yield();
	}

	public int getBaseSleep() {
		return baseSleep;
	}

	public void setBaseSleep(int baseSleep) {
		this.baseSleep = baseSleep;
	}

	public void finalize() {
		try {
			super.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public String getThreadName() {
		return threadName;
	}

	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}

	public String getThreadClassname() {
		return threadClassname;
	}

	public void setThreadClassname(String threadClassname) {
		this.threadClassname = threadClassname;
	}

	public String getThreadGroup() {
		return threadGroup;
	}

	public void setThreadGroup(String threadGroup) {
		this.threadGroup = threadGroup;
	}

	public long getLastRunTimestamp() {
		return lastRunTimestamp;
	}

	public void setLastRunTimestamp(long lastRunTimestamp) {
		this.lastRunTimestamp = lastRunTimestamp;
	}

	public long getHeart() {
		return heart;
	}

	public void setHeart(long heart) {
		this.heart = heart;
	}

}
