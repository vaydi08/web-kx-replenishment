package com.sol.kx.web.dao.impl;

import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.sol.kx.web.dao.SysUserDao;
import com.sol.kx.web.dao.pojo.SysUser;

@Repository
public class SysUserDaoImpl extends BaseDaoImpl implements SysUserDao{
	@Value("${sql.sys.user.find}")
	private String SQL_FIND;
	
	public List<SysUser> findUsers(int page,int pageSize) throws Exception {
		StringBuilder sb = new StringBuilder();

		sb.append("select * from(select top ").append(pageSize).append(" * From (");
		sb.append("select top ").append(page * pageSize).append(SQL_FIND.substring(6)).append(") find1 order by id asc) find2 order by id desc");

		return dataConsole.find(sb.toString(),SysUser.class,
				dataConsole.parseSmap(SysUser.class, "id","username","password","shorttel","groupid","groupname","description"),
				null);
	}
	
	@Value("${sql.sys.user.findcount}")
	private String SQL_FINDCOUNT;
	
	public int findUsersCount() throws SQLException {
		return (Integer)dataConsole.findReturn(SQL_FINDCOUNT, Types.INTEGER, null);
	}
}
