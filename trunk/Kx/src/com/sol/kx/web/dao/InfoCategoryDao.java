package com.sol.kx.web.dao;

import java.sql.SQLException;
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
	
	public String generatePname(Integer parent,String code) throws SQLException;
	public boolean checkExists(String code,Integer level,Integer parent) throws SQLException;
	public boolean checkDeleteExists(Integer id) throws SQLException;
	
	public int findCountCustom(InfoCategory obj) throws Exception;
}