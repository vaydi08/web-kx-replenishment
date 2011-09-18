package com.sol.kx.web.action.info;

import java.util.Map;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.InfoShop;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.InfoShopService;

@Controller
@Scope("session")
@Results({@Result(name = "choose",location = "/stock/chooseData.jsp")})
public class InfoShopAction extends BaseAction<InfoShop>{

	private static final long serialVersionUID = 1L;

	public String findChoose() {
		return "choose";
	}
	public Map<String,Integer> getChoose() {
		return infoShopService.findShopChoose();
	}
	
	@Autowired
	private InfoShopService infoShopService;
	
	@Override
	protected BaseService<InfoShop> getService() {
		return infoShopService;
	}
	
	@Override
	protected InfoShop newPojo() {
		return new InfoShop();
	}
	
	public void setInfoShopService(InfoShopService infoShopService) {
		this.infoShopService = infoShopService;
	}

}
