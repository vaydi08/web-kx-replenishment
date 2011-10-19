package com.sol.kx.web.service;

import java.io.File;

import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.bean.PagerBean_Cargo;

public interface CompareService extends BaseService<Compare>{
	public PagerBean<Compare> compareSupply(File uploadFile,int shopid,int stocktype);
	
	public PagerBean_Cargo compareCargo(File cargoSupplyFile,File cargoSaleFile,File cargoStockFile,int stocktype,Integer minallot);
	
}