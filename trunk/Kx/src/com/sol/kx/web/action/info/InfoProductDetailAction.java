package com.sol.kx.web.action.info;

import org.springframework.beans.factory.annotation.Autowired;

import com.sol.kx.web.action.BaseAction;
import com.sol.kx.web.dao.pojo.InfoProductDetail;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.InfoProductService;

public class InfoProductDetailAction extends BaseAction<InfoProductDetail>{

	private static final long serialVersionUID = 1L;

	@Autowired
	private InfoProductService infoProductService;
	
	private String picData;
	
	@Override
	public String add2() {
		infoProductService.saveUploadPic(picData, input);
		result = infoProductService.addProductDetail(input);
		return RESULT;
	}
	
	@Override
	protected BaseService<InfoProductDetail> getService() {
		return null;
	}

	@Override
	protected InfoProductDetail newPojo() {
		return new InfoProductDetail();
	}

	public void setPicData(String picData) {
		this.picData = picData;
	}

}
