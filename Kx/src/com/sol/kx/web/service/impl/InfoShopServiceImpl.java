package com.sol.kx.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.InfoShopDao;
import com.sol.kx.web.dao.pojo.InfoShop;
import com.sol.kx.web.service.InfoShopService;

@Service
public class InfoShopServiceImpl extends BaseServiceImpl<InfoShop> implements InfoShopService{

	@Autowired
	private InfoShopDao infoShopDao;
	
	@Override
	protected BaseDao getDao() {
		return infoShopDao;
	}

}
