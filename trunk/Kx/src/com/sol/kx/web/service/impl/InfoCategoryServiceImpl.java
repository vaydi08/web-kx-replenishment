package com.sol.kx.web.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sun.misc.BASE64Decoder;

import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.InfoCategoryDao;
import com.sol.kx.web.dao.pojo.InfoCategory;
import com.sol.kx.web.service.InfoCategoryService;
import com.sol.kx.web.service.bean.ComboBoxBean;
import com.sol.kx.web.service.bean.PagerBean;

@Service
public class InfoCategoryServiceImpl extends BaseServiceImpl<InfoCategory> implements InfoCategoryService{
	@Autowired
	private InfoCategoryDao infoCategoryDao;
	
	@Override
	protected BaseDao getDao() {
		return infoCategoryDao;
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
	

	public PagerBean<InfoCategory> findCustom(PagerBean<InfoCategory> bean,
			InfoCategory obj) {
		Logger.SERVICE.ldebug("查询[info_category]数据",bean.getPage(),bean.getPageSize(),obj.toString());
				
		try {
			return setBeanValue(bean, 
					infoCategoryDao.findCustom(InfoCategory.class, obj, bean.getPage(), bean.getPageSize(),"id"),
					infoCategoryDao.findCountCustom(obj));
		} catch (Exception e) {
			bean.setException(e);
			exceptionHandler.onDatabaseException("查询[info_category]时的错误", e);
			return bean;
		}
	}
	
	public String saveUploadPic(String picData,InfoCategory input) {
		String filename = null;
		OutputStream fos = null;
		try {
			BASE64Decoder decode=new BASE64Decoder();
			byte[] datas=decode.decodeBuffer(picData);
			filename = "category_" + input.getClevel() + "_" + input.getCcode() + ".jpg";
			String savePath = ServletActionContext.getServletContext().getRealPath("/upload/etc");
			File file=new File(savePath,filename);
			fos=new FileOutputStream(file);
			fos.write(datas);
			fos.flush();
			input.setImage(filename);
			return filename;
		} catch (Exception e) {
			exceptionHandler.onSaveUpload(filename, picData, e);
			return null;
		} finally {
			if(fos != null)
				try {
					fos.close();
				} catch (IOException e) {
				}
		}
	}
}
