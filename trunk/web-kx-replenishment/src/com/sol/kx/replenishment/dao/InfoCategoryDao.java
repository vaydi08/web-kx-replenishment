package com.sol.kx.replenishment.dao;

import java.util.List;

import com.sol.kx.replenishment.dao.pojo.InfoCategory;

public interface InfoCategoryDao extends BaseDao<InfoCategory>{
	/**
	 * 获取产品目录
	 * @param level 第几级目录
	 * @return
	 */
	public List<InfoCategory> findCategory(int level);
}
