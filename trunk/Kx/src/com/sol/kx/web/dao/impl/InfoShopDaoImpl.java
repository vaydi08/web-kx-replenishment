package com.sol.kx.web.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sol.util.c3p0.DataConsoleAnnotation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.InfoShopDao;
import com.sol.kx.web.dao.pojo.InfoShop;

@Repository
public class InfoShopDaoImpl extends BaseDaoImpl implements InfoShopDao{
	@Value("${sql.info.shop.find.choose}")
	private String SQL_SHOP_FIND_CHOOSE;
	
	public List<InfoShop> findShopChoose() throws Exception {
		DataConsoleAnnotation dataConsole = new DataConsoleAnnotation(Constants.DB, Constants.TRANS_TIMEOUT);
		Map<String,Class<?>> smap = new HashMap<String, Class<?>>(2);
		smap.put("id",Integer.class);
		smap.put("name",String.class);
		
		List<Object> list = null;
		return dataConsole.find(SQL_SHOP_FIND_CHOOSE,InfoShop.class, smap, list);
	}
}
