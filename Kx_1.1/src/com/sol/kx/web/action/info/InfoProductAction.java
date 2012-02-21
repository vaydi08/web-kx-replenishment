package com.sol.kx.web.action.info;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.info.InfoProductService;

@Controller
@Scope("session")
public class InfoProductAction extends BaseAction<InfoProduct> {

	private static final long serialVersionUID = 1L;

	@Autowired
	private InfoProductService infoProductService;

	public String select() {
		InfoProduct param = defaultQuery();
		infoProductService.select(pagerBean, param);
		return JSONDATA;
	}

	private InputStream exportFile;

	public String printPreview() {
		InfoProduct param = defaultQuery();
		infoProductService.printPreview(pagerBean,param);

		return JSONDATA;
	}

	@Override
	protected InfoProduct newPojo() {
		return new InfoProduct();
	}

	@Override
	protected BaseService injectService() {
		return infoProductService;
	}

	public InputStream getExportFile() {
		return exportFile;
	}

}
