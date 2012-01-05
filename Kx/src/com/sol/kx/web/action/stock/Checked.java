package com.sol.kx.web.action.stock;

import org.springframework.beans.factory.annotation.Autowired;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.StockChecked;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.StockCheckService;

public class Checked extends BaseAction<StockChecked>{

	private static final long serialVersionUID = 1L;

	private Integer shopid;
	private Integer parent;
	private Integer clevel;
	private Integer parentlevel;
	
	@Autowired
	private StockCheckService stockCheckService;
	
	public String type1() {
		if(shopid == null)
			return JSONDATA;
		
		pagerBean = stockCheckService.findCheckedType1(shopid);
		return JSONDATA;
	}
	
	public String type234() {
		if(shopid < 0)
			return JSONDATA;
		
		pagerBean = stockCheckService.findCheckedType234(shopid,parent,clevel,parentlevel);
		return JSONDATA;
	}
	
	@Override
	protected BaseService<StockChecked> getService() {
		return null;
	}

	@Override
	protected StockChecked newPojo() {
		return null;
	}

	public Integer getShopid() {
		return shopid;
	}

	public void setShopid(Integer shopid) {
		this.shopid = shopid;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public Integer getClevel() {
		return clevel;
	}

	public void setClevel(Integer clevel) {
		this.clevel = clevel;
	}

	public Integer getParentlevel() {
		return parentlevel;
	}

	public void setParentlevel(Integer parentlevel) {
		this.parentlevel = parentlevel;
	}

}
