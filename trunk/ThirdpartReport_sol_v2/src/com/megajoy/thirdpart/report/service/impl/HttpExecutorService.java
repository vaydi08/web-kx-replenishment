package com.megajoy.thirdpart.report.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.sol.util.textCounter.Counter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.megajoy.thirdpart.report.common.Logger;
import com.megajoy.thirdpart.report.dao.pojo.ThirdpartReport;
import com.megajoy.thirdpart.report.service.bean.ConfigBeanFactory;
import com.megajoy.thirdpart.report.service.bean.WaitForSendData;

@Service
public class HttpExecutorService {

	private Executor pool;
	
	@Value("${http.poolSize}")
	private int poolSize;
	
	@Value("${http.thread_timeout}")
	private int timeout;
	
	@Autowired
	private ConfigBeanFactory configBeanFactory;
	
	private final static Object lock = new Object();
	
	@PostConstruct
	public void init() {
		pool = Executors.newFixedThreadPool(poolSize);
	}
	
	public FutureTask<String> runTask(WaitForSendData data) {
		FutureTask<String> task = new FutureTask<String>(new Runned(data),"success");
		pool.execute(task);
		
		return task;
	}
	
	public List<ThirdpartReport> takeHttpTask(WaitForSendData data) {
		FutureTask<String> task = runTask(data);
		
		try {
			task.get(timeout, TimeUnit.MILLISECONDS);
			return data.getList();
		} catch (InterruptedException e) {
			Logger.SYS.lerror(data.getWhich_service(), e.getMessage(), e);
			return null;
		} catch (ExecutionException e) {
			Logger.SYS.lerror(data.getWhich_service(), e.getMessage(), e);
			return null;
		} catch (TimeoutException e) {
			Logger.SYS.lerror(data.getWhich_service(), "HTTP数据发送超时");
			return null;
		}
	}

	private class Runned implements Runnable {
		private WaitForSendData data;
		
		public Runned(WaitForSendData data) {
			this.data = data;
		}
		
		@Override
		public void run() {
			Logger.SEND.debug(data.toString());
			if(data.isReadyToSend()) {
				data.setHttpResponse(false);
				// 有正常发送数据情况下
				if(data.getReadyForSend() > 0) {
					// 正常发送
					Logger.SEND.linfo(data.getWhich_service(),"已处理的发送数据 发送长度|扣量长度:",data.getReadyForSend(),data.getDeduction());
					// 进行发送,并计数
					data.setHttpResponse(sendHttp(data.getWhich_service(), data.getXml()));
					
					if(data.isHttpResponse()) {
						dayCount(data.getWhich_service(), data.getReadyForSend(), data.getReadyForSend() + data.getDeduction());
					}
				} else if(data.getDeduction() > 0) {
					// 没有正常发送数据,但是有扣量情况下
					Logger.SEND.linfo(data.getWhich_service(), "处理当前获取数据只有扣量数据,扣量长度:", data.getDeduction());
					dayAllCount(data.getWhich_service(), data.getDeduction());
					data.setHttpResponse(true);
				}
			} else {
				// 重发
				Logger.SEND.linfo(data.getWhich_service(),"已处理的重发数据 发送长度:",data.getReadyForSend());
				// 进行发送
				data.setHttpResponse(sendHttp(data.getWhich_service(), data.getXml()));
			}
		}
		
		// 发送HTTP数据
		private boolean sendHttp(Integer which_service,String xml) {
			Logger.UNCATCH.ldebug(which_service,"开始发送HTTP数据",xml);
			try {
				return configBeanFactory.getHttpManager(which_service).post(xml);
			} catch (IOException e) {
				Logger.SEND.lerror(which_service, "发送HTTP数据错误", e,xml);
				return false;
			}
		}
		
		// 计数
		private void dayCount(Integer which_service,int success,int all) {
			// 计数器
			Counter counterAll = configBeanFactory.getConfigBean().get(which_service).getAllCounter();
			Counter counter = configBeanFactory.getConfigBean().get(which_service).getSuccessCounter();
			
			synchronized (lock) {
				try {
					counter.takeMOCount(success);
					counterAll.takeMOCount(all);
				}catch(Exception e){
					Logger.SYS.error(e.getMessage(),e);
				}
			}
			
		}
		private void dayAllCount(Integer which_service,int all) {
			// 计数器
			Counter counterAll = configBeanFactory.getConfigBean().get(which_service).getAllCounter();
			
			synchronized (lock) {
				counterAll.takeMOCount(all);
			}
		}
	}

	public int getPoolSize() {
		return poolSize;
	}
	
}
