package com.sol.kx.replenishment.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import com.sol.kx.replenishment.dao.pojo.InfoTessera;

public interface InfoTesseraDao extends BaseDao<InfoTessera>{
	public List<InfoTessera> findByCriteria(DetachedCriteria detachedCriteria);
}
