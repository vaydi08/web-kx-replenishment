package com.sol.kx.web.action.info;

import org.sol.util.c3p0.Condition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.InfoProduct;

@Controller
@Scope("session")
public class InfoProductAction extends BaseAction<InfoProduct>{

	private static final long serialVersionUID = 1L;

	private int type1;
	private int type2;
	private int type3;
	private int type4;
	
	public String manager() {
		Condition condition = defaultCondition();
		if(type1 > 0)
			condition.addWhere("type1 = ?", type1);
		if(type2 > 0)
			condition.addWhere("type2 = ?", type2);
		if(type3 > 0)
			condition.addWhere("type3 = ?", type3);
		if(type4 > 0)
			condition.addWhere("type4 = ?", type4);
		
		pagerBean = infoProductService.findProducts(pagerBean, condition);
		return DATA;
	}
	
	public String add() {
		
		resultSuccess = false;
		resultErrMsg = "测试测试";
		return RESULT;
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
	
}
