package com.sol.kx.replenishment.action.info;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.replenishment.action.BaseAction;
import com.sol.kx.replenishment.dao.pojo.InfoShop;
import com.sol.kx.replenishment.service.InfoProductService;

@Controller
@Scope("prototype")
@Results({@Result(name = BaseAction.MANAGER,location = "shopmanager.jsp"),
		  @Result(name = BaseAction.PRENEW,location = "shop.jsp")})
public class ShopAction extends BaseAction<InfoShop>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private InfoProductService infoProductService;
	
	public String manager() {
		list = infoProductService.findShop();
		return MANAGER;
	}
	
	public String preNew() {
		input = new InfoShop();
		return PRENEW;
	}
}
