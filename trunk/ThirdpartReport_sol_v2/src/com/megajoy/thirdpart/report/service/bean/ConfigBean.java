package com.megajoy.thirdpart.report.service.bean;

import java.io.IOException;

import org.sol.util.common.SpringContextUtil;
import org.sol.util.textCounter.Counter;
import org.sol.util.textCounter.RandomData;

import com.megajoy.thirdpart.report.common.Logger;
import com.megajoy.thirdpart.report.dao.pojo.Config;

public class ConfigBean {
	private Config config;
	
	private Counter successCounter;
	
	private Counter allCounter;
	
	private HttpConnectionManager httpConnectionManager;
	
	private RandomData deductionRandom;

	public ConfigBean(Config config) {
		this.config = config;
		this.successCounter = new Counter(config.getCountDayAccess(),
				config.getCounterDocpath(), "tp_" + config.getWhich_service() + "_");
		this.allCounter = new Counter(config.getCounterDocpath(), "tpAll_" + config.getWhich_service() + "_");
		this.httpConnectionManager = (HttpConnectionManager) SpringContextUtil.getBean("httpConnectionManager", config.getSync_url());
		
		try {
			this.deductionRandom = new RandomData(config.getCounterDocpath(), config.getWhich_service().toString(),config.getDiscount().intValue());
		} catch (IOException e) {
			Logger.SYS.info("初始化扣量随机数组错误",e);
		} catch (ClassNotFoundException e) {
			Logger.SYS.info("初始化扣量随机数组错误",e);
		}
	}
	
	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	public Counter getSuccessCounter() {
		return successCounter;
	}

	public void setSuccessCounter(Counter successCounter) {
		this.successCounter = successCounter;
	}

	public Counter getAllCounter() {
		return allCounter;
	}

	public void setAllCounter(Counter allCounter) {
		this.allCounter = allCounter;
	}

	public HttpConnectionManager getHttpConnectionManager() {
		return httpConnectionManager;
	}

	public void setHttpConnectionManager(HttpConnectionManager httpConnectionManager) {
		this.httpConnectionManager = httpConnectionManager;
	}

	public RandomData getDeductionRandom() {
		return deductionRandom;
	}

	public void setDeductionRandom(RandomData deductionRandom) {
		this.deductionRandom = deductionRandom;
	}

}
