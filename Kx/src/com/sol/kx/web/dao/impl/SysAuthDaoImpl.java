package com.sol.kx.web.dao.impl;

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
}
