package com.sol.kx.web.service.info;

import org.sol.util.mybatis.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.kx.web.dao.info.InfoShopMapper;
import com.sol.kx.web.service.BaseService;

@Service
public class InfoShopService extends BaseService{

	@Autowired
	private InfoShopMapper infoShopMapper;
	
	@Override
	protected BaseMapper injectMapper() {
		return infoShopMapper;
	}

}
