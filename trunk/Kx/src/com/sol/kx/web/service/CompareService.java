package com.sol.kx.web.service;

import java.io.File;
import com.sol.kx.web.action.compare.CargoBean;
import com.sol.kx.web.dao.pojo.CargoCompare;
import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.util.PoiUtil;

public interface CompareService extends BaseService<Compare>{
	public PagerBean<Compare> compareSupply(File uploadFile,int shopid);
	public PoiUtil exportDownloadSupply(PagerBean<Compare> bean);
	
	public CargoBean compareCargo(File cargoSupplyFile,File cargoSaleFile,File cargoStockFile,Integer minallot);
	public PoiUtil exportDownloadCargo(PagerBean<CargoCompare> cargoBean);
}