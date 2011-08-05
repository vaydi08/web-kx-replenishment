package org.sol.util.threadMonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sol.util.socket.SocketReceiverHandler;
import org.sol.util.socket.SocketServerListener;
import org.sol.util.socket.message.ContentFactory;
import org.sol.util.socket.message.PacketContent;
import org.sol.util.socket.message.XMLContent;

/**
 * 线程监视器 服务器端
 * @author HUYAO
 *
 */
public class ThreadMonitorServer extends SocketServerListener{

	// 维护线程表
	private Map<String,List<ThreadInfo>> threadMap;
	
	// 事件监听器
	private ThreadMonitorServerListener listener;
	
	/*
	 * (non-Javadoc)
	 * @see org.sol.util.socket.SocketServerListener#SocketServerListener(int,boolean)
	 */
	public ThreadMonitorServer(int port,boolean debug,ThreadMonitorServerListener listener) {
		super(port,debug);
		
		threadMap = new HashMap<String, List<ThreadInfo>>();
		
		this.listener = listener;
	}

	/**
	 * 加入新线程信息
	 * @param threadClass
	 * @param threadName
	 * @param threadGroup
	 */
	private void put(ThreadInfo info) {
		List<ThreadInfo> list = threadMap.get(info.getThreadGroupname());
		if(list == null) {
			list = new ArrayList<ThreadInfo>();
			threadMap.put(info.getThreadGroupname(),list);
		}
		list.add(info);
	}
	
	/**
	 * 移除断开的线程
	 * @param info
	 */
	private void remove(ThreadInfo info) {
		List<ThreadInfo> list = threadMap.get(info.getThreadGroupname());
		list.remove(info);
	}
	
	/**
	 * SOCKET接收器
	 * @author HUYAO
	 *
	 */
	private class Receiver implements SocketReceiverHandler {
		private ThreadInfo info;
		@Override
		public PacketContent receive(PacketContent content) {
			Integer command = content.getHeader().getCommand();
			if(command.equals(1)) {
				int sequence = content.getHeader().getSequence();
				long timestamp = System.currentTimeMillis();
				
				info.setSequence(sequence);
				info.setLastActiveTestTimestamp(timestamp);
				
				listener.onActiveTest(info);
			}
			
			PacketContent resp = ContentFactory.getContentResp(content, "ActiveTestResp");
			return resp;
		}

		@Override
		public PacketContent initReceive(String address,int port,PacketContent content) {
			Integer command = ContentFactory.getPacketCommand(content);
			if(command == 3) {
				XMLContent c = (XMLContent)content;
				String threadClass = (String) c.getItemValue("threadClass");
				String threadName = (String) c.getItemValue("threadName");
				String threadGroup = (String) c.getItemValue("threadGroup");
				
				info = new ThreadInfo(address,port,threadClass, threadName, threadGroup);
				
				put(info);
				
				listener.onNewThreadRegister(info);
			}
			
			return ContentFactory.getContentResp(content, "ActiveTestResp");
		}

		@Override
		public void disconnected() {
			remove(info);
			listener.onDisconnected(info);
		}

		@Override
		public PacketContent initError() {
			PacketContent content = ContentFactory.getContent("ResultResp");
			XMLContent c = (XMLContent)content;
			c.setItemValue("resultCode", -1);
			c.setItemValue("resultMsg", "错误的线程注册信息");
			return c;
		}
	}
	@Override
	protected SocketReceiverHandler getSocketReceiverHandler() {
		return new Receiver();
	}
	
	public Map<String, List<ThreadInfo>> getThreadMap() {
		return threadMap;
	}

	public void setThreadMap(Map<String, List<ThreadInfo>> threadMap) {
		this.threadMap = threadMap;
	}
	
	public List<ThreadInfo> getThreadInfoList(String threadGroup) {
		return threadMap.get(threadGroup);
	}
}
