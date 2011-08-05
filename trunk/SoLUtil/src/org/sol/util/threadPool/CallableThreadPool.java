package org.sol.util.threadPool;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 回调线程池
 * @author HUYAO
 *
 * @param <T>
 */
public class CallableThreadPool<T> extends RunnableThreadPool {

	public CallableThreadPool(String poolName, int poolSize) {
		super(poolName, poolSize);
		
		log.init("初始化 回调线程池 [%s] - 核心线程数:%d",poolName,poolSize);
	}

	public FutureTask<T> execute(Callable<T> c) {
		FutureTask<T> ft = new FutureTask<T>(c);
		execute(ft);
		return ft;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		if(r instanceof CallableTask)
			((CallableTask<T>)r).afterExecute(t);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		if(r instanceof CallableTask)
			((CallableTask<T>)r).beforeExecute(t);
	}
}
