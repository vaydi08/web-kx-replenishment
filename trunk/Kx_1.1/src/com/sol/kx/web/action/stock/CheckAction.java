package com.sol.kx.web.action.stock;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.StockCheck;
import com.sol.kx.web.dao.pojo.StockCheckSum;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.stock.StockCheckService;

@Controller
@Scope("session")
@Results({@Result(name = "productData",location = "/stock/productData.jsp"),
	@Result(name = "stockCheckSum",location = "/html/stock/stockCheckSum.jsp")})
public class CheckAction extends BaseAction<StockCheck>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private StockCheckService stockCheckService;
	
	// 获取核定统计数
	private String stockCheckSum;
	
	public String stockCheckSum() {
		StockCheckSum sum = stockCheckService.stockCheckSum(input);
		JSONObject json = new JSONObject(sum);
		stockCheckSum = json.toString();
		return "stockCheckSum";
	}
	
	// 核定数 复制
	public String copyCheck() {
		result = stockCheckService.copyCheck(input);
		return RESULT;
	}
	
	@Override
	protected StockCheck newPojo() {
		return new StockCheck();
	}

	@Override
	protected BaseService injectService() {
		return stockCheckService;
	}

	public String getStockCheckSum() {
		return stockCheckSum;
	}

}
