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
	public List<InfoCategory> findCategoryType1(int clevel) throws Exception;
	public List<InfoCategory> findCategoryType(int clevel,int parent) throws Exception;

	/**
	 * 查询分类名称-id映射表
	 * @return
	 * @throws Exception
	 */
	public List<InfoCategory> findCategoryMapping() throws Exception;
	
	public List<InfoCategory> findCustom(Class<InfoCategory> clazz, InfoCategory obj, int page, int pageSize,
			String order) throws Exception;
	
	public int findCountCustom(InfoCategory obj) throws Exception;
}