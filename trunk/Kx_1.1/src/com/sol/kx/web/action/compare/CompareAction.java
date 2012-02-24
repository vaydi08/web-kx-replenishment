package com.sol.kx.web.action.compare;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.compare.CompareService;

@Controller
@Scope("session")
public class CompareAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private CompareService compareService;
	
	private PagerBean<Compare> pagerBean;
	
	private File supplyFile;
	private Compare supply;
	
	public String uploadSupply() {

		pagerBean = compareService.compareSupply(supplyFile, supply);
		compareService.removeSupplyTempTable(supply);

//		ActionContext.getContext().getSession().put(Constants.SESSION_DOWNLOAD_SUPPLY, pagerBean);
		return "jsondata";
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
}
