package com.sol.kx.web.dao.sys;

import java.util.List;

import org.sol.util.mybatis.BaseMapper;
import com.sol.kx.web.dao.pojo.SysAuth;

public interface SysAuthMapper extends BaseMapper<SysAuth>{

	public List<SysAuth> reloadAuth();
	
	public List<SysAuth> select(SysAuth sysAuth);
}
