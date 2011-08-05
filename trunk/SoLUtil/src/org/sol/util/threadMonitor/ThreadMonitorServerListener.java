package org.sol.util.threadMonitor;

/**
 * 监听服务器动作
 * @author HUYAO
 *
 */
public interface ThreadMonitorServerListener {
	/**
	 * 当新线程被注册时
	 * @param info
	 */
	public void onNewThreadRegister(ThreadInfo info);
	
	/**
	 * 当收到心跳包
	 * @param info
	 */
	public void onActiveTest(ThreadInfo info);
	
	/**
	 * 当连接断开
	 * @param info
	 */
	public void onDisconnected(ThreadInfo info);
}
