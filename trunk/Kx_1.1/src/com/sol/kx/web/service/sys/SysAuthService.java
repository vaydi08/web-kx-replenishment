package com.sol.kx.web.service.sys;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.sol.util.mybatis.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.pojo.SysAuth;
import com.sol.kx.web.dao.pojo.SysUser;
import com.sol.kx.web.dao.sys.SysAuthMapper;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.bean.ResultBean;

@Service
public class SysAuthService extends BaseService{
	private Map<String,Boolean[]> authMap;
	
	@Autowired
	private SysAuthMapper sysAuthMapper;
	
	/**
	 * 检查当前用户的URI的权限
	 * @param uri
	 * @param user
	 * @return
	 */
	@Transactional(readOnly = true,timeout = Constants.DB_READ_TIMEOUT)
	public boolean checkAuth(String uri,SysUser user) {
		Boolean[] groupAuth = authMap.get(uri);
		if(groupAuth == null)
			return false;
		return groupAuth[user.getGroupid() - 1];
	}
	
	@PostConstruct
	public void init() {
		reloadAuthConfig();
	}
	
	/**
	 * 载入权限表
	 * @return
	 */
	@Transactional(readOnly = true,timeout = Constants.DB_READ_TIMEOUT)
	public ResultBean reloadAuthConfig() {
		if(Constants.DEBUG)
			Logger.SERVICE.debug("获取用户权限列表");
		
		List<SysAuth> list = null;
		try {
			list = sysAuthMapper.reloadAuth();
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("获取用户权限列表 出错","Sys_Auth", e);
			authMap = new HashMap<String,Boolean[]>(0);
			return ResultBean.RESULT_ERR(e.getMessage(),new Object());
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
		
		return ResultBean.RESULT_SUCCESS(new Object());
	}

	@Override
	protected BaseMapper injectMapper() {
		return sysAuthMapper;
	}
}
