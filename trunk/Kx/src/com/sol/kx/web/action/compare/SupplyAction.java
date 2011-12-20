package com.sol.kx.web.action.compare;

import java.io.File;
import java.io.InputStream;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.CompareService;
import com.sol.kx.web.service.util.PoiUtil;

@Controller
@Scope("session")
@Results({
	@Result(name = "export",type = "stream",params = 
		{"contentType","application/octet-stream;charset=UTF-8",
		"contentDisposition","attachment;filename=\"Download-Supply.xls\"",
		"inputName","exportFile"})
})
public class SupplyAction extends BaseAction<Compare>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private CompareService compareService;
	
	private File supplyFile;
	private int shopid;
	
	private SupplyBean bean;
	
	public String uploadSupply() {
		bean = compareService.compareSupply(supplyFile, shopid);
		ActionContext.getContext().getSession().put(Constants.SESSION_DOWNLOAD_SUPPLY, bean);
		return JSONDATA;
	}
	
	private InputStream exportFile;
	
	public String download() {
		SupplyBean bean= (SupplyBean) ActionContext.getContext().getSession().get(Constants.SESSION_DOWNLOAD_SUPPLY);
		PoiUtil poi = null;
		try {
			poi = compareService.exportDownloadSupply(bean);
			exportFile = poi.getExcel();
		} finally {
			if(poi != null)
				poi.close();
		}
		
		return "export";
	}
	
	@Override
	protected BaseService<Compare> getService() {
		return null;
	}

	@Override
	protected Compare newPojo() {
		return null;
	}

	public SupplyBean getPagerBean() {
		return bean;
	}

	public void setSupplyFile(File supplyFile) {
		this.supplyFile = supplyFile;
	}

	public void setShopid(int shopid) {
		this.shopid = shopid;
	}

	public InputStream getExportFile() {
		return exportFile;
	}

}
