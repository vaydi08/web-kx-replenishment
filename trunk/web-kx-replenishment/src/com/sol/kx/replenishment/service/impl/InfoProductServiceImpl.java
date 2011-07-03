package com.sol.kx.replenishment.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sol.kx.replenishment.dao.InfoCategoryDao;
import com.sol.kx.replenishment.dao.InfoProductDao;
import com.sol.kx.replenishment.dao.pojo.InfoCategory;
import com.sol.kx.replenishment.dao.pojo.InfoProduct;
import com.sol.kx.replenishment.service.InfoProductService;
import com.sol.kx.replenishment.service.bean.EnvironmentFactory;
import com.sol.kx.replenishment.service.bean.PageResult;

@Service
public class InfoProductServiceImpl implements InfoProductService{
	
	@Autowired
	private InfoCategoryDao infoCategoryDao;
	
	@Autowired
	private InfoProductDao infoProductDao;
	
	@Autowired
	private EnvironmentFactory environmentFactory;
	
	@Transactional(readOnly = true)
	public List<InfoCategory> findCategory(int level) {
		return infoCategoryDao.findCategory(level);
	}
	
	@Transactional(readOnly = true)
	public PageResult<InfoProduct> findProduct(int page,Map<String,Object> map) {
		PageResult<InfoProduct> result = new PageResult<InfoProduct>(page,environmentFactory.getSysEnvironment().getPageSize());
		infoProductDao.findProduct(result, map);
		return result;
	}
	
	public List<InfoProduct> findProduct(Map<String, Object> map) {
		return infoProductDao.findProduct(map);
	}
}
