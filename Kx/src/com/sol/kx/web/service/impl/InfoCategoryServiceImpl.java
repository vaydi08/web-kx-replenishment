package com.sol.kx.web.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sol.kx.web.dao.pojo.InfoCategory;
import com.sol.kx.web.service.InfoCategoryService;
import com.sol.kx.web.service.bean.ComboBoxBean;

@Service
public class InfoCategoryServiceImpl extends BaseServiceImpl implements InfoCategoryService{
	/* (non-Javadoc)
	 * @see com.sol.kx.web.service.impl.InfoCategoryService#findCategoryType(int)
	 */
	public ComboBoxBean findCategoryType(int clevel,String defaultText) {
		List<InfoCategory> list = null;
		try {
			list = infoCategoryDao.findCategoryType(clevel);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询[Info_category]分类时产生错误 clevel:" + clevel, e);
			return new ComboBoxBean();
		}
		
		ComboBoxBean bean = new ComboBoxBean();
		bean.addData(defaultText, 0,true);
		for(InfoCategory po : list)
			bean.addData(po.getCname(), po.getId());
		
		return bean;
	}
}
