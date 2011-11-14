package com.sol.kx.web.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.sol.util.c3p0.Condition;
import org.sol.util.c3p0.dataEntity2.Criteria;
import org.sol.util.c3p0.dataEntity2.InsertEntity;
import org.sol.util.c3p0.dataEntity2.SelectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import sun.misc.BASE64Decoder;

import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.InfoProductDao;
import com.sol.kx.web.dao.pojo.InfoCategory;
import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.dao.pojo.InfoProductDetail;
import com.sol.kx.web.dao.pojo.StockCheck;
import com.sol.kx.web.service.ImageService;
import com.sol.kx.web.service.InfoCategoryService;
import com.sol.kx.web.service.InfoProductService;
import com.sol.kx.web.service.bean.ImportResultBean;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.bean.ResultBean;
import com.sol.kx.web.service.util.PoiUtil;

@Service
public class InfoProductServiceImpl extends BaseServiceImpl<InfoProduct> implements InfoProductService{
	
	@Autowired
	private InfoProductDao infoProductDao;
	@Autowired
	private ImageService imageService;
	
	public PagerBean<InfoProduct> findByPage2(PagerBean<InfoProduct> bean,InfoProduct obj) {		
		Logger.SERVICE.ldebug("查询[info_product]数据",bean.getPage(),bean.getPageSize(),obj.toString());
		try {
			Criteria criteria = new Criteria();
			criteria.byPojo(obj, true);
			return setBeanValue(bean, 
					infoProductDao.find(bean.getPage(), bean.getPageSize(), criteria),
					infoProductDao.findCount(obj));
		} catch (Exception e) {
			bean.setException(e);
			exceptionHandler.onDatabaseException("查询[info_product]时的错误", e);
			return bean;
		}
	}
	
	public ResultBean checkExists(String pcode) {
		try {
			return (infoProductDao.checkExists(pcode)) ? 
					ResultBean.RESULT_ERR("已存在的商品代码,请重新选择") : ResultBean.RESULT_SUCCESS();
		} catch (SQLException e) {
			exceptionHandler.onDatabaseException("查询[info_product]重复记录时的错误", e);
			return ResultBean.RESULT_ERR("数据库错误");
		}
	}
	
	public PagerBean<InfoProduct> findFuzzy(PagerBean<InfoProduct> bean,String value) {		
		Logger.SERVICE.ldebug("查询[info_product]数据",bean.getPage(),bean.getPageSize(),value);
		try {
			return setBeanValue(bean, 
					infoProductDao.findFuzzy(bean.getPage(), bean.getPageSize(), value),
					infoProductDao.findCountFuzzy(value));
		} catch (Exception e) {
			bean.setException(e);
			exceptionHandler.onDatabaseException("查询[info_product]时的错误", e);
			return bean;
		}
	}
	
	public Map<String,List<StockCheck>> findQuickLocator(Integer pid) {
		Logger.SERVICE.ldebug("查询[stock_type]数据,快速定位",pid);
		
		try {
			List<StockCheck> list = infoProductDao.findQuickLocator(pid);
			
			if(list == null || list.size() == 0)
				return null;
			
			Map<String,List<StockCheck>> map = new HashMap<String,List<StockCheck>>();

			for(StockCheck stockCheck : list) {
				List<StockCheck> typeList = map.get(stockCheck.getShopname());
				if(typeList == null) {
					typeList = new ArrayList<StockCheck>();
					map.put(stockCheck.getShopname(), typeList);
				}
				typeList.add(stockCheck);
			}
			
			return map;
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询[stock_type]数据,快速定位错误", e);
			return null;
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
	
	
	@Value("${product.startrow}")
	private Integer PRODUCT_STARTROW;
	@Value("${product.pname}")
	private Integer PRODUCT_PNAME;
	@Value("${product.pcode}")
	private Integer PRODUCT_PCODE;
	@Value("${product.pweight}")
	private Integer PRODUCT_PWEIGHT;
	@Value("${product.quality}")
	private Integer PRODUCT_QUALITY;
	@Value("${product.stand}")
	private Integer PRODUCT_STAND;
	@Value("${product.remark}")
	private Integer PRODUCT_REMARK;
	@Value("${product.image}")
	private Integer PRODUCT_IMAGE;
	
	// 导出
	public PoiUtil createExcel() {
		Logger.SERVICE.debug("查询[info_product + info_product_detail]数据,导出为Excel表格数据");
		
		PoiUtil poi = new PoiUtil("产品数据");
		
		// 获取数据库
		List<InfoProduct> list = null;
		try {
			list = infoProductDao.findExport();
		} catch (Exception e) {
			Logger.SERVICE.error("查询[info_product + info_product_detail]数据,导出为Excel表格数据错误",e);
			return poi;
		}
		
		poi.newRow();
		poi.setValue(0, "产品名称");
		poi.setValue(1, "产品代码");
		poi.setValue(2, "克重");
		poi.setValue(3, "成色");
		poi.setValue(4, "规格");
		poi.setValue(27, "备注");
		poi.setValue(28, "图片");
		poi.newRow();
		poi.mergeCells(0, 0, 0, 1);
		poi.mergeCells(1, 0, 1, 1);
		poi.mergeCells(2, 0, 2, 1);
		poi.mergeCells(3, 0, 3, 1);
		poi.mergeCells(4, 0, 4, 1);
		poi.mergeCells(27, 0, 27, 1);
		poi.mergeCells(28, 0, 28, 1);
		
		// 输出到EXCEL表
		for(InfoProduct po : list) {
			poi.newRow();
			poi.setValue(PRODUCT_PNAME, po.getPname());
			poi.setValue(PRODUCT_PCODE, po.getPcode());
			poi.setValue(PRODUCT_PWEIGHT, po.getPweight());
			poi.setValue(PRODUCT_QUALITY, po.getQuality());
			poi.setValue(PRODUCT_STAND, po.getStand());
			poi.setValue(PRODUCT_REMARK, po.getPremark());
			poi.setValue(PRODUCT_IMAGE, po.getImage());
		}
		
		return poi;
	}
	
	@Override
	protected BaseDao getDao() {
		return infoProductDao;
	}

	
	
	
	/**
	 * 导入相关
	 */
	
	public ImportResultBean[] importExcel(File[] files) {
		ImportResultBean[] result = new ImportResultBean[files.length];
		for(int i = 0; i < files.length; i ++)
			result[i] = process(files[i],PRODUCT_STARTROW);
		
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
		
		Map<String,Integer> codeMap = findCode2Id();
		// result
		ImportResultBean result = new ImportResultBean();
		try {
			poi = new PoiUtil(file);
			// 遍历
			while(poi.hasRow()) {
				if(poi.getRowNo() < startrow - 1)
					continue;
				
				try {
					saveProduct(poi, map,codeMap,result);
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
	
	private void saveProduct(PoiUtil poi,Map<String,Integer> map,Map<String,Integer> codeMap,ImportResultBean result) throws Exception {
		String pname = poi.getValue(PRODUCT_PNAME,"产品名称").toString();
		String pcode = poi.getValue(PRODUCT_PCODE,"").toString();
		if(pcode.equals("") || pcode.length() != 6)
			throw new Exception("产品代码为空 或者 产品代码长度错误");
		
		Double pweight = (Double) poi.getValue(PRODUCT_PWEIGHT,0.0);
		String quality = poi.getValue(PRODUCT_QUALITY,"成色").toString();
		String stand = poi.getValue(PRODUCT_STAND,"规格").toString();
		String premark = poi.getValue(PRODUCT_REMARK,"").toString();
		String image = poi.getValue(PRODUCT_IMAGE,"").toString();
		
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
		
		Integer pid = codeMap.get(pcode);
		if(pid == null) {
			pid = addProduct(infoProduct, result);
			codeMap.put(pcode, pid);
		}
		
		InfoProductDetail infoProductDetail = new InfoProductDetail();
		infoProductDetail.setPid(pid);
		infoProductDetail.setPweight(pweight);
		infoProductDetail.setQuality(quality);
		infoProductDetail.setStand(stand);
		infoProductDetail.setPremark(premark);
		infoProductDetail.setImage(image);
		
		addDetail(infoProductDetail);
	}
	
	private Map<String,Integer> findCode2Id() {
		Logger.SERVICE.debug("查询产品编号列表");
		try {
			List<InfoProduct> infoProducts = infoProductDao.findCode2Id();
			Map<String, Integer> map = new HashMap<String, Integer>(infoProducts.size());
			for(InfoProduct infoProduct : infoProducts)
				map.put(infoProduct.getPcode(), infoProduct.getId());
			return map;
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("导入InfoProduct错误", e);
			return new HashMap<String, Integer>();
		}
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
	public void addDetail(InfoProductDetail infoProductDetail) {
		Logger.SERVICE.debug("导入(添加)产品-详细 " + infoProductDetail.toString());
		
		try {
			InsertEntity entity = new InsertEntity();
			entity.init(infoProductDetail,false);
			infoProductDao.addProductDetail(entity);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("导入infoProductDetail错误", e);
		}
		
	}
	public ResultBean addProductDetail(InfoProductDetail infoProductDetail) {
		Logger.SERVICE.ldebug("插入[info_product_detail]数据", infoProductDetail.toString());
		try {
			InsertEntity entity = new InsertEntity();
			entity.init(infoProductDetail,false);
			infoProductDao.addProductDetail(entity);
			return ResultBean.RESULT_SUCCESS();
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("插入记录错误", e);
			return ResultBean.RESULT_ERR(e.getMessage());
		}
	}
	
	// 保存摄像头图片
	public String saveUploadPic(String picData,InfoProduct input) {
		String filename = input.getPcode() + ".jpg";
		
		try {
			imageService.save(picData, filename);
			input.setImage(filename);
			return filename;
		} catch (Exception e) {
			exceptionHandler.onSaveUpload(filename, picData, e);
			return null;
		}
	}
	// 保存上传图片
	public String saveUploadPic(File img,InfoProduct input) {
		String filename = input.getPcode() + ".jpg";
		
		try {
			imageService.save(img, filename);
			input.setImage(filename);
			return filename;
		} catch (Exception e) {
			exceptionHandler.onSaveUpload(filename, "", e);
			return null;
		}
	}
}
