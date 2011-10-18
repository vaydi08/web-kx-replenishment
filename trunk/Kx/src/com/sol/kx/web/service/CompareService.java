package com.sol.kx.web.service;

import java.io.File;

import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.service.bean.PagerBean;

public interface CompareService extends BaseService<Compare>{
	public PagerBean<Compare> compareSupply(File uploadFile,int shopid,int stocktype);
	
	public PagerBean<Compare> compareCargo(File cargoSupplyFile,File cargoSaleFile,File cargoStockFile,int stocktype);
	
}