package com.megajoy.thirdpart.report.service.queue;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.sol.util.common.SerializableUtil;
import org.sol.util.log.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.megajoy.thirdpart.report.dao.pojo.ThirdpartReport;
import com.megajoy.thirdpart.report.service.bean.ThirdpartReportBean;
import com.megajoy.thirdpart.report.service.bean.WaitForSendData;

@Service
public class QueueManager implements Serializable{

	private static final long serialVersionUID = 5830491811840628600L;
	
	@Value("${queue.docpath}")
	private transient String SERIALIZE_DOCPATH;
	private static final transient String SERIALIZE_FILENAME = "queue.dat";
		
	private BlockingQueue<ThirdpartReportBean> workQueue;
	
	private BlockingQueue<List<ThirdpartReport>> updateQueue;
		
	private BlockingQueue<WaitForSendData> httpQueue;
	
	public QueueManager() {
		workQueue = new LinkedBlockingQueue<ThirdpartReportBean>();
		updateQueue = new LinkedBlockingQueue<List<ThirdpartReport>>();
		httpQueue = new LinkedBlockingQueue<WaitForSendData>();
	}

	public String queueStatus() {
		StringBuilder sb = new StringBuilder();
		String NEWLINE = System.getProperty("line.separator");
		sb.append("workQueue length:\t" + workQueue.size()).append(NEWLINE);
		sb.append("httpQueue length:\t" + httpQueue.size()).append(NEWLINE);
		sb.append("updateQueue length:\t" + updateQueue.size()).append(NEWLINE);
		
		return sb.toString();
	}
	
	public void serialize() {
		Logger.SYS.info("开始将队列序列化");
		try {
			SerializableUtil.serialize(this, SERIALIZE_DOCPATH, SERIALIZE_FILENAME);
		} catch (IOException e) {
			Logger.SYS.error("队列序列化错误",e);
		}
	}
	
	@PostConstruct
	public void deSerialize() {
		Logger.SYS.info("从序列化文件中读取队列信息");
		File file = new File(SERIALIZE_DOCPATH,SERIALIZE_FILENAME);
		if(!file.exists()) {
			Logger.SYS.info("不存在序列化队列文件,跳过...");
			return;
		}
		try {
			QueueManager obj = (QueueManager)SerializableUtil.deSerialize(SERIALIZE_DOCPATH, SERIALIZE_FILENAME);
			this.workQueue = obj.getWorkQueue();
			this.updateQueue = obj.getUpdateQueue();
			this.httpQueue = obj.getHttpQueue();
		} catch (IOException e) {
			Logger.SYS.error("队列序列化错误",e);
		} catch (ClassNotFoundException e) {
			Logger.SYS.error("队列序列化错误",e);
		}
	}
	
	public void addWork(ThirdpartReportBean bean) {
		workQueue.add(bean);
	}
	
	public void addHttp(WaitForSendData data) {
		httpQueue.add(data);
	}
	
	public void addUpdate(List<ThirdpartReport> list) {
		updateQueue.add(list);
	}
	
	public ThirdpartReportBean taskWork() throws InterruptedException {
		return workQueue.take();
	}
	
	public WaitForSendData taskHttp() throws InterruptedException {
		return httpQueue.take();
	}
	
	public List<ThirdpartReport> takeUpdate() throws InterruptedException {
		return updateQueue.take();
	}

	public BlockingQueue<ThirdpartReportBean> getWorkQueue() {
		return workQueue;
	}

	public BlockingQueue<List<ThirdpartReport>> getUpdateQueue() {
		return updateQueue;
	}

	public BlockingQueue<WaitForSendData> getHttpQueue() {
		return httpQueue;
	}

}
