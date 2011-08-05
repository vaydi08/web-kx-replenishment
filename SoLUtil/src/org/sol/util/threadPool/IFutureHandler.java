package org.sol.util.threadPool;

/**
 * 回调方法处理句柄
 * @author HUYAO
 *
 * @param <T>
 */
public interface IFutureHandler<T> {
	/**
	 * 结果处理
	 * @param t
	 */
	public void handler(T t);
	
	/**
	 * 超时
	 * @param t
	 */
	public void timeout(T t);
}
