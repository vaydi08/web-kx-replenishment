package org.sol.util.threadMonitor;

import java.io.IOException;

import org.sol.util.socket.SocketAdpater;
import org.sol.util.socket.message.ContentFactory;
import org.sol.util.socket.message.PacketContent;
import org.sol.util.socket.message.XMLContent;

/**
 * 被监听线程客户端包装类
 * @author HUYAO
 *
 */
public class ActiveTestClient extends SocketAdpater{
	public ActiveTestClient(String host,int port,boolean debug) {
		super(host,port,debug,true,2);
	}
	
	/**
	 * 心跳检测
	 */
	public void activeTest() {
		if(isConnected()) {
			PacketContent content = ContentFactory.getContent("ActiveTest");

			write(content);
		}
	}

	/**
	 * 尝试连接
	 * @param threadClass
	 * @param groupName
	 * @throws IOException
	 */
	public void tryConnect(String threadClass,String threadName,String groupName) throws IOException {
		if(!isConnected()) {
			log.debug("尝试连接线程监视器");
			super.connect();

			if(isConnected())
				registerThread(threadClass,threadName,groupName);
		}
	}
	
	/**
	 * 注册线程信息
	 * @param threadClass
	 * @param groupName
	 * @throws IOException
	 */
	public void registerThread(String threadClass,String threadName,String groupName) throws IOException {			
		log.debug("注册线程信息");
		XMLContent report = (XMLContent)ContentFactory.getContent("MonitorThreadReport");
		report.setItemValue("threadClass", threadClass);
		report.setItemValue("threadName", threadName);
		report.setItemValue("threadGroup", groupName);
		write(report);
	}
}
