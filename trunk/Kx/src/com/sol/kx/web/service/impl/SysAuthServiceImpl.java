package com.sol.kx.web.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.SysAuthDao;
import com.sol.kx.web.dao.pojo.SysAuth;
import com.sol.kx.web.dao.pojo.SysUser;
import com.sol.kx.web.service.SysAuthService;

@Service
public class SysAuthServiceImpl extends BaseServiceImpl<SysAuth> implements SysAuthService{

	private Map<String,Boolean[]> authMap;
	
	@Autowired
	private SysAuthDao sysAuthDao;
	
	public boolean checkAuth(String uri,SysUser user) {
		Boolean[] groupAuth = authMap.get(uri);
		if(groupAuth == null)
			return true;
		return groupAuth[user.getGroupid() - 1];
	}
	
	@PostConstruct
	public void init() {
		Logger.SERVICE.debug("获取用户权限列表");
		List<SysAuth> list = null;
		try {
			list = sysAuthDao.reloadAuth();
		} catch (Exception e) {
			Logger.SERVICE.debug("获取用户权限列表 出错");
			authMap = new HashMap<String,Boolean[]>(0);
			return;
		}
		
		authMap = new HashMap<String,Boolean[]>(20);
		for(SysAuth auth : list) {
			Boolean[] groupAuth = authMap.get(auth.getUri());
			if(groupAuth == null) {
				groupAuth = new Boolean[]{false,false,false,false};
				authMap.put(auth.getUri(), groupAuth);
			}
			
			groupAuth[auth.getGroupid() - 1] = (auth.getStatus() == 1);
		}
		
		Logger.SERVICE.linfo("用户鉴权情况:",authMap.toString());
	}
	
	@Override
	protected BaseDao getDao() {
		return sysAuthDao;
	}

}
