package org.sol.util.socket.message;

import java.io.IOException;

import org.sol.util.socket.DataInputStream;
import org.sol.util.socket.DataOutputStream;

/**
 * 注册监控线程信息
 * @author HUYAO
 *
 */
public class MonitorThreadReport extends Content{
	// 监控类名称 长度
	private int threadClassLength;
	
	// 监控类名称
	private String threadClass;
	
	// 监控线程组名称 长度
	private int threadGroupLength;
	
	// 监控线程组名称
	private String threadGroup;
	
	public MonitorThreadReport(int threadClassLength,String threadClass,int threadGroupLength,String threadGroup) {
		super(Command.MONITOR_THREAD_REPORT.CMD,threadClassLength+threadGroupLength+8);
		this.threadClass = threadClass;
		this.threadClassLength = threadClassLength;
		this.threadGroup = threadGroup;
		this.threadGroupLength = threadGroupLength;
	}
	
	public MonitorThreadReport() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void getSelfBytes(DataOutputStream ds) throws IOException {
		ds.writeInt(threadClassLength);
		ds.writeString(threadClass);
		ds.writeInt(threadGroupLength);
		ds.writeString(threadGroup);
	}

	@Override
	protected void parseSelfBytes(DataInputStream ds) throws IOException {
		this.threadClassLength = ds.readInt();
		this.threadClass = ds.readString(threadClassLength);
		this.threadGroupLength = ds.readInt();
		this.threadGroup = ds.readString(threadGroupLength);
	}
	
	public int getThreadClassLength() {
		return threadClassLength;
	}

	public void setThreadClassLength(int threadClassLength) {
		this.threadClassLength = threadClassLength;
	}

	public String getThreadClass() {
		return threadClass;
	}

	public void setThreadClass(String threadClass) {
		this.threadClass = threadClass;
	}

	public int getThreadGroupLength() {
		return threadGroupLength;
	}

	public void setThreadGroupLength(int threadGroupLength) {
		this.threadGroupLength = threadGroupLength;
	}

	public String getThreadGroup() {
		return threadGroup;
	}

	public void setThreadGroup(String threadGroup) {
		this.threadGroup = threadGroup;
	}

	
}
