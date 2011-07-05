package com.sol.kx.replenishment.service;

import java.util.List;
import java.util.Map;

import com.sol.kx.replenishment.dao.pojo.InfoCategory;
import com.sol.kx.replenishment.dao.pojo.InfoProduct;
import com.sol.kx.replenishment.dao.pojo.InfoShop;
import com.sol.kx.replenishment.service.bean.PageResult;

public interface InfoProductService {
	/**
	 * 查找产品目录
	 * @param level
	 * @return
	 */
	public List<InfoCategory> findCategory(int level);
	
	/**
	 * 查找产品
	 * @param page
	 * @param map
	 * @return
	 */
	public PageResult<InfoProduct> findProduct(int page,Map<String,Object> map);
	
	/**
	 * 查找产品
	 * @param page
	 * @param map
	 * @return
	 */
	public List<InfoProduct> findProduct(Map<String,Object> map);
	
	/**
	 * 查找门店
	 * @return
	 */
	public List<InfoShop> findShop();
}
