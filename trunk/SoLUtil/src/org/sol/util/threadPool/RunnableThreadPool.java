package org.sol.util.threadPool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 可执行任务线程池(固定大小)
 * @author HUYAO
 *
 */
public class RunnableThreadPool extends ThreadPool{

	public RunnableThreadPool(String poolName, int poolSize) {
		super(poolSize, poolSize,0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                poolName);
		
		log.init("初始化 任务线程池 [%s] - 核心线程数:%d",poolName,poolSize);
	}

	
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		((RunnableTask)r).afterExecute(t);
	}
	
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		((RunnableTask)r).beforeExecute(t);
	}
}
