package com.sol.kx.web.action.compare;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionContext;
import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.pojo.CargoCompare;
import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.CompareService;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.util.PoiUtil;

@Results({@Result(name = "compare",location = "/compare/compare.jsp"),
	@Result(name = "cargoCompare",location = "/compare/cargoCompare.jsp"),
	@Result(name = "input",location = "/t.jsp"),
	@Result(name = "export",type = "stream",params = 
		{"contentType","application/octet-stream;charset=UTF-8",
		"contentDisposition","attachment;filename=\"Download.xls\"",
		"inputName","exportFile"})})
public class CompareAction extends BaseAction<Compare>{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private CompareService compareService;
	
	private File supplyFile;
	private int shopid;
	private int stocktype;
	
	private PagerBean<Compare> supplyBean;
	private PagerBean<CargoCompare> cargoBean;
	
	public String uploadSupply() {
		supplyBean = compareService.compareSupply(supplyFile, shopid,stocktype);
		ActionContext.getContext().getSession().put(Constants.SESSION_DOWNLOAD_SUPPLY, supplyBean.getDataList());
		return "compare";
	}
	
	private InputStream exportFile;
	
	public String downloadSupply() {
		List<Compare> list = (List<Compare>) ActionContext.getContext().getSession().get(Constants.SESSION_DOWNLOAD_SUPPLY);
		PoiUtil poi = null;
		try {
			poi = compareService.exportDownloadSupply(list);
			exportFile = poi.getExcel();
		} finally {
			if(poi != null)
				poi.close();
		}
		
		return "export";
	}
	
	private Integer minallot;
	private File cargoSupplyFile;
	private File cargoSaleFile;
	private File cargoStockFile;
	
	public String uploadCargoSupply() {
		cargoBean = compareService.compareCargo(cargoSupplyFile, cargoSaleFile, cargoStockFile, stocktype,minallot);
		ActionContext.getContext().getSession().put(Constants.SESSION_DOWNLOAD_CARGO, cargoBean);
		return "cargoCompare";
	}
	
	public String downloadCargo() {
		PagerBean<CargoCompare> cargoBean = (PagerBean<CargoCompare>) ActionContext.getContext().getSession().get(Constants.SESSION_DOWNLOAD_CARGO);
		PoiUtil poi = null;
		try {
			poi = compareService.exportDownloadCargo(cargoBean);
			exportFile = poi.getExcel();
		} finally {
			if(poi != null)
				poi.close();
		}
		
		return "export";
	}
	
	@Override
	protected BaseService<Compare> getService() {
		return compareService;
	}

	@Override
	protected Compare newPojo() {
		return new Compare();
	}

	public void setSupplyFile(File supplyFile) {
		this.supplyFile = supplyFile;
	}

	public void setShopid(int shopid) {
		this.shopid = shopid;
	}

	public void setStocktype(int stocktype) {
		this.stocktype = stocktype;
	}

	public PagerBean<Compare> getSupplyBean() {
		return supplyBean;
	}

	public void setCargoSupplyFile(File cargoSupplyFile) {
		this.cargoSupplyFile = cargoSupplyFile;
	}

	public void setCargoSaleFile(File cargoSaleFile) {
		this.cargoSaleFile = cargoSaleFile;
	}

	public void setCargoStockFile(File cargoStockFile) {
		this.cargoStockFile = cargoStockFile;
	}

	public PagerBean<CargoCompare> getCargoBean() {
		return cargoBean;
	}

	public void setMinallot(Integer minallot) {
		this.minallot = minallot;
	}

	public InputStream getExportFile() {
		return exportFile;
	}


}
