package com.sol.kx.web.action.compare;

import java.io.File;
import java.util.List;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.CompareService;

@Results({@Result(name = "compare",location = "/compare/compare.jsp")})
public class CompareAction extends BaseAction<Compare>{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private CompareService compareService;
	
	private File supplyFile;
	private int shopid;
	private int stocktype;
	
	private List<Compare> supplyList;
	
	public String uploadSupply() {
		supplyList = compareService.compareSupply(supplyFile, shopid,stocktype);
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

	public List<Compare> getSupplyList() {
		return supplyList;
	}

}
