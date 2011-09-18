package com.sol.kx.web.service;

import java.util.Map;

import com.sol.kx.web.dao.pojo.InfoShop;

public interface InfoShopService extends BaseService<InfoShop>{
	public Map<String,Integer> findShopChoose();
}