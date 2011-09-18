package com.sol.kx.web.service;

import java.util.Map;

import com.sol.kx.web.dao.pojo.InfoCategory;
import com.sol.kx.web.service.bean.ComboBoxBean;
import com.sol.kx.web.service.bean.PagerBean;

public interface InfoCategoryService extends BaseService<InfoCategory>{

	/**
	 * 查询分类
	 * @param clevel
	 * @return
	 */
	public ComboBoxBean findCategoryType(int clevel,String defaultText);

	/**
	 * 查询分类名称-ID映射表 <'clevel+cname',id>
	 * @return
	 */
	public Map<String,Integer> findCategoryMapping();
	
	/**
	 * 用于核定数据第一页的选择类别
	 * @param input
	 * @return
	 */
	public Map<String,Integer> findCategoryChoose(InfoCategory input);
	
	public PagerBean<InfoCategory> findCustom(PagerBean<InfoCategory> bean,InfoCategory obj);
		
}