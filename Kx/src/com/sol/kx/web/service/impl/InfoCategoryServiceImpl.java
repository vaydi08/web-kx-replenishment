package com.sol.kx.web.service.impl;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sol.util.c3p0.dataEntity2.DeleteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.InfoCategoryDao;
import com.sol.kx.web.dao.pojo.InfoCategory;
import com.sol.kx.web.service.ImageService;
import com.sol.kx.web.service.InfoCategoryService;
import com.sol.kx.web.service.bean.ComboBoxBean;
import com.sol.kx.web.service.bean.ResultBean;

@Service
public class InfoCategoryServiceImpl extends BaseServiceImpl<InfoCategory> implements InfoCategoryService{
	@Autowired
	private InfoCategoryDao infoCategoryDao;
	@Autowired
	private ImageService imageService;
	
	@Override
	protected BaseDao getDao() {
		return infoCategoryDao;
	}
	
	// 用于确认删除
	@Override
	public ResultBean delete2(InfoCategory obj) {
		try {
			if(infoCategoryDao.checkDeleteExists(obj.getId()))
				return ResultBean.RESULT_ERR("存在下级类别,不能直接进行删除");
			else {
				DeleteEntity entity = new DeleteEntity();
				entity.init(obj);
				infoCategoryDao.delete2(entity);
				return ResultBean.RESULT_SUCCESS();
			}
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("删除[info_category]记录时的错误", e);
			return ResultBean.RESULT_ERR("数据库错误 " + e.getMessage());
		}
	}
	
	public ComboBoxBean findCategoryType1(int clevel,String defaultText) {
		List<InfoCategory> list = null;
		try {
			list = infoCategoryDao.findCategoryType1(clevel);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询[Info_category]分类时产生错误 clevel:" + clevel, e);
			return new ComboBoxBean();
		}
		
		ComboBoxBean bean = new ComboBoxBean();
		//bean.addData(defaultText, 0,true);
		for(InfoCategory po : list)
			bean.addData(po.getCname(), po.getId(),po.getCcode());
		bean.setFirstSelect();
		
		return bean;
	}
	
	public ComboBoxBean findCategoryType(int clevel,int parent,String defaultText) {
		List<InfoCategory> list = null;
		try {
			list = infoCategoryDao.findCategoryType(clevel,parent);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询[Info_category]分类时产生错误 clevel:" + clevel, e);
			return new ComboBoxBean();
		}
		
		ComboBoxBean bean = new ComboBoxBean();
		bean.addData(defaultText, 0,true);
		for(InfoCategory po : list)
			bean.addData(po.getCname(), po.getId(),po.getCcode());
		
		return bean;
	}

	
	public Map<String,Integer> findCategoryMapping() {
		Map<String,Integer> map = new HashMap<String, Integer>();
		
		List<InfoCategory> list = null;
		try {
			list = infoCategoryDao.findCategoryMapping();
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询[Info_category]分类名称-ID映射产生错误", e);
			return map;
		}
		
		for(InfoCategory po : list) {
			String code = po.getCcode();
			Integer level = po.getClevel();
			Integer id = po.getId();
			
			map.put(level + code, id);
		}
		
		return map;
	}
	
	public Map<String,Integer> findCategoryChoose(InfoCategory input) {
		Map<String,Integer> map = new HashMap<String, Integer>();
		
		List<InfoCategory> list = null;
		try {
			list = infoCategoryDao.findCategoryType(input.getClevel(),input.getParent());
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询[Info_category]分类映射产生错误", e);
			return map;
		}
		
		for(InfoCategory po : list) {
			String name = po.getCname();
			Integer id = po.getId();
			
			map.put(name, id);
		}
		
		return map;
	}
	
	public String generateProductPcode(InfoCategory input) {
		Logger.SERVICE.ldebug("根据分类获取产品代码数据", input.toString());
		try {
			return infoCategoryDao.generatePname(input.getParent(), input.getCcode());
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("插入记录错误", e);
			return null;
		}
	}
	
	public ResultBean checkExists(String pcode,Integer level,Integer parent) {
		try {
			return (infoCategoryDao.checkExists(pcode,level,parent)) ? 
					ResultBean.RESULT_ERR("已存在的商品代码,请重新输入") : ResultBean.RESULT_SUCCESS();
		} catch (SQLException e) {
			exceptionHandler.onDatabaseException("查询[info_category]重复记录时的错误", e);
			return ResultBean.RESULT_ERR("数据库错误");
		}
	}
	
	public String saveUploadPic(String picData,String image,InfoCategory input) {
		String filename = image + ".jpg";
		
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
	public String saveUploadPic(File img,String image,InfoCategory input) {
		String filename = image + ".jpg";
		
		try {
			imageService.save(img, filename);
			input.setImage(filename);
			return filename;
		} catch (Exception e) {
			exceptionHandler.onSaveUpload(filename, "upload file", e);
			return null;
		}
	}
}
