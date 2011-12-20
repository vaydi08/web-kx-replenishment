package com.sol.kx.web.service;

import java.io.File;
import java.util.List;

import com.sol.kx.web.action.compare.CargoBean;
import com.sol.kx.web.action.compare.SupplyBean;
import com.sol.kx.web.dao.pojo.CargoCompare;
import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.bean.PagerBean_Cargo;
import com.sol.kx.web.service.util.PoiUtil;

public interface CompareService extends BaseService<Compare>{
	public SupplyBean compareSupply(File uploadFile,int shopid);
	public PoiUtil exportDownloadSupply(SupplyBean bean);
	
	public CargoBean compareCargo(File cargoSupplyFile,File cargoSaleFile,File cargoStockFile,Integer minallot);
	public PoiUtil exportDownloadCargo(PagerBean<CargoCompare> cargoBean);
}