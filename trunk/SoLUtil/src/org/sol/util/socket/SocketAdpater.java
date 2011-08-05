package org.sol.util.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sol.util.common.StringUtil;
import org.sol.util.socket.message.Command;
import org.sol.util.socket.message.Content;
import org.sol.util.socket.message.ContentFactory;
import org.sol.util.socket.message.Header;
import org.sol.util.socket.message.PacketContent;
import org.sol.util.socket.message.XMLContent;
import org.sol.util.socket.message.XMLHeader;


/**
 * SOCKET包装类
 * @author HUYAO
 *
 */
public class SocketAdpater {
	
	protected Bean bean;
	
	// 用于处理接收消息的句柄
	protected SocketReceiverHandler receiverHandler;
	
	// DEBUG
	protected boolean debug;
	
	// LOG
	protected Log log;
	
	private class Bean {
		// 服务器地址
		protected String host;
		// 端口
		protected int port;
		
		// socket
		protected Socket socket;
			
		// 是否需要重连
		protected boolean autoReconnect;
		
		// 是否连接上
		protected boolean connected;
		
		// 重连次数
		protected int reconnectTimes;
		
		// 是否使用异步处理模式
		protected boolean asynMode;
		
		// 记录连接次数
		protected int connectTimes;
		
		public String toString() {
			return String.format("Host [%s:%d] | autoReconnect:%b|connected:%b|reconnectTimes:%d(-1)|asynMode:%b",
					host,port,autoReconnect,connected,reconnectTimes,asynMode);
		}
	}
	
	/**
	 * 作为客户端时的构造 异步处理模式
	 * @param host 服务器地址
	 * @param port 服务器端口
	 * @param debug DEBUG
	 */
	public SocketAdpater(String host,int port,boolean debug,
			boolean autoReconnect,int reconnectTimes,
			SocketReceiverHandler receiverHandler) {
		this.debug = debug;
		this.log = LogFactory.getLog(getClass());
		this.receiverHandler = receiverHandler;
		
		this.bean = new Bean();
		bean.host = host;
		bean.port = port;
		bean.autoReconnect = autoReconnect;
		bean.connectTimes = 0;
		bean.reconnectTimes = reconnectTimes + 1;
		bean.asynMode = true;
		bean.connected = false;
	}
	
	/**
	 * 作为客户端时的构造 同步处理模式
	 * @param host 服务器地址
	 * @param port 服务器端口
	 * @param debug DEBUG
	 */
	public SocketAdpater(String host,int port,boolean debug,
			boolean autoReconnect,int reconnectTimes) {
		this(host,port,debug,autoReconnect,reconnectTimes,null);
		bean.asynMode = false;
	}
	
	/**
	 * 作为服务器时的构造
	 * @param s SOCKET
	 * @param debug DEBUG
	 * @throws IOException
	 */
	public SocketAdpater(Socket s,boolean debug) throws IOException {
		this.debug = debug;
		log = LogFactory.getLog(getClass());
		
		this.bean = new Bean();
		bean.socket = s;
		bean.asynMode = true;
		bean.connected = false;
	}
	
	/**
	 * 连接
	 * @throws IOException
	 */
	public void connect() throws IOException {
		log.info("开始连接 " + bean.toString());

		try {
			setConnected(true);
			connectSocket();
		} catch (ConnectException e) {
			log.info("连接到远程服务器失败");
			close();
			while(needReconnect())
				reconnect();
			return;
		} catch (InterruptedIOException e) {
			log.info("连接服务器超时 消息:\n\t" + bean.socket);
			close();
			while(needReconnect())
				reconnect();
			return;
		} catch (IOException e) {
			close();
			throw e;
		}
		bean.socket.setSoTimeout(40000);
		
		log.debug("连接成功");
		bean.connectTimes = 0;
	}
	
	/**
	 * 重新连接服务器
	 * @throws IOException 
	 */
	protected void reconnect() throws IOException {
		log.info("\t重新连接 " + bean.toString());
		
		try {
			setConnected(true);
			connectSocket();
		} catch (ConnectException e) {
			log.info("\t连接到远程服务器失败");
			close();
			return;
		} catch (InterruptedIOException e) {
			log.info("\t连接到远程服务器超时 消息:\n\t" + bean.socket);
			close();
			return;
		} catch (IOException e) {
			close();
			throw e;
		}

		log.debug("\t重新连接成功");
		bean.connectTimes = 0;
		
//		startReceiver();
	}
	
	private void connectSocket() throws IOException {
		bean.socket = new Socket();
		InetSocketAddress addr = new InetSocketAddress(bean.host, bean.port);

		// 连接SOCKET并且设置超时时间为20秒
		bean.socket.connect(addr, 20000);
		// 设置读写超时为40秒
		bean.socket.setSoTimeout(40000);
	}
	
	/**
	 * 写数据包 同步发送时返回RESP对象,异步返回NULL
	 * @param content
	 */
	public Content write(Content content){
		if(debug) {
			String s = String.format("send message SEQUENCE[%d] COMMAND[%d] CONTENTLENGHT[%d] \n\t- %s", 
					content.getHeader().getSequence(),content.getHeader().getCommand(),
					content.getHeader().getContentLength(),content.getClass());
			log(s);
		}
		
		try {
			write(content.getBytes());
		}catch (Throwable e) {
			log("字节化发送消息时产生错误",e);
			return null;
		}
		
		if(bean.asynMode) {
			return null;
		} else {
			Content resp = null;
			try {
				resp = readResp();
			} catch (IOException e) {
				log("接收数据时错误",e);
			}
			
			return resp;
		}
	}
	
	/**
	 * 写数据包 同步发送时返回RESP对象,异步返回NULL
	 * @param content
	 */
	public PacketContent write(PacketContent content){
		if(debug) {
			log("Write PacketContent\n" + content.toString());
		}
		
		try {
			write(content.getBytes());
		}catch (Throwable e) {
			log("字节化发送消息时产生错误",e);
			return null;
		}
		
		if(bean.asynMode) {
			return null;
		} else {
			PacketContent resp = null;
			try {
				resp = receive();
			} catch (IOException e) {
				log("接收数据时错误",e);
			}
			
			return resp;
		}
	}
	
	/**
	 * 写数据包
	 * @param msg
	 * @throws IOException
	 */
	public void write(byte[] msg) throws IOException {
		try {
			bean.socket.getOutputStream().write(msg);
		} catch (SocketException e) {
			log.info("连接已断开 " + bean.toString());
			close();
			return;
		} catch (InterruptedIOException e) {
			log.info("发送信息超时 尝试重发消息\t" + bean.socket);
			if(isConnected())
				rewrite(msg);
			return;
		} catch (IOException e) {
			log("发送信息时产生网络错误 关闭连接",e);
			close();
			throw e;
		} 
	}
	
	/**
	 * 重发信息
	 * @param content
	 */
	private void rewrite(byte[] msg) {
		try {
			bean.socket.getOutputStream().write(msg);
		} catch (IOException e) {
			log.error("\t重发消息失败");
			return;
		} catch (Throwable e) {
			log.error("\t重发消息失败");
			return;
		}
		log.debug("\t重发消息成功");
	}
	
	/**
	 * 读数据包
	 * @return
	 * @throws IOException
	 */
	public Content readResp() throws IOException {
		byte[] header = new byte[Header.getHeaderSize()];
		byte[] content;
		
		try {
			read(header);
			Header h = Header.parseBytes(header);
			content = new byte[h.getContentLength()];
			read(content);
			Content c = Command.MessageFactory.getContent(h, content);
			
			if(debug) {
				log.debug(String.format("receive message SEQUENCE[%d] COMMAND[%d] CONTENTLENGHT[%d] \n\t- %s", 
						h.getSequence(),h.getCommand(),
						h.getContentLength(),c.getClass()));
			}
			
			return c;
		} catch (SocketException e) {
			log.debug("连接已断开");
			close();
		} catch (InterruptedIOException e) {
			log.debug("接收信息超时");
			throw new IOException("接收信息超时");
		} catch (IOException e) {
			log.debug("接收信息时产生网络错误",e);
			throw e;
		}
		
		
		return null;
	}
	
	/**
	 * 读数据包
	 * @return
	 * @throws IOException
	 */
	public PacketContent receive() throws IOException {
		XMLHeader headerTemplate = ContentFactory.getHeaderTemplate();
		XMLContent contentTemplate;
		byte[] header = new byte[headerTemplate.getHeaderLength()];
		byte[] content;
		
		try {
			read(header);
			headerTemplate.parseBytes(header);
			if(headerTemplate.getPacketLength() - headerTemplate.getHeaderLength() < 0)
				throw new IOException("错误的数据包,PacketLength:" + headerTemplate.getPacketLength()
						+ " HeaderLength:" + headerTemplate.getHeaderLength() + "\n\t" + 
						StringUtil.deepArray(header));
			
			content = new byte[headerTemplate.getPacketLength() - headerTemplate.getHeaderLength()];
			read(content);
			contentTemplate = ContentFactory.getContent(headerTemplate);
			contentTemplate.parseBytes(content);
			
			if(debug) 
				log.debug("Receive Message\n" + contentTemplate);

			return contentTemplate;
		} catch (SocketException e) {
			log.debug("连接已断开");
			close();
		} catch (InterruptedIOException e) {
			log.debug("接收信息超时");
			throw new IOException("接收信息超时");
		} catch (IOException e) {
			log.debug("接收信息时产生网络错误",e);
			close();
			throw e;
		}
		
		
		return null;
	}
	
	/**
	 * 读数据包
	 * @param buf
	 * @return
	 * @throws IOException
	 */
	public int read(byte[] buf) throws IOException {
		return read(buf,buf.length);
	}
	/**
	 * 读数据包
	 * @param buf
	 * @param length
	 * @return
	 * @throws IOException
	 */
	public int read(byte[] buf,int length) throws IOException {
		if(!isConnected())
			return -1;
		
		InputStream is = bean.socket.getInputStream();

		int size = 0;
		int offset = 0;
		int len = length;

		while(len > 0 && (size = is.read(buf,offset,len)) != -1) {
			offset += size;
			len -= size;
		}
		
		return offset + size;
	}
	
	private void log(String msg) {
		log.debug(bean.host + ":" + bean.port + " - " + msg);
	}
	private void log(String msg,Throwable e) {
		log.error(bean.host + ":" + bean.port + " - " + msg,e);
	}
	
	public void write(int msg) throws IOException {
		bean.socket.getOutputStream().write(msg);
	}
	
	public void write(String msg) throws IOException {
		bean.socket.getOutputStream().write(msg.getBytes());
	}

	public void close() {
		try {
			log.debug("\t\t关闭SOCKET连接");
			if(bean.socket != null && bean.socket.isConnected()) {
				bean.socket.shutdownInput();
				bean.socket.shutdownOutput();

				if(!bean.socket.isInputShutdown())
					bean.socket.getInputStream().close();
				if(!bean.socket.isOutputShutdown())
					bean.socket.getOutputStream().close();
			}
			bean.connected = false;
		} catch (IOException e) {
			if(debug)
				log.error("关闭SOCKET的错误",e);
		} finally {
			try {
				if(bean.socket != null) {
					bean.socket.close();
					bean.socket = null;
				}
			} catch (IOException e) {
			}
		}
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public String getHost() {
		return bean.host;
	}

	public void setHost(String host) {
		bean.host = host;
	}

	public int getPort() {
		return bean.port;
	}

	public void setPort(int port) {
		bean.port = port;
	}
	
	public Socket getSocket() {
		return bean.socket;
	}

	public boolean isAutoReconnect() {
		return bean.autoReconnect;
	}

	public void setAutoReconnect(boolean autoReconnect) {
		bean.autoReconnect = autoReconnect;
	}

	public int getReconnectTimes() {
		return bean.reconnectTimes;
	}

	public void setReconnectTimes(int reconnectTimes) {
		bean.reconnectTimes = reconnectTimes;
	}

	public SocketReceiverHandler getReceiverHandler() {
		return receiverHandler;
	}

	public void setReceiverHandler(SocketReceiverHandler receiverHandler) {
		this.receiverHandler = receiverHandler;
	}

	public boolean isAsynMode() {
		return bean.asynMode;
	}

	public void setAsynMode(boolean asynMode) {
		bean.asynMode = asynMode;
	}

	public boolean isConnected() {
		return  bean.connected && bean.socket != null && bean.socket.isConnected();
	}

	public void setConnected(boolean connected) {
		bean.connected = connected;
	}

	public boolean needReconnect() {
		return bean.autoReconnect && ++bean.connectTimes < bean.reconnectTimes;
	}
	
	public Bean getBean() {
		return bean;
	}

	public void setBean(Bean bean) {
		this.bean = bean;
	}
}
