package com.megajoy.thirdpart.report.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.megajoy.thirdpart.report.common.Logger;
import com.megajoy.thirdpart.report.dao.ConfigDao;
import com.megajoy.thirdpart.report.dao.pojo.Config;
import com.megajoy.thirdpart.report.service.ConfigService;
import com.megajoy.thirdpart.report.service.bean.ConfigBean;

@Service("configService")
public class ConfigServiceImpl extends BaseServiceImpl implements ConfigService{
	@Autowired
	private ConfigDao configDao;
		
	/* (non-Javadoc)
	 * @see com.megajoy.thirdpart.report.service.impl.ConfigService#initConfig()
	 */
	public void initConfig() {
		Logger.SYS.info("系统启动,开始初始化...");
		
		List<Config> list = null;
		try {
			list = configDao.findConfig();
		} catch (Exception e) {
			exceptionHandler.onInit(e);
		}
		
		for(Config config : list) {
			ConfigBean configBean = new ConfigBean(config);
			configBeanFactory.addConfigBean(config.getWhich_service(), configBean);
		}
		
		if(list.size() == 0) {
			Logger.SYS.info("系统没有读取到有效配置的业务,系统退出...");
			System.exit(0);
		}
		
		Logger.SYS.info(configBeanFactory.toString());
	}
}
