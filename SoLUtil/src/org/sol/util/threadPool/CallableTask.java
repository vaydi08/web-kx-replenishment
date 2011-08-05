package org.sol.util.threadPool;

import java.util.concurrent.Callable;

/**
 * 回调用任务接口
 * @author HUYAO
 *
 * @param <V>
 */
public interface CallableTask<V> extends Callable<V>{
	public void beforeExecute(Thread t);
	
	public void afterExecute(Throwable t);
}
