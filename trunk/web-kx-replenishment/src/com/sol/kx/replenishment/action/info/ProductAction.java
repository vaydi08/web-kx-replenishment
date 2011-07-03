package com.sol.kx.replenishment.action.info;

import java.util.HashMap;
import java.util.List;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.replenishment.action.BaseAction;
import com.sol.kx.replenishment.dao.pojo.InfoCategory;
import com.sol.kx.replenishment.dao.pojo.InfoProduct;
import com.sol.kx.replenishment.service.InfoProductService;

@Controller
@Scope("prototype")
@Results({@Result(name = "manager",location = "manager2.jsp")})
public class ProductAction extends BaseAction<InfoProduct>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private InfoProductService infoCategoryService;
	
	private List<InfoProduct> productList;
	
	private boolean showImage;
	
	public String manager() {
		productList = infoCategoryService.findProduct(new HashMap<String,Object>(0));
		return "manager";
	}
	
	public List<InfoCategory> getCategoryLevel1() {
		return infoCategoryService.findCategory(1);
	}
	public List<InfoCategory> getCategoryLevel2() {
		return infoCategoryService.findCategory(2);
	}
	public List<InfoCategory> getCategoryLevel3() {
		return infoCategoryService.findCategory(3);
	}
	public List<InfoCategory> getCategoryLevel4() {
		return infoCategoryService.findCategory(4);
	}

	public boolean isShowImage() {
		return showImage;
	}

	public void setShowImage(boolean showImage) {
		this.showImage = showImage;
	}

	public List getProductList() {
		return productList;
	}

	public void setProductList(List productList) {
		this.productList = productList;
	}

}
