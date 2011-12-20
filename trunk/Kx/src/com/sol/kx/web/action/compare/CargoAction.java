package com.sol.kx.web.action.compare;

import java.io.File;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.pojo.CargoCompare;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.CompareService;

@Controller
@Scope("session")
@Results({
	@Result(name = "export",type = "stream",params = 
		{"contentType","application/octet-stream;charset=UTF-8",
		"contentDisposition","attachment;filename=\"Download-Supply.xls\"",
		"inputName","exportFile"})
})
public class CargoAction extends BaseAction<CargoCompare>{
	
	private static final long serialVersionUID = 1L;

	@Autowired
	private CompareService compareService;
	
	private Integer minallot;
	private File supplyFile;
	private File saleFile;
	private File stockFile;
	
	private CargoBean cargoBean;
	
	public String uploadCargo() {
		cargoBean = compareService.compareCargo(supplyFile, saleFile, stockFile, minallot);
		ActionContext.getContext().getSession().put(Constants.SESSION_DOWNLOAD_CARGO, cargoBean);
		return JSONDATA;
	}
	
	@Override
	public CargoBean getPagerBean() {
		return cargoBean;
	}

	@Override
	protected BaseService<CargoCompare> getService() {
		return null;
	}

	@Override
	protected CargoCompare newPojo() {
		return null;
	}

	public void setMinallot(Integer minallot) {
		this.minallot = minallot;
	}

	public void setSupplyFile(File supplyFile) {
		this.supplyFile = supplyFile;
	}

	public void setSaleFile(File saleFile) {
		this.saleFile = saleFile;
	}

	public void setStockFile(File stockFile) {
		this.stockFile = stockFile;
	}
}
