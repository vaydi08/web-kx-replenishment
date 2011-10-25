package com.sol.kx.web.service;

import com.sol.kx.web.dao.pojo.SysAuth;
import com.sol.kx.web.dao.pojo.SysUser;

public interface SysAuthService extends BaseService<SysAuth>{
	public boolean checkAuth(String uri,SysUser user);
}
