package com.sol.kx.web.service.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.sol.util.c3p0.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.InfoProductDao;
import com.sol.kx.web.dao.pojo.InfoCategory;
import com.sol.kx.web.dao.pojo.InfoProduct;
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
	
	private class ExcelMapping {
		String name;
		Class<?> type;
		Object defaultvalue;
		public ExcelMapping(String name,Class<?> type,Object defaultvalue) {
			this.name = name;
			this.type = type;
			this.defaultvalue = defaultvalue;
		}
		void doMethod(InfoProduct obj,Object cellValue) {
			Method method;
			try {
				if(cellValue == null)
					cellValue = defaultvalue;
				Object value = cellValue;
				if(!cellValue.getClass().equals(type)) {
					if(type.equals(String.class))
						value = cellValue.toString();
					else if(type.equals(Double.class))
						value = new Double(0);
					else if(type.equals(Boolean.class))
						value = new Boolean(true);
				}
				method = obj.getClass().getMethod(name, type);
				method.invoke(obj, value);
			} catch (Exception e) {
				exceptionHandler.onExcelException("导入数据错误 obj:" + obj + 
						" name:" + name + " value:" + cellValue, e);
			}			
		}
	}
	private class ExcelTypeMapping extends ExcelMapping {
		Integer level;
		Map<String,Integer> map;
		public ExcelTypeMapping(String name,Class<?> type,Object defaultvalue,Integer level,Map<String,Integer> map) {
			super(name,type,defaultvalue);
			this.level = level;
			this.map = map;
		}
		void doMethod(InfoProduct obj,Object cellValue) {
			Integer value = 0;
			if(cellValue != null) {
				value = map.get(level + cellValue.toString() + "");
				if(value == null) {
					value = infoCategoryService.addAndReturnKey(new InfoCategory("", cellValue.toString(), level));
					map.put(level + cellValue.toString(), value);
				}
			}
			InfoCategory subobj = null;
			try {
				subobj = (InfoCategory)obj.getClass().getMethod(name).invoke(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
			subobj.setId(value);
		}
	}

	private List<ExcelMapping> getMethodMapping() {
		Map<String,Integer> map = infoCategoryService.findCategoryMapping();
		
		List<ExcelMapping> methodMapping = new ArrayList<ExcelMapping>(33);
		methodMapping.add(new ExcelTypeMapping("getType1",Integer.class,0,1,map));
		methodMapping.add(new ExcelTypeMapping("getType2",Integer.class,0,2, map));
		methodMapping.add(new ExcelTypeMapping("getType3",Integer.class,0,3,map));
		methodMapping.add(new ExcelTypeMapping("getType4",Integer.class,0,4, map));
		methodMapping.add(new ExcelMapping("setPname",String.class,"产品名称"));
		methodMapping.add(new ExcelMapping("setPcode",String.class,"CODE"));
		methodMapping.add(new ExcelMapping("setQuality",String.class,"成色"));
		methodMapping.add(new ExcelMapping("setPweight",Double.class,0.0));
		methodMapping.add(new ExcelMapping("setStand",String.class,"规格"));
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(null);
		methodMapping.add(new ExcelMapping("setPremark",String.class,""));
		methodMapping.add(new ExcelMapping("setImage",String.class,""));
		
		return methodMapping;
	}
	/**
	 * 处理一个导入文件
	 * @param file
	 */
	private ImportResultBean process(File file,int startrow) {
		// POI工具
		PoiUtil poi = null;
		// mapping
		List<ExcelMapping> mapping = getMethodMapping();
		// result
		ImportResultBean result = new ImportResultBean();
		try {
			poi = new PoiUtil(file);
			// 遍历
			while(poi.hasRow()) {
				InfoProduct infoProduct = new InfoProduct();
				if(poi.getRowNo() < startrow - 1)
					continue;
				
				while(poi.hasCell()) {
					if(poi.getCellNo() < mapping.size()) {
						ExcelMapping map = mapping.get(poi.getCellNo());
						if(map != null)
							map.doMethod(infoProduct, poi.getValue());
					}
				}
				
				importFile(infoProduct, result);
			}
		} catch (IOException e) {
//			result = ResultBean.RESULT_ERR(e.getMessage());
			
			Logger.SERVICE.error("上传文件失败",e);
		} finally {
			if(poi != null) 
				poi.close();
		}
		
		return result;
	}
	
	private void importFile(InfoProduct infoProduct,ImportResultBean result) {
		Logger.SERVICE.debug("导入产品 " + infoProduct.toString());
		try {
			infoProductDao.addAndReturnKey(infoProduct);
			result.anSuccess();
		} catch (SQLException e) {
			result.anError(e.getMessage());
			exceptionHandler.onDatabaseException("导入InfoProduct错误", e);
		}
		
	}
}
