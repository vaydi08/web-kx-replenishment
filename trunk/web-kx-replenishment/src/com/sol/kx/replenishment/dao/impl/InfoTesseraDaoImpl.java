package com.sol.kx.replenishment.dao.impl;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import com.sol.kx.replenishment.dao.InfoTesseraDao;
import com.sol.kx.replenishment.dao.pojo.InfoTessera;

@Repository
public class InfoTesseraDaoImpl extends HibernateDaoImpl<InfoTessera> implements InfoTesseraDao{
	@SuppressWarnings("unchecked")
	@Override
	public List<InfoTessera> findByCriteria(DetachedCriteria detachedCriteria) {
		return super.findByCriteria(detachedCriteria);
	}
}
