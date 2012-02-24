package com.sol.kx.web.common;

import org.apache.ibatis.logging.LogFactory;

public class MyBatisLog4j {
	public MyBatisLog4j() {
		LogFactory.useLog4JLogging();
	}
}
