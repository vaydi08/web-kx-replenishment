package com.sol.kx.web.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.dao.pojo.InfoProductDetail;
import com.sol.kx.web.dao.pojo.StockCheck;
import com.sol.kx.web.service.bean.ImportResultBean;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.bean.ResultBean;
import com.sol.kx.web.service.util.PoiUtil;

public interface InfoProductService extends BaseService<InfoProduct>{
	public ResultBean checkExists(String pcode);
	
	public PagerBean<InfoProduct> findFuzzy(PagerBean<InfoProduct> bean,String value);
	
	public List<InfoProductDetail> findProductDetails(Integer pid);
	
	public ImportResultBean[] importExcel(File[] files);
	
	public Map<String,List<StockCheck>> findQuickLocator(Integer pid);
	
	public PoiUtil createExcel();
	
	public ResultBean addProductDetail(InfoProductDetail infoProductDetail);
	public String saveUploadPic(String picData,InfoProduct input);
	public String saveUploadPic(File img,InfoProduct input);
}