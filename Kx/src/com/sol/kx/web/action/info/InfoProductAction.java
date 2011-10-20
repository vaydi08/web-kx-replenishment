package com.sol.kx.web.action.info;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.sol.util.c3p0.Condition;
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
		  @Result(name = "quickLocatorStock",location = "/info/QuickLocatorStock.jsp"),
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
	
	private int type1;
	private int type2;
	private int type3;
	private int type4;

	
	/**
	 * 列表
	 * @return
	 */
	@Override
	public String manager() {
		Condition condition = defaultCondition();

		if(type1 > 0) {
			if(condition == null)
				condition = new Condition();
			condition.addWhere("type1 = ?", type1);
		}
			
		if(type2 > 0) {
			if(condition == null)
				condition = new Condition();
			condition.addWhere("type2 = ?", type2);
		}
		if(type3 > 0) {
			if(condition == null)
				condition = new Condition();
			condition.addWhere("type3 = ?", type3);
		}
		if(type4 > 0) {
			if(condition == null)
				condition = new Condition();
			condition.addWhere("type4 = ?", type4);
		}
		
		pagerBean = infoProductService.find(pagerBean, condition);
		return DATA;
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
		PoiUtil poi = infoProductService.createExcel();
		exportFile = poi.getExcel();
		return "export";
	}
	
	/**
	 * 子内容列表
	 */
	private Integer pid;
	private List<InfoProductDetail> detailList;
	
	public String details() {
		detailList = infoProductService.findProductDetails(pid);
		return "productDetail";
	}

	public void setType1(int type1) {
		this.type1 = type1;
	}

	public void setType2(int type2) {
		this.type2 = type2;
	}

	public void setType3(int type3) {
		this.type3 = type3;
	}

	public void setType4(int type4) {
		this.type4 = type4;
	}

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

}
