package com.sol.kx.web.dao.sys;

import java.util.List;

import org.sol.util.mybatis.BaseMapper;
import com.sol.kx.web.dao.pojo.SysUser;

public interface SysUserMapper extends BaseMapper<SysUser>{

	public List<SysUser> select();
	
	public SysUser login(SysUser obj);
}
