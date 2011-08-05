package com.megajoy.thirdpart.report.dao;

import java.util.List;

import com.megajoy.thirdpart.report.dao.pojo.Config;

public interface ConfigDao {

	/**
	 * 读取配置文件
	 * @return
	 * @throws Exception
	 */
	public List<Config> findConfig() throws Exception;

}