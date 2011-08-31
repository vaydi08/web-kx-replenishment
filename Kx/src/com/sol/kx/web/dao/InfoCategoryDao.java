package com.sol.kx.web.dao;

import java.util.List;

import com.sol.kx.web.dao.pojo.InfoCategory;

public interface InfoCategoryDao extends BaseDao{

	/**
	 * 查询各个分类的列表
	 * @param clevel
	 * @return
	 * @throws Exception
	 */
	public List<InfoCategory> findCategoryType(int clevel) throws Exception;

}