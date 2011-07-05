package com.sol.kx.replenishment.dao;

import java.util.List;

import com.sol.kx.replenishment.dao.pojo.InfoShop;

public interface InfoShopDao extends BaseDao<InfoShop>{
	public List<InfoShop> findShop();
}
