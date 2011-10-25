package com.sol.kx.web.dao;

import java.util.List;

import com.sol.kx.web.dao.pojo.SysAuth;

public interface SysAuthDao extends BaseDao{
	public List<SysAuth> reloadAuth() throws Exception;
}
