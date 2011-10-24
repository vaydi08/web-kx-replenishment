package com.sol.kx.web.service;

import java.io.File;
import java.util.List;

import com.sol.kx.web.dao.pojo.CargoCompare;
import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.bean.PagerBean_Cargo;
import com.sol.kx.web.service.util.PoiUtil;

public interface CompareService extends BaseService<Compare>{
	public PagerBean<Compare> compareSupply(File uploadFile,int shopid,int stocktype);
	public PoiUtil exportDownloadSupply(List<Compare> list);
	
	public PagerBean_Cargo compareCargo(File cargoSupplyFile,File cargoSaleFile,File cargoStockFile,int stocktype,Integer minallot);
	public PoiUtil exportDownloadCargo(PagerBean<CargoCompare> cargoBean);
}