package com.sol.kx.web.service.sys;

import org.sol.util.mybatis.BaseMapper;
import org.sol.util.mybatis.MyBatisPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.pojo.SysUser;
import com.sol.kx.web.dao.sys.SysUserMapper;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.bean.PagerBean;

@Service
public class SysService extends BaseService{

	@Autowired
	private SysUserMapper sysUserMapper;
	
	/**
	 * 获取用户表
	 * @param bean
	 */
	@Transactional(readOnly = true,timeout = Constants.DB_READ_TIMEOUT)
	public void selectByPage(PagerBean<SysUser> bean) {
		if(Constants.DEBUG)
			Logger.SERVICE.debug("查询[sys_user]数据");
		try {
			bean.setDataList(sysUserMapper.select());
			bean.setCount(sysUserMapper.count(new SysUser()));
		} catch (Exception e) {
			bean.setException(e);
			exceptionHandler.onDatabaseException("查询错误","SysUser", e);
		}
	}
	
	/**
	 * 登录动作
	 * @param obj
	 * @return
	 */
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,timeout = Constants.DB_TIMEOUT)
	public SysUser login(SysUser obj) {
		if(Constants.DEBUG)
			Logger.SERVICE.debug("用户登录 " + obj.toString());
		
		return sysUserMapper.login(obj);
	}

	@Override
	protected BaseMapper<? extends MyBatisPojo> injectMapper() {
		return sysUserMapper;
	}
}
