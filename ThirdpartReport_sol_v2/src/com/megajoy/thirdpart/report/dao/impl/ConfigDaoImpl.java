package com.megajoy.thirdpart.report.dao.impl;

import java.util.List;
import org.sol.util.c3p0.DataConsoleUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.megajoy.thirdpart.report.common.Constants;
import com.megajoy.thirdpart.report.dao.ConfigDao;
import com.megajoy.thirdpart.report.dao.pojo.Config;

@Repository
public class ConfigDaoImpl extends BaseDaoImpl implements ConfigDao{
	
	@Value("${sql.config}")
	private String SQL_CONFIG;
	@Value("${groupid}")
	private int groupid;
	
	/* (non-Javadoc)
	 * @see com.megajoy.thirdpart.report.dao.impl.ConfigDao#findConfig()
	 */
	public List<Config> findConfig() throws Exception {
		return super.find(Constants.DB_LOGREPORT, Config.class, SQL_CONFIG, 
				DataConsoleUtil.getClassDefine(Config.class), groupid);
	}

}
