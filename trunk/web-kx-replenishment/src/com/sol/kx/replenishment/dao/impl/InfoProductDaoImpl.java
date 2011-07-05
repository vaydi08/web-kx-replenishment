package com.sol.kx.replenishment.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import com.sol.kx.replenishment.dao.InfoProductDao;
import com.sol.kx.replenishment.dao.pojo.InfoProduct;
import com.sol.kx.replenishment.service.bean.PageResult;

@Repository
public class InfoProductDaoImpl extends HibernateDaoImpl<InfoProduct> implements InfoProductDao{
	@SuppressWarnings("unchecked")
	public void findProduct(PageResult<InfoProduct> result,Map<String,Object> map) {
		DetachedCriteria dc = createCriterion(map);
		result.setRowCount(super.findCount(dc));
		dc.setProjection(null);
		result.setResult(super.findByCriteria(dc, (result.getPage() - 1) * result.getPageSize(), result.getPageSize()));
	}
	
	@SuppressWarnings("unchecked")
	public List<InfoProduct> findProduct(Map<String, Object> map) {
		return findByCriteria(createCriterion(map));
	}

}
