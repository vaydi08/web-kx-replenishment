package com.sol.kx.replenishment.dao;

import java.util.List;
import java.util.Map;

import com.sol.kx.replenishment.dao.pojo.InfoProduct;
import com.sol.kx.replenishment.service.bean.PageResult;

public interface InfoProductDao extends BaseDao<InfoProduct>{
	/**
	 * 查找产品
	 * @param result
	 * @param map
	 * @return
	 */
	public void findProduct(PageResult<InfoProduct> result,Map<String,Object> map);
	
	/**
	 * 查找产品
	 * @param result
	 * @param map
	 * @return
	 */
	public List<InfoProduct> findProduct(Map<String,Object> map);
}
