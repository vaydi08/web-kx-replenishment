package org.sol.util.threadPool;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.sol.util.log.Log;

/**
 * 线程池基类
 * @author HUYAO
 *
 */
public abstract class ThreadPool extends ThreadPoolExecutor{

	/**
	 * 线程池名称
	 */
	protected String poolName;
	
	protected static Log log;
	
	public ThreadPool(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue,String poolName) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, 
				unit, workQueue,new SoLThreadFactory(poolName));
		
		if(log == null)
			log = Log.getLog(getClass());
		
		setPoolName(poolName);
	}
	
	/**
	 * 获取所有核心线程信息
	 * @return
	 */
	public String getAllThreadsinfo() {
		StringBuffer bf = new StringBuffer();
		bf.append("\r\n线程池 : ");
		bf.append(poolName);
		bf.append("\r\n");
		
		
		bf.append(String.format("\t当前线程池大小：%d , 当前任务队列：%d , 已完成任务 %d\r\n",
				getCorePoolSize(), getActiveCount(), getCompletedTaskCount()));

		SoLThreadFactory tf = getThreadFactory();
		ThreadGroup tg = tf.getGroup();
		Thread[] threadList = new Thread[tg.activeCount()];
		tg.enumerate(threadList);
		
		for(Thread t : threadList) {
			if(tg == null || t == null)
				log.error("空值:G-" + tg + "  T-" + t);
			else
				bf.append(String.format("[%s] %s\t \r\n", tg.getName(),t.getName()));
		}
		
		return bf.toString();
	}
	
	/**
	 * 获取两个队列的信息
	 */
	public void getAllThreadsinfo(OutputStream os) {
		String msg = getAllThreadsinfo();
		try {
			os.write(msg.getBytes());
			os.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public SoLThreadFactory getThreadFactory() {
		return (SoLThreadFactory)super.getThreadFactory();
	}
	
	public ThreadGroup getCoreThread() {
		return getThreadFactory().getGroup();
	}
	
	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	/**
	 * 生成核心线程工厂
	 * @author HUYAO
	 *
	 */
	static class SoLThreadFactory implements ThreadFactory {
        static final AtomicInteger poolNumber = new AtomicInteger(1);
        final ThreadGroup group;
        final AtomicInteger threadNumber = new AtomicInteger(1);
        final String namePrefix;

        String poolName;
        
        SoLThreadFactory(String poolName) {

        	int pool = poolNumber.getAndIncrement();
        	
       		group = new ThreadGroup(poolName + "-" + pool + " Group");
            
            this.poolName = poolName;

            namePrefix = poolName + "-" + pool + "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                                  namePrefix + threadNumber.getAndIncrement(),
                                  0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }

		public ThreadGroup getGroup() {
			return group;
		}
    }
}
