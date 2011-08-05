package org.sol.util.threadPool;

/**
 * 线程错误 
 * 运行时异常
 * @author HUYAO
 *
 */
public class ThreadException extends RuntimeException{

	public ThreadException(String string) {
		super(string);
	}
	
	public ThreadException(Throwable t) {
		super(t);
	}
	
	public ThreadException(String msg,Throwable t) {
		super(msg,t);
	}

	private static final long serialVersionUID = 1L;

}
