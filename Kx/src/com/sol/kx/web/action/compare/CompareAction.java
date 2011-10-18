package com.sol.kx.web.action.compare;

import java.io.File;
import java.sql.SQLException;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.CompareService;
import com.sol.kx.web.service.bean.PagerBean;

@Results({@Result(name = "compare",location = "/compare/compare.jsp"),
	@Result(name = "input",location = "/t.jsp")})
public class CompareAction extends BaseAction<Compare>{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private CompareService compareService;
	
	private File supplyFile;
	private int shopid;
	private int stocktype;
	
	private PagerBean<Compare> supplyBean;
	
	public String uploadSupply() {
		supplyBean = compareService.compareSupply(supplyFile, shopid,stocktype);
		return "compare";
	}
	
	private File cargoSupplyFile;
	private File cargoSaleFile;
	private File cargoStockFile;
	
	public String uploadCargoSupply() {
		supplyBean = compareService.compareCargo(cargoSupplyFile, cargoSaleFile, cargoStockFile, stocktype);
		return "compare";
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


}
