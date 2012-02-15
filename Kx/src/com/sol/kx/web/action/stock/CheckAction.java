package com.sol.kx.web.action.stock;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.dao.pojo.StockCheck;
import com.sol.kx.web.dao.pojo.StockCheckSum;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.StockCheckService;
import com.sol.kx.web.service.bean.PagerBean;

@Controller
@Scope("session")
@Results({@Result(name = "productData",location = "/stock/productData.jsp"),
	@Result(name = "stockCheckSum",location = "/html/stock/stockCheckSum.jsp")})
public class CheckAction extends BaseAction<StockCheck>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private StockCheckService stockCheckService;
	
	private Integer shopid;
	private Integer clevel;
	
	public String productList() {
		productPagerBean = new PagerBean<InfoProduct>();
		productPagerBean.setPage(super.pagerBean.getPage());
		productPagerBean.setPageSize(super.pagerBean.getPageSize());
		productPagerBean = stockCheckService.findProducts(productPagerBean, productMap,shopid);
		return "productData";
	}
	
	private PagerBean<InfoProduct> productPagerBean;
	
	private Map<String,Integer> productMap;
	
	private void putProductMap(String name,Integer value) {
		if(productMap == null)
			productMap = new HashMap<String, Integer>(5);
		productMap.put(name, value);
	}
	
	public String manager() {
		pagerBean = stockCheckService.find2(input);
		return JSONDATA;
	}
	
	// 获取核定统计数
	private String stockCheckSum;
	
	public String stockCheckSum() {
		StockCheckSum sum = stockCheckService.findStockCheckSum(input);
		JSONObject json = new JSONObject(sum);
//		try {
//			json.put("shop_stocktype1", sum.getShop_stocktype1());
//			json.put("shop_stocktype2", sum.getShop_stocktype2());
//			json.put("shop_product_stocktype1", sum.getShop_product_stocktype1());
//			json.put("shop_product_stocktype2", sum.getShop_product_stocktype2());
//			json.put("sum_type1_stocktype1", sum.getSum_type1_stocktype1());
//			json.put("sum_type1_stocktype2", sum.getSum_type1_stocktype2());
//			json.put("sum_type2_stocktype1", sum.getSum_type2_stocktype1());
//			json.put("sum_type2_stocktype2", sum.getSum_type2_stocktype2());
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		stockCheckSum = json.toString();
		return "stockCheckSum";
	}
	
	// 核定数 复制
	public String copyCheck() {
		result = stockCheckService.copyCheck(input, clevel);
		return RESULT;
	}
	
	public String getStockCheckSum() {
		return stockCheckSum;
	}
		
	public void setType1(int type1) {
		putProductMap("type1", type1);
	}
	public void setType2(int type2) {
		putProductMap("type2", type2);
	}
	public void setType3(int type3) {
		putProductMap("type3", type3);
	}
	public void setType4(int type4) {
		putProductMap("type4", type4);
	}
	public void setShopid(int shopid) {
		this.shopid = shopid;
	}
	@Override
	protected BaseService<StockCheck> getService() {
		return stockCheckService;
	}

	@Override
	protected StockCheck newPojo() {
		return new StockCheck();
	}

	public PagerBean<InfoProduct> getProductPagerBean() {
		return productPagerBean;
	}

	public void setClevel(Integer clevel) {
		this.clevel = clevel;
	}

}
