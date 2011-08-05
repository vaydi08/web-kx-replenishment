package org.sol.util.threadPool;

/**
 * 可执行任务接口
 * @author HUYAO
 *
 */
public interface RunnableTask extends Runnable{
	public void beforeExecute(Thread t);
	
	public void afterExecute(Throwable t);
	
}
