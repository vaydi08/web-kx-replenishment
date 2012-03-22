package com.sol.kx.web.action.compare;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.CompareExcelProperty;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.compare.CompareExcelService;

@Controller
@Scope("session")
public class CompareExcelPropertyAction extends BaseAction<CompareExcelProperty>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private CompareExcelService compareExcelService;
	
	public String loadProperty() {
		compareExcelService.selectProperty(pagerBean, input);
		return JSONDATA;
	}
	
	@Override
	protected CompareExcelProperty newPojo() {
		return new CompareExcelProperty();
	}

	@Override
	protected BaseService injectService() {
		return compareExcelService;
	}

}
