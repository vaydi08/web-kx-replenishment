package org.sol.util.threadMonitor;

/**
 * 线程信息BEAN
 * @author HUYAO
 *
 */
public class ThreadInfo {
	// 线程类名
	private String threadClass;
	// 线程名
	private String threadName;
	// 线程组名
	private String threadGroupname;
	
	// 线程来源地址
	private String address;
	
	// 线程来源端口
	private int port;
	
	// 心跳包的序列号
	private int sequence;
	
	// 上一次记录心跳包的时间
	private long lastActiveTestTimestamp;
	
	public ThreadInfo(String address,int port,String threadClass,String threadName,String threadGroupname) {
		this.address = address;
		this.port = port;
		this.threadClass = threadClass;
		this.threadName = threadName;
		this.threadGroupname = threadGroupname;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((threadClass == null) ? 0 : threadClass.hashCode());
		result = prime * result
				+ ((threadGroupname == null) ? 0 : threadGroupname.hashCode());
		result = prime * result
				+ ((threadName == null) ? 0 : threadName.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ThreadInfo other = (ThreadInfo) obj;
		if (threadClass == null) {
			if (other.threadClass != null)
				return false;
		} else if (!threadClass.equals(other.threadClass))
			return false;
		if (threadGroupname == null) {
			if (other.threadGroupname != null)
				return false;
		} else if (!threadGroupname.equals(other.threadGroupname))
			return false;
		if (threadName == null) {
			if (other.threadName != null)
				return false;
		} else if (!threadName.equals(other.threadName))
			return false;
		return true;
	}
	
	public String getThreadClass() {
		return threadClass;
	}
	public void setThreadClass(String threadClass) {
		this.threadClass = threadClass;
	}
	public String getThreadName() {
		return threadName;
	}
	public void setThreadName(String threadName) {
		this.threadName = threadName;
	}
	public String getThreadGroupname() {
		return threadGroupname;
	}
	public void setThreadGroupname(String threadGroupname) {
		this.threadGroupname = threadGroupname;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public long getLastActiveTestTimestamp() {
		return lastActiveTestTimestamp;
	}
	public void setLastActiveTestTimestamp(long lastActiveTestTimestamp) {
		this.lastActiveTestTimestamp = lastActiveTestTimestamp;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

}