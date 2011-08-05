package org.sol.util.thread;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sol.util.threadMonitor.ActiveTestClient;

/**
 * 通用受监控线程 基类
 * 连接到线程监视器
 * @author HUYAO
 *
 */
public abstract class BaseThreadWithNodelay implements Runnable {
	// 控制筏门
	protected int Swich = 1; 
	
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
	public BaseThreadWithNodelay(int baseSleep,int sleepTime,
			String threadName,String threadClassname,String threadGroup,
			String threadMonitorHost,int threadMonitorPort,boolean debug) {
		log = LogFactory.getLog(getClass());
		activeTest = new ActiveTestClient(threadMonitorHost,threadMonitorPort,debug);
		
		this.threadName = threadName;
		this.threadClassname = threadClassname;
		this.threadGroup = threadGroup;
		
		this.lastRunTimestamp = System.currentTimeMillis();
		
		Swich = 2;
	}
	
	public BaseThreadWithNodelay(String threadName) {
		log = LogFactory.getLog(getClass());
		
		this.threadName = threadName;
		
		this.heart = Long.MIN_VALUE;
		
		this.lastRunTimestamp = System.currentTimeMillis();
		
		Swich = 2;
	}
	
	public void run() {

		while (Swich == 2) {
			// 连接线程监视器
//			tryConnectMonitor();
			
			try {
				beforwork();
				dowhileAction();
				afterwork();
			} catch (Exception e) {
				log.error("BaseThread Exception BIGERROR:" + e + "\n",e);
			} catch (Throwable e1) {
				log.error("BaseThread Throwable BIGERROR:" + e1 + "\n",e1);
			}
			
//			yield();
			
			// 已小间隔发送心跳包,达到设定长间隔后执行下一轮操作
			heart();

		}
		
//		System.out.println("NDT线程终止");
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
	
	protected void heart() {
		lastRunTimestamp = System.currentTimeMillis();

		heart = (heart == Long.MAX_VALUE - 1) ? Long.MIN_VALUE : heart + 1;
	}

	public String threadStatus() {
		return String.format("%s[ND]\t%s %d",
			threadName,df.format(new Date(lastRunTimestamp)),heart);
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
	
	public int getSwich() {
		return Swich;
	}

}
