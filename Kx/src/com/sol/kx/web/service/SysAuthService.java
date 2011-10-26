package com.sol.kx.web.service;

import com.sol.kx.web.dao.pojo.SysAuth;
import com.sol.kx.web.dao.pojo.SysUser;
import com.sol.kx.web.service.bean.ResultBean;

public interface SysAuthService extends BaseService<SysAuth>{
	public boolean checkAuth(String uri,SysUser user);
	
	public ResultBean reloadAuthConfig();
}
