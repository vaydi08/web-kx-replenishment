package com.sol.kx.web.action.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.InfoShop;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.bean.ComboBoxBean;
import com.sol.kx.web.service.info.InfoShopService;

@Controller
@Scope("session")
public class InfoShopAction extends BaseAction<InfoShop>{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private InfoShopService infoShopService;
	
	public String shopCombo() {
		comboboxBean = new ComboBoxBean();
		infoShopService.select(pagerBean, input);
		for(InfoShop shop : pagerBean.getDataList())
			comboboxBean.addData(shop.getName(), shop.getId());
		comboboxBean.setFirstSelect();
		
		return COMBOBOX;
	}
	
	@Override
	protected InfoShop newPojo() {
		return new InfoShop();
	}

	@Override
	protected BaseService injectService() {
		return infoShopService;
	}

}
