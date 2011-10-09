package com.sol.kx.web.dao;

import java.sql.SQLException;
import java.util.List;

import com.sol.kx.web.dao.pojo.SysUser;

public interface SysUserDao extends BaseDao {
	public List<SysUser> findUsers(int page,int pageSize) throws Exception;
	public int findUsersCount() throws SQLException;
	
	public SysUser login(String username,String password) throws Exception;
}
