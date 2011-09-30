package com.sol.kx.web.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.dao.pojo.SysUser;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.SysService;

@Controller
@Scope("session")
public class SysAction extends BaseAction<SysUser> {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private SysService sysService;
	
	@Override
	public String manager() {
		pagerBean = sysService.findUsers(pagerBean);
		return DATA;
	}
	
	@Override
	protected BaseService<SysUser> getService() {
		return sysService;
	}

	@Override
	protected SysUser newPojo() {
		return new SysUser();
	}

}
