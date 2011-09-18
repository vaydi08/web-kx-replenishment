package com.sol.kx.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.InfoShopDao;
import com.sol.kx.web.dao.pojo.InfoShop;
import com.sol.kx.web.service.InfoShopService;

@Service
public class InfoShopServiceImpl extends BaseServiceImpl<InfoShop> implements InfoShopService{

	public Map<String,Integer> findShopChoose() {
		Map<String,Integer> map = new HashMap<String, Integer>();
		
		List<InfoShop> list = null;
		try {
			list = infoShopDao.findShopChoose();
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询[Info_Shop]映射产生错误", e);
			return map;
		}
		
		for(InfoShop po : list) {
			String name = po.getName();
			Integer id = po.getId();
			
			map.put(name, id);
		}
		
		return map;
	}
	
	@Autowired
	private InfoShopDao infoShopDao;
	
	@Override
	protected BaseDao getDao() {
		return infoShopDao;
	}

}
