package com.sol.kx.web.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.sol.kx.web.dao.SysAuthDao;
import com.sol.kx.web.dao.pojo.SysAuth;

@Repository
public class SysAuthDaoImpl extends BaseDaoImpl implements SysAuthDao{
	@Value("${sql.sys.auth.check}")
	private String SQL_CHECKAUTH;
	
	public List<SysAuth> reloadAuth() throws Exception {
		return dataConsole.find(SQL_CHECKAUTH, SysAuth.class, dataConsole.parseSmap(SysAuth.class, 
				"id","uri","uriname","groupdesc","groupid","status"), null);
	}
	
	@Value("${sql.sys.auth.find}")
	private String SQL_FIND;
	
	public List<SysAuth> findAuth(Integer groupid) throws Exception {
		List<Object> list = new ArrayList<Object>(1);
		list.add(groupid);
		return dataConsole.find(SQL_FIND, SysAuth.class,dataConsole.parseSmap(SysAuth.class, 
				"id","status","uri","description"),list);
	}
	
	@Value("${sql.sys.auth.findcount}")
	private String SQL_FINDCOUNT;
	
	public int findAuthCount(Integer groupid) throws SQLException {
		return findReturnInt(SQL_FINDCOUNT, groupid);
	}
}
