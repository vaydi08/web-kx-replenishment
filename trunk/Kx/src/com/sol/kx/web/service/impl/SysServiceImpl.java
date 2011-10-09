package com.sol.kx.web.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.SysUserDao;
import com.sol.kx.web.dao.pojo.SysUser;
import com.sol.kx.web.service.SysService;
import com.sol.kx.web.service.bean.PagerBean;

@Service
public class SysServiceImpl extends BaseServiceImpl<SysUser> implements SysService{

	@Autowired
	private SysUserDao sysUserDao;
	
	public PagerBean<SysUser> findUsers(PagerBean<SysUser> bean) {
		Logger.SERVICE.ldebug("查询[sys_user]数据",bean.getPage(),bean.getPageSize());
		try {
			return setBeanValue(bean, 
					sysUserDao.findUsers(bean.getPage(), bean.getPageSize()), 
					sysUserDao.findUsersCount());
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询sys_user错误", e);
			bean.setException(e);
			return bean;
		}
	}
	
	public SysUser login(SysUser input) {
		Logger.SERVICE.ldebug("用户登录[sys_user]", input.getUsername(),input.getPassword());
		try {
			return sysUserDao.login(input.getUsername(), input.getPassword());
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("用户登录[sys_user]错误", e);
			return null;
		}
	}
	
	@Override
	protected BaseDao getDao() {
		return sysUserDao;
	}

}
