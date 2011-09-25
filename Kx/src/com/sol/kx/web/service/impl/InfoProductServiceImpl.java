package com.sol.kx.web.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.sol.util.c3p0.Condition;
import org.sol.util.c3p0.dataEntity2.Criteria;
import org.sol.util.c3p0.dataEntity2.InsertEntity;
import org.sol.util.c3p0.dataEntity2.SelectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.InfoProductDao;
import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.dao.pojo.InfoProductDetail;
import com.sol.kx.web.service.InfoCategoryService;
import com.sol.kx.web.service.InfoProductService;
import com.sol.kx.web.service.bean.ImportResultBean;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.util.PoiUtil;

@Service
public class InfoProductServiceImpl extends BaseServiceImpl<InfoProduct> implements InfoProductService{
	
	@Autowired
	private InfoProductDao infoProductDao;
	
	public PagerBean<InfoProduct> find(PagerBean<InfoProduct> bean,Condition condition) {		
		Logger.SERVICE.ldebug("查询[info_product]数据",bean.getPage(),bean.getPageSize(),
				(condition != null ? Arrays.deepToString(condition.getParams().toArray()) : ""));
		try {
			return setBeanValue(bean, 
					infoProductDao.find(bean.getPage(), bean.getPageSize(), condition),
					infoProductDao.findCount(condition));
		} catch (Exception e) {
			bean.setException(e);
			exceptionHandler.onDatabaseException("查询[info_product]时的错误", e);
			return bean;
		}
	}
	
	public List<InfoProductDetail> findProductDetails(Integer pid) {
		Logger.SERVICE.ldebug("查询[info_product_detail]数据",pid);
		
		SelectEntity entity = new SelectEntity();
		Criteria criteria = new Criteria();
		criteria.eq("pid", pid);
		entity.init(InfoProductDetail.class, criteria);
		
		try {
			return infoProductDao.findProductDetails(entity);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询[info_product_detail]时的错误", e);
			return null;
		}
	}
	
	
	@Override
	protected BaseDao getDao() {
		return infoProductDao;
	}

	
	
	
	/**
	 * 导入相关
	 */
	
	public ImportResultBean[] importExcel(File[] files,int startrow) {
		ImportResultBean[] result = new ImportResultBean[files.length];
		for(int i = 0; i < files.length; i ++)
			result[i] = process(files[i],startrow);
		
		return result;
	}
	
	@Autowired
	private InfoCategoryService infoCategoryService;

	/**
	 * 处理一个导入文件
	 * @param file
	 */
	private ImportResultBean process(File file,int startrow) {
		// POI工具
		PoiUtil poi = null;
		// mapping
		Map<String,Integer> map = infoCategoryService.findCategoryMapping();
		// result
		ImportResultBean result = new ImportResultBean();
		try {
			poi = new PoiUtil(file);
			// 遍历
			while(poi.hasRow()) {
				if(poi.getRowNo() < startrow - 1)
					continue;
				
				try {
					saveProduct(poi, map,result);
				} catch (Exception e) {
					result.anError(poi.getRowNo() + " - " + e.getMessage());
				}
			}
		} catch (IOException e) {
			Logger.SERVICE.error("上传文件失败",e);
		} finally {
			if(poi != null) 
				poi.close();
		}
		
		return result;
	}
	
	private void saveProduct(PoiUtil poi,Map<String,Integer> map,ImportResultBean result) throws Exception {
		String pname = poi.getValue(0,"产品名称").toString();
		String pcode = poi.getValue(1,"").toString();
		if(pcode.equals("") || pcode.length() != 6)
			throw new Exception("产品代码为空 或者 产品代码长度错误");
		
		Double pweight = (Double) poi.getValue(2,0.0);
		String quality = poi.getValue(3,"成色").toString();
		String stand = poi.getValue(4,"规格").toString();
		String premark = poi.getValue(27,"").toString();
		String image = poi.getValue(28,"").toString();
		
		Integer type1 = map.get("1" + pcode.substring(0,1));
		Integer type2 = map.get("2" + pcode.substring(1,2));
		Integer type3 = map.get("3" + pcode.substring(2,4));
		Integer type4 = map.get("4" + pcode.substring(4));
		
		if(type1 == null || type2 == null || type3 == null || type4 == null)
			throw new Exception("无效的产品代码");
		
		InfoProduct infoProduct = new InfoProduct();
		infoProduct.getType1().setId(type1);
		infoProduct.getType2().setId(type2);
		infoProduct.getType3().setId(type3);
		infoProduct.getType4().setId(type4);
		infoProduct.setPname(pname);
		infoProduct.setPcode(pcode);
		
		Integer pid = addProduct(infoProduct, result);
		
		InfoProductDetail infoProductDetail = new InfoProductDetail();
		infoProductDetail.setPid(pid);
		infoProductDetail.setPweight(pweight);
		infoProductDetail.setQuality(quality);
		infoProductDetail.setStand(stand);
		infoProductDetail.setPremark(premark);
		infoProductDetail.setImage(image);
		
		addDetail(infoProductDetail);
	}
	
	private Integer addProduct(InfoProduct infoProduct,ImportResultBean result) {
		Logger.SERVICE.debug("导入产品 " + infoProduct.toString());
		try {
			Integer pid = infoProductDao.addNotExists(infoProduct);
			result.anSuccess();
			return pid;
		} catch (SQLException e) {
			result.anError(e.getMessage());
			exceptionHandler.onDatabaseException("导入InfoProduct错误", e);
			return null;
		}
	}
	private void addDetail(InfoProductDetail infoProductDetail) {
		Logger.SERVICE.debug("导入产品-详细 " + infoProductDetail.toString());
		
		try {
			InsertEntity entity = new InsertEntity();
			entity.init(infoProductDetail);
			infoProductDao.addProductDetail(entity);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("导入infoProductDetail错误", e);
		}
		
	}
}
