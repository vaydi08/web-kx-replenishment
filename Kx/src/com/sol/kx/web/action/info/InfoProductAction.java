package com.sol.kx.web.action.info;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.dao.pojo.InfoProductDetail;
import com.sol.kx.web.dao.pojo.StockCheck;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.InfoProductService;
import com.sol.kx.web.service.util.PoiUtil;

@Controller
@Scope("session")
@Results({@Result(name = "productDetail",location = "/info/ProductDetail.jsp"),
		  @Result(name = "quickLocatorStock",location = "/html/info/QuickLocatorStock.jsp"),
		  @Result(name = "panelSelect",location = "/panelSelectData.jsp"),
		  @Result(name = "export",type = "stream",params = 
			{"contentType","application/octet-stream;charset=UTF-8",
			"contentDisposition","attachment;filename=\"Goods.xls\"",
			"inputName","exportFile"})})
public class InfoProductAction extends BaseAction<InfoProduct>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private InfoProductService infoProductService;
	
	@Override
	protected BaseService<InfoProduct> getService() {
		return infoProductService;
	}
	
	@Override
	protected InfoProduct newPojo() {
		return new InfoProduct();
	}

	private String picType;
	private File picFile;
	private String picData;
	
	@Override
	public String add2() {
		if(picType.equals("webcap"))
			infoProductService.saveUploadPic(picData, input);
		else if(picType.equals("file"))
			infoProductService.saveUploadPic(picFile, input);
			
		return super.add2();
	}
	
	private String pcode;
	
	public String checkPcode() {
		result = infoProductService.checkExists(pcode);
		return RESULT;
	}
	/**
	 * 快速定位列表
	 */
	private Map<String,List<StockCheck>> quickLocatorStockList;
	
	public String quickLocator() {
		if(queryValue == null || queryValue.equals(""))
			return null;
		
		pagerBean = infoProductService.findFuzzy(pagerBean, queryValue);
		return DATA;
	}
	
	public String quickLocatorStock() {
		quickLocatorStockList = infoProductService.findQuickLocator(pid);
		return "quickLocatorStock";
	}
	
	private InputStream exportFile;
	
	public String export() {
		PoiUtil poi = null;
		try {
			poi = infoProductService.createExcel();
			exportFile = poi.getExcel();
		} catch (Exception e) {
			if(poi != null)
				poi.close();
		}
		
		return "export";
	}
	
	// 快速定位 type4列表
	public String findType4Select() {
		pagerBean = infoProductService.find2(input);
		return "panelSelect";
	}
	
	/**
	 * 子内容列表
	 */
	private Integer pid;
	private List<InfoProductDetail> detailList;
	
//	public String details() {
//		detailList = infoProductService.findProductDetails(pid);
//		return "productDetail";
//	}

	public void setPid(Integer pid) {
		this.pid = pid;
	}

	public List<InfoProductDetail> getDetailList() {
		return detailList;
	}

	public Map<String,List<StockCheck>> getQuickLocatorStockList() {
		return quickLocatorStockList;
	}

	public InputStream getExportFile() {
		return exportFile;
	}

	public void setPicData(String picData) {
		this.picData = picData;
	}

	public void setPicType(String picType) {
		this.picType = picType;
	}

	public void setPicFile(File picFile) {
		this.picFile = picFile;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

}
