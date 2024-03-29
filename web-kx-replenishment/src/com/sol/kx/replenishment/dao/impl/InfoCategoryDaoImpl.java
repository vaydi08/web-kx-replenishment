package com.sol.kx.replenishment.dao.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.sol.kx.replenishment.dao.InfoCategoryDao;
import com.sol.kx.replenishment.dao.pojo.InfoCategory;

@Repository
public class InfoCategoryDaoImpl extends HibernateDaoImpl<InfoCategory> implements InfoCategoryDao{
	public List<InfoCategory> findCategory(int level) {
		return find(Restrictions.eq("clevel", level));
	}
}
