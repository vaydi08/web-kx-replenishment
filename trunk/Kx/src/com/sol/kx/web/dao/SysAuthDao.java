package com.sol.kx.web.dao;

import java.sql.SQLException;
import java.util.List;

import com.sol.kx.web.dao.pojo.SysAuth;

public interface SysAuthDao extends BaseDao{
	public List<SysAuth> reloadAuth() throws Exception;
	
	public List<SysAuth> findAuth(Integer groupid) throws Exception;
	public int findAuthCount(Integer groupid) throws SQLException;
}
