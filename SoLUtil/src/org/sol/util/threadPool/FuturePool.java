package org.sol.util.threadPool;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.sol.util.log.Log;


/**
 * 带有执行超时的任务池
 * 使用一个Runnable线程池以及一个Callable线程池
 * @author HUYAO
 *
 * @param <T>
 */
public abstract class FuturePool<T> {

	/**
	 * Runnable线程池 维护线程
	 */
	private RunnableThreadPool runner;
	/**
	 * Callable线程池 实际处理线程
	 */
	private CallableThreadPool<T> caller;
	/**
	 * 操作处理句柄
	 */
	private IFutureHandler<T> handlerClass;
	/**
	 * 超时设置
	 */
	private int handler_timeout;
	
	protected Log log;
	
	/**
	 * 初始化两个线程池,注册处理句柄
	 * @param poolName 线程池名
	 * @param poolSize 池大小
	 * @param handlerClass 回调处理类
	 * @param timeout 超时 单位秒
	 */
	public FuturePool(String poolName, int poolSize,IFutureHandler<T> handlerClass,int timeout) {
		if(log == null)
			log = Log.getLog(getClass());
		
		initPool(poolName, poolSize, handlerClass, timeout);
		
		log.init("初始化 超时回调线程池 [%s] - 核心线程:%d|回调处理类:%s|超时:%d",poolName,poolSize,handlerClass.toString(), timeout);
	}
	
	/**
	 * 不回调处理,只使用超时设置
	 * @param poolName 线程池名
	 * @param poolSize 池大小
	 * @param timeout 超时 单位秒
	 */
	public FuturePool(String poolName,int poolSize,int timeout) {
		initPool(poolName, poolSize, null, timeout);
	}
	
	public FuturePool() {
		
	}
	
	/**
	 * 注册池参数
	 * @param poolName 线程池名
	 * @param poolSize 池大小
	 * @param handlerClass 回调处理类(null 不适用回调)
	 * @param timeout 超时 单位秒
	 */
	public void initPool(String poolName,int poolSize,IFutureHandler<T> handlerClass,int timeout) {
		runner = new RunnableThreadPool(poolName + "Runner", poolSize);
		caller = new CallableThreadPool<T>(poolName + "Caller", poolSize);
		this.handler_timeout = timeout;
		
		if(handlerClass == null) return;
					
		this.handlerClass = handlerClass;
	}
	
	/**
	 * 运行任务
	 * @param call
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public void runCallTask(Callable<T> call) throws InstantiationException, IllegalAccessException {
		runner.execute(new FutureDoCall(call));
	}

	/**
	 * 获取两个队列的信息
	 */
	public String getAllThreadsinfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n执行队列信息:\r\n");
		sb.append(runner.getAllThreadsinfo());
		sb.append("延迟队列信息:\r\n");
		sb.append(caller.getAllThreadsinfo());
		
		return sb.toString();
	}
	
	/**
	 * 获取两个队列的信息
	 */
	public void getAllThreadsinfo(OutputStream os) {
		String msg = getAllThreadsinfo();
		try {
			os.write(msg.getBytes());
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取两个队列的信息
	 */
	public void getAllThreadsinfo(Log log) {
		log.info(getAllThreadsinfo());
	}
	
	/**
	 * 用于返回回调处理类
	 */
	@SuppressWarnings("unchecked")
	protected IFutureHandler<T> newHandlerClass() throws InstantiationException, IllegalAccessException {
		return handlerClass.getClass().newInstance();
	}
	
	/**
	 * 池的任务提交函数
	 * @param bean
	 */
	public void execute(T bean) {
		try {
			runCallTask(new Task(bean));
		} catch (ThreadException e) {
			log.lerror("任务超时.", getThreadExceptionMessage(bean));
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	protected Object[] getThreadExceptionMessage(T bean) {
		return new Object[0];
	}
	
	/**
	 * 池内任务的call方法回调
	 * @param bean
	 */
	public T taskCall(T bean){return null;}
	
	/**
	 * 池内任务
	 * @author HUYAO
	 *
	 */
	protected class Task extends SimpleCallableTask<T> {
		private T bean;
		public Task(T bean) {
			this.bean = bean;
		}
		
		@Override
		public T call() throws Exception {
			return taskCall(bean);
		}
		
		public T getBean() {
			return bean;
		}
	}
	
	/**
	 * 超时任务控制
	 * @author HUYAO
	 *
	 */
	private class FutureDoCall implements RunnableTask {
		private Callable<T> call;
		
		private IFutureHandler<T> handler;
		
		public FutureDoCall(Callable<T> call) throws InstantiationException, IllegalAccessException {
			this.call = call;
			if(handlerClass == null)
				handler = null;
			else
				handler = newHandlerClass();
		}
		
		public void run() {
			FutureTask<T> task = caller.execute(call);
//			long timeout = Integer.parseInt(ConfigProperties.main.getProperty("THREAD_TIMEOUT","60"));
			try {
				if(handler == null)
					task.get(handler_timeout,TimeUnit.SECONDS);
				else
					handler.handler(task.get(handler_timeout,TimeUnit.SECONDS));
			} catch (InterruptedException e) {
				throw new ThreadException(e);
			} catch (ExecutionException e) {
				throw new ThreadException(e);
			} catch (TimeoutException e) {
				if(handler == null)
					throw new ThreadException("线程:" + Thread.currentThread().getName() + " 任务:" + call.toString() + " 超时");
				else
					handler.timeout(((Task)call).getBean());
			} catch (Exception e) {
				throw new ThreadException(e);
			}
		}

		@Override
		public void afterExecute(Throwable t) {
		}

		@Override
		public void beforeExecute(Thread t) {
		}
		
	}

	public RunnableThreadPool getRunner() {
		return runner;
	}

	public CallableThreadPool<T> getCaller() {
		return caller;
	}

	public Class<?> getHandlerClass() {
		return handlerClass.getClass();
	}

	public int getHandler_timeout() {
		return handler_timeout;
	}
}
