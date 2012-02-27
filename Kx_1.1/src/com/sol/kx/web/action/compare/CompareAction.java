package com.sol.kx.web.action.compare;

import java.io.File;
import java.io.InputStream;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.service.bean.CargoBean;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.compare.CompareService;

@Controller
@Scope("session")
@Results({
	@Result(name = "cargodata",location = "/html/compare/cargodata.jsp"),
	@Result(name = "export",type = "stream",params = 
		{"contentType","application/octet-stream;charset=UTF-8",
		"contentDisposition","attachment;filename=\"Download.xls\"",
		"inputName","exportFile"})
})
public class CompareAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private CompareService compareService;
	
	private PagerBean<Compare> pagerBean;
	private CargoBean cargoBean;
	
	private Integer minallot;
	private File supplyFile;
	private File saleFile;
	private File stockFile;
	private Compare supply;
	private InputStream exportFile;
	
	
	public String uploadSupply() {

		pagerBean = compareService.compareSupply(supplyFile, supply);
		compareService.removeSupplyTempTable(supply);

		ActionContext.getContext().getSession().put(Constants.SESSION_DOWNLOAD_SUPPLY, pagerBean);
		return "jsondata";
	}
	
	public String uploadCargo() {
		cargoBean = compareService.compareCargo(supplyFile, saleFile, stockFile, minallot);
		ActionContext.getContext().getSession().put(Constants.SESSION_DOWNLOAD_CARGO, cargoBean);
		return "cargodata";
	}
	
	public String downloadSupply() {
		this.exportFile = compareService.exportDownloadSupply(
				(PagerBean<Compare>)ActionContext.getContext().getSession().get(Constants.SESSION_DOWNLOAD_SUPPLY));
		return "export";
	}

	public void setSupplyFile(File supplyFile) {
		this.supplyFile = supplyFile;
	}

	public PagerBean<Compare> getPagerBean() {
		return pagerBean;
	}

	public void setSupply(Compare supply) {
		this.supply = supply;
	}

	public InputStream getExportFile() {
		return exportFile;
	}

	public void setMinallot(Integer minallot) {
		this.minallot = minallot;
	}

	public void setSaleFile(File saleFile) {
		this.saleFile = saleFile;
	}

	public void setStockFile(File stockFile) {
		this.stockFile = stockFile;
	}
}
