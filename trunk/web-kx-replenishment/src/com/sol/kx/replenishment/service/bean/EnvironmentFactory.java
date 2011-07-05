package com.sol.kx.replenishment.service.bean;

import org.springframework.stereotype.Service;

import com.sol.kx.replenishment.dao.pojo.SysEnvironment;

@Service
public class EnvironmentFactory {
	private SysEnvironment sysEnvironment;
	
	public EnvironmentFactory() {
		this.sysEnvironment = new SysEnvironment();
		sysEnvironment.setPageSize(40);
		sysEnvironment.setImageFolder("../upload/");
	}

	public SysEnvironment getSysEnvironment() {
		return sysEnvironment;
	}

	public void setSysEnvironment(SysEnvironment sysEnvironment) {
		this.sysEnvironment = sysEnvironment;
	}
}
