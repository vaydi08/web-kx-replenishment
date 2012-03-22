package com.sol.kx.web.action.sys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.SysAuth;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.sys.SysAuthService;

@Controller
@Scope("session")
public class SysAuthAction extends BaseAction<SysAuth>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private SysAuthService sysAuthService;
	
	public String reloadAuthConfig() {
		result = sysAuthService.reloadAuthConfig();
		return RESULT;
	}
	
	public String manager() {
		sysAuthService.select(pagerBean, input);
		return JSONDATA;
	}
	
	@Override
	protected SysAuth newPojo() {
		return new SysAuth();
	}

	@Override
	protected BaseService injectService() {
		return sysAuthService;
	}

}