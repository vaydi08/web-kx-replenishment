package com.sol.kx.replenishment.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sol.kx.replenishment.dao.InfoShopDao;
import com.sol.kx.replenishment.dao.pojo.InfoShop;

@Repository
public class InfoShopDaoImpl extends HibernateDaoImpl<InfoShop> implements InfoShopDao {

	public List<InfoShop> findShop() {
		return findAll();
	}

}
