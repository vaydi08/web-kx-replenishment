package com.sol.kx.web.service;

import com.sol.kx.web.service.bean.ComboBoxBean;

public interface InfoCategoryService {

	/**
	 * 查询分类
	 * @param clevel
	 * @return
	 */
	public ComboBoxBean findCategoryType(int clevel,String defaultText);

}