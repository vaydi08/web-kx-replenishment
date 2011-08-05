package org.sol.util.threadPool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 任务线程池(单线程)
 * @author HUYAO
 *
 */
public class SingleThreadPool extends ThreadPool{

	public SingleThreadPool(String poolName) {
		super(1, 1,0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(),poolName);
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		((RunnableTask)r).beforeExecute(t);
	}
	
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		((RunnableTask)r).afterExecute(t);
	}
}
