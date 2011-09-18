package com.sol.kx.web.dao;

import java.util.List;

import com.sol.kx.web.dao.pojo.InfoShop;

public interface InfoShopDao extends BaseDao{
	/**
	 * 门店 核定第一页选取
	 * @return
	 * @throws Exception
	 */
	public List<InfoShop> findShopChoose() throws Exception;
}