package com.sol.kx.web.service;

import com.sol.kx.web.dao.pojo.SysUser;
import com.sol.kx.web.service.bean.PagerBean;

public interface SysService extends BaseService<SysUser>{
	public PagerBean<SysUser> findUsers(PagerBean<SysUser> bean);
	
	public SysUser login(SysUser input);
}
