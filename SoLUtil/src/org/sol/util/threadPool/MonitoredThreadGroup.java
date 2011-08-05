package org.sol.util.threadPool;

import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 受监控的线程组(单线程池)
 * @author HUYAO
 *
 */
public abstract class MonitoredThreadGroup {
	/**
	 * 单线程池
	 */
	protected SingleThreadPool pool;
//	protected int ThreadCount = 0;
	protected int baseSleep= 0;
	
	/**
	 * 存储当前线程的心跳值，如果当前的值与新取的值一样，说明线程不工作了
	 */
//	protected HashMap<String, String> heart = null;
	protected int heart;
	
	protected static int THREAD_NUMBER = 0;

	private Thread execute;
	private ExecuteDaemon daemon;
	
	private String poolName;
	
	private RunnableTask task;
	
	private static final Log log = LogFactory.getLog(MonitoredThreadGroup.class);
	/**
	 * 登记工作线程
	 * @param c
	 * @param name
	 * @return
	 */
	public synchronized void registerWorkThread() {
		startPool();
		log.debug("注册单线程任务");
		daemon = new ExecuteDaemon(task);
		execute = new Thread(pool.getThreadFactory().getGroup(),daemon);
		execute.setDaemon(true);
		execute.start();
	}
	
	public void destory() {
		daemon.over();
		execute.interrupt();
		pool.shutdownNow();
	}
	
	public String getAllThreadsinfo(){
		return pool.getAllThreadsinfo();
	}
	
	public void getAllThreadsinfo(OutputStream os){
		pool.getAllThreadsinfo(os);
	}
	
	
	/**
	 * 用于发现池内死去的线程。
	 * @return
	 */
	public void checkPool() {
		log.debug("daemon livenumber:" + daemon.getLiveNumber() + " heart:" + heart);
		// 维护线程死锁
		if(daemon.getLiveNumber() == heart) {
			execute.interrupt();
			daemon.over();
			pool.shutdown();
			
			log.debug("发现锁死的线程,已关闭,现在重启线程池");
			pool = null;
			registerWorkThread();
		} else {
			heart = daemon.getLiveNumber();
		}
	}
	
	
	public MonitoredThreadGroup(String poolName,RunnableTask task){
		this.poolName = poolName;
		init();
	}
	
	public MonitoredThreadGroup(String poolName) {
		this.poolName = poolName;
	}
	
	/**
	 * 启动一个新的池
	 */
	public void startPool() {
		log.debug("开始受监控单线程池");
		pool = new SingleThreadPool(poolName);
		heart = -1;
	}
	
	/**
	 * 初始化配置
	 */
	abstract public void init();

	

	
	/**
	 * 派发任务的守护线程
	 * @author HUYAO
	 *
	 */
	private class ExecuteDaemon implements Runnable {
		private RunnableTask r;
		
		private boolean flag;
		
		private int liveNumber;
		
		public ExecuteDaemon(RunnableTask r) {
			this.r = r;
			flag = true;
			
			liveNumber = 0;
			log.debug("单线程任务守护线程已启动 任务:" + r.getClass());
		}
		
		@Override
		public void run() {
			while(flag) {
				pool.execute(r);
			
				try {
					Thread.sleep(baseSleep);
				} catch (InterruptedException e) {
				}
				
				if (liveNumber > 2147483640) {
					liveNumber = -2147483640;
				}
				++liveNumber;
			}
		}
		
		public void over() {
			flag = false;
		}

		public int getLiveNumber() {
			return liveNumber;
		}

	}


	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public RunnableTask getTask() {
		return task;
	}

	public void setTask(RunnableTask task) {
		this.task = task;
	}

	public int getBaseSleep() {
		return baseSleep;
	}

	public void setBaseSleep(int baseSleep) {
		this.baseSleep = baseSleep;
	}
	public int getHeart() {
		return heart;
	}

	public void setHeart(int heart) {
		this.heart = heart;
	}

	public SingleThreadPool getPool() {
		return pool;
	}
}

