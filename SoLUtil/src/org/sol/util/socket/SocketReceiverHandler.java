package org.sol.util.socket;

import org.sol.util.socket.message.PacketContent;

/**
 * 用来处理接收信息的情况
 * @author HUYAO
 *
 */
public interface SocketReceiverHandler {
	
	/**
	 * 产生一个初次连接的反应
	 * @param content
	 * @return
	 */
	public PacketContent initReceive(String address,int port,PacketContent content);
	
	/**
	 * 初次连接的数据包为空,或有错误
	 * @return
	 */
	public PacketContent initError();
	
	/**
	 * 每次的请求
	 * @param content
	 * @return
	 */
	public PacketContent receive(PacketContent content);
	
	/**
	 * 连接被断开
	 */
	public void disconnected();
}
