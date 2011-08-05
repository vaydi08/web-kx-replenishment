package org.sol.util.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sol.util.socket.message.PacketContent;

/**
 * SOCKET包装类
 * @author HUYAO
 *
 */
public abstract class SocketServerListener {
	// 监听端口
	private int port;
	
	// 服务器线程组
	private ThreadGroup group;
	
	// 线程序号
	private static int _seq = 1;
	
	// 日志
	protected Log log;
	
	// 调试信息
	protected boolean debug;
	
	// 主线程
	private Listener listener;
	private Thread serverThread;
	
	private List<SocketThread> daemonThreadList;
	
	/**
	 * 监听端口
	 * @param port 端口
	 * @param debug 调试信息
	 */
	public SocketServerListener(int port,boolean debug) {
		this.port = port;
		this.debug = debug;

		this.log = LogFactory.getLog(getClass());
	}
	
	/**
	 * 开始运行服务器监听
	 */
	public void startServer() {
		this.group = new ThreadGroup("SOCKET SERVER THREADGROUP");
		
		listener = new Listener();
		serverThread = new Thread(group,listener,"SOCKET SERVER LISTENER -" + getSeq());
		serverThread.start();
	}
	
	/**
	 * 终止线程
	 */
	public void close() {
		listener.close();
		
		if(daemonThreadList != null)
			for(SocketThread r : daemonThreadList) 
				r.close();
		
		while(group.activeCount() > 0) {
			log.debug("服务器活动线程:" + group.activeCount() + " 等待线程活动终止");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
		log.debug("服务器活动线程已全部终止");
		group.destroy();
		
		listener = null;
		serverThread = null;
	}
	
	
	// 监听类
	private class Listener implements Runnable {
		// ServerSocket
		private ServerSocket serverSocket;
		
		public Listener() {
			try {
				log.info("启动SOCKET服务器,监听端口:" + port);
				this.serverSocket = new ServerSocket(port);
				log.debug("开始接受远程连接");
			} catch (IOException e) {
				log.error("启动SOCKET服务器失败",e);
				System.exit(-1);
			}
		}
		
		@Override
		public void run() {
			while(true) {
				Socket socket;
				
				try {
					socket = serverSocket.accept();
				} catch (IOException e) {
					log.error(e,e);
					break;
				}
				
				handlerSocket(socket);
			}
			log.debug("ServerSocket监听器退出");
		}
		
		public void close() {
			try {
				serverSocket.close();
			} catch (IOException e) {
			}
		}
	}
	
	/**
	 * 处理获取到的连接
	 * @param socket
	 */
	protected void handlerSocket(Socket socket) {
		if(daemonThreadList == null)
			daemonThreadList = new ArrayList<SocketThread>();
		
		SocketThread r;
		try {
			log.info("获取到远程连接 " + socket.getInetAddress().getHostName() + " - " + socket.getInetAddress().getHostAddress());
			r = new SocketThread(socket,getSocketReceiverHandler(),debug);
			
		} catch (IOException e) {
			log.error("启动线程失败",e);
			return;
		}
		Thread monitorThread = new Thread(getGroup(),r,"[SS] SOCKET THREAD -" + getSeq());
		monitorThread.setDaemon(true);
		monitorThread.start();
		
		daemonThreadList.add(r);
	}
	
	/**
	 * 生成处理句柄,需要子类生成
	 * 最好返回一个新对象用来处理不同连接的信息
	 * @return
	 */
	protected abstract SocketReceiverHandler getSocketReceiverHandler();
	
	protected class SocketThread extends SocketAdpater implements Runnable {
		private int Switch = 1;
		
		private SocketReceiverHandler handler;

		public SocketThread(Socket socket,SocketReceiverHandler handler,boolean debug) throws IOException {
			super(socket,debug);
			
			this.handler = handler;
			
			Switch = 2;
			
			setConnected(true);
		}
		@Override
		public void run() {
			PacketContent content = null;
			
			// 接收一个初始化请求
			if(isConnected()) {
				try {
					content = receive();
				} catch (IOException e) {
					log.error(e,e);
					write(handler.initError());
					return;
				}
				if(content != null) {
					PacketContent resp = handler.initReceive(getSocket().getInetAddress().getHostAddress(),getPort(),content);
					if(resp != null) 
						write(resp);
				} else {
					write(handler.initError());
					return;
				}
			}
			
			// 处理每个请求
			while(isConnected() && Switch == 2) {

				try {
					if((content = receive()) != null ) {
						PacketContent resp = handler.receive(content);
						if(resp != null) {
							write(resp);
						}
					}
				} catch (IOException e) {
					log.error(e,e);
				}

			}
			
			handler.disconnected();
			log.debug("当前线程退出 线程[" + Thread.currentThread().getName() + "]");
		}
		
		public void close() {
			Switch = 3;

			super.close();
		}
	}
	
	public int getSeq() {
		if(_seq == Integer.MAX_VALUE)
			_seq = 1;
		
		return _seq++;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public ThreadGroup getGroup() {
		return group;
	}

	public void setGroup(ThreadGroup group) {
		this.group = group;
	}
}
