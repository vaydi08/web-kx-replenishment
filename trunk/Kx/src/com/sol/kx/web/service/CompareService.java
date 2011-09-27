package com.sol.kx.web.service;

import java.io.File;
import java.util.List;

import com.sol.kx.web.dao.pojo.Compare;

public interface CompareService extends BaseService<Compare>{
	public List<Compare> compareSupply(File uploadFile,int shopid,int stocktype);
}