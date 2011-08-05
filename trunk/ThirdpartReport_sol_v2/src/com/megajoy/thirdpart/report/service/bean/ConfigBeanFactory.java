package com.megajoy.thirdpart.report.service.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.sol.util.common.StringUtil;
import org.springframework.stereotype.Component;

import com.megajoy.thirdpart.report.dao.pojo.Config;

@Component
public class ConfigBeanFactory implements Serializable{

	private static final long serialVersionUID = 1L;

	private Map<Integer,ConfigBean> configBean;

	public ConfigBeanFactory() {
		configBean = new HashMap<Integer, ConfigBean>();
	}
	
	public Map<Integer, ConfigBean> getConfigBean() {
		return configBean;
	}
	
	public HttpConnectionManager getHttpManager(Integer which_service) {
		ConfigBean bean = configBean.get(which_service);
		if(bean == null) System.out.println(which_service);
		return bean.getHttpConnectionManager();
	}

	public void setConfigBean(Map<Integer, ConfigBean> configBean) {
		this.configBean = configBean;
	}
	
	public void addConfigBean(Integer which_service,ConfigBean configBean) {
		this.configBean.put(which_service, configBean);
	}
	
	@Override
	public String toString() {
		Set<Entry<Integer,ConfigBean>> set = configBean.entrySet();
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtil.newLine());
		sb.append("读取的作业配置情况:").append(StringUtil.newLine());
		sb.append("\twhich_service Discount           Longnumber Command  Gateway DayAccess\r\n");
		String pa = "\t%13s %8s %20s %7s %8s %9d" + StringUtil.newLine();
		
		for(Entry<Integer,ConfigBean> en : set) {
			Config config = en.getValue().getConfig();
			sb.append(String.format(pa, en.getKey(),config.getDiscount(),config.getLongnumber(),
					config.getCommand(),config.getGateway(),config.getCountDayAccess()));
		}
		
		return sb.toString();
	}
}
