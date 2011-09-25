package com.sol.kx.web.action.info;

import java.util.List;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.sol.util.c3p0.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.dao.pojo.InfoProductDetail;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.InfoProductService;

@Controller
@Scope("session")
@Results({@Result(name = "productDetail",location = "/info/ProductDetail.jsp")})
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

}
