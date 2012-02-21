package com.sol.kx.web.dao.info;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;
import org.sol.util.mybatis.BaseMapper;
import org.sol.util.mybatis.SelectTemplate;

import com.sol.kx.web.dao.pojo.InfoShop;

public interface InfoShopMapper extends BaseMapper<InfoShop>{
	
	@SelectProvider(type = SelectTemplate.class,method = "select")
	public List<InfoShop> select(InfoShop obj);
}
