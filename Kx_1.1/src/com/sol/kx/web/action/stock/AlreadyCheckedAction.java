package com.sol.kx.web.action.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.StockChecked;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.stock.StockCheckService;

@Controller
@Scope("session")
public class AlreadyCheckedAction extends BaseAction<StockChecked>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private StockCheckService stockCheckService;
	
	public String execute() {
		stockCheckService.selectChecked(pagerBean, input);
		return JSONDATA;
	}

	@Override
	protected StockChecked newPojo() {
		return new StockChecked();
	}

	@Override
	protected BaseService injectService() {
		return stockCheckService;
	}

}
