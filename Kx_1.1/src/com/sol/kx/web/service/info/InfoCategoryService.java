package com.sol.kx.web.service.info;

import java.io.File;
import java.util.List;

import org.sol.util.mybatis.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.info.InfoCategoryMapper;
import com.sol.kx.web.dao.pojo.InfoCategory;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.ImageService;
import com.sol.kx.web.service.bean.ComboBoxBean;
import com.sol.kx.web.service.bean.ResultBean;

@Service
public class InfoCategoryService extends BaseService{

	@Autowired
	private InfoCategoryMapper infoCategoryMapper;
	
	@Autowired
	private ImageService imageService;
	
	/**
	 *  用于删除检查
	 * @param obj
	 * @return
	 */
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,timeout = Constants.DB_TIMEOUT)
	public ResultBean deleteCheck(InfoCategory obj) {
		if(Constants.DEBUG)
			Logger.SERVICE.debug("删除记录 [info_category] id:"+ obj.getId());
		
		try {
			int i = infoCategoryMapper.checkDeleteExists(obj);
			if(infoCategoryMapper.checkDeleteExists(obj) == 1)
				return ResultBean.RESULT_ERR("存在下级类别,不能直接进行删除",obj);
			else {
				infoCategoryMapper.delete(obj);
				return ResultBean.RESULT_SUCCESS(obj);
			}
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("删除记录时的错误","info_category", e);
			return ResultBean.RESULT_ERR("数据库错误 " + e.getMessage(),obj);
		}
	}
	
	/**
	 * 获取分类列表
	 * 添加分类时使用
	 * @param clevel
	 * @param defaultText
	 * @return
	 */
	@Transactional(readOnly = true)
	public ComboBoxBean findCategoryTypeByNew(InfoCategory obj) {
		if(Constants.DEBUG)
			Logger.SERVICE.debug("获取分类列表-ADD [info_category] "+ obj.toString());
		
		List<InfoCategory> list = null;
		try {
			list = infoCategoryMapper.findCategoryTypeByNew(obj);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询分类时产生错误 clevel:" + obj.getClevel(),"Info_category", e);
			return new ComboBoxBean();
		}
		
		ComboBoxBean bean = new ComboBoxBean();
		for(InfoCategory po : list)
			bean.addData(po.getCname(), po.getId(),po.getCcode());
		bean.setFirstSelect();
		
		return bean;
	}
	
	/**
	 * 获取分类列表
	 * 查询参数
	 * @param clevel
	 * @param parent
	 * @return
	 */
	@Transactional(readOnly = true)
	public ComboBoxBean findCategoryType(InfoCategory obj,String defaultText) {
		if(Constants.DEBUG)
			Logger.SERVICE.debug("获取分类列表-QUERY [info_category] "+ obj.toString());
		
		List<InfoCategory> list = null;
		try {
			list = infoCategoryMapper.findCategoryTypeByQuery(obj);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询分类时产生错误 clevel:" + obj.getClevel(),"Info_category", e);
			return new ComboBoxBean();
		}
		
		ComboBoxBean bean = new ComboBoxBean();
		bean.addData(defaultText, 0,true);
		for(InfoCategory po : list)
			bean.addData(po.getCname(), po.getId(),po.getCcode());
		
		return bean;
	}
	
	/**
	 * 生成产品名称
	 * 用于产生新产品时,对应图片名称
	 * @param parent
	 * @param code
	 * @return
	 */
	@Transactional(readOnly = true)
	public String generateProductPcode(InfoCategory obj) {
		if(Constants.DEBUG)
			Logger.SERVICE.debug("根据分类获取产品代码数据 "+ obj.toString());
		
		try {
			return infoCategoryMapper.generatePname(obj);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("根据分类获取产品代码数据","info_category", e);
			return null;
		}
	}
	
	/**
	 * 增加产品时校验是否有重复
	 * @param code
	 * @param level
	 * @param parent
	 * @return
	 */
	@Transactional(readOnly = true)
	public ResultBean checkExists(InfoCategory obj) {
		if(Constants.DEBUG)
			Logger.SERVICE.debug("查询重复记录 [info_category] "+ obj.toString());
		
		try {
			return (infoCategoryMapper.checkExists(obj) == 1) ? 
					ResultBean.RESULT_ERR("已存在的商品代码,请重新输入",obj.getCcode()) : 
					ResultBean.RESULT_SUCCESS(obj.getCcode());
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询重复记录时的错误","info_category", e);
			return ResultBean.RESULT_ERR("数据库错误",obj.getCcode());
		}
	}
	
	/**
	 * 保存摄像图片
	 * @param picData
	 * @param input
	 * @return
	 */
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,timeout = Constants.DB_TIMEOUT)
	public ResultBean saveUploadPic(String picData,InfoCategory obj) {
		if(Constants.DEBUG)
			Logger.SERVICE.debug("保存摄像图片");
		
		String image = generateProductPcode(obj);
		String filename = image + ".jpg";
		
		try {
			imageService.save(picData, filename);
			obj.setImage(filename);
			return insert(obj);
		} catch (Exception e) {
			exceptionHandler.onSaveUpload(filename, picData, e);
			return ResultBean.RESULT_ERR("保存上传图片失败", obj);
		}
	}
	/**
	 * 保存上传图片
	 * @param img
	 * @param image
	 * @param input
	 * @return
	 */
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,timeout = Constants.DB_TIMEOUT)
	public ResultBean saveUploadPic(File img,InfoCategory obj) {
		if(Constants.DEBUG)
			Logger.SERVICE.debug("保存上传图片");
		
		String image = generateProductPcode(obj);
		String filename = image + ".jpg";
		
		try {
			imageService.save(img, filename);
			obj.setImage(filename);
			return insert(obj);
		} catch (Exception e) {
			exceptionHandler.onSaveUpload(filename, "upload file", e);
			return ResultBean.RESULT_ERR("保存上传图片失败", obj);
		}
	}
	
	@Override
	protected BaseMapper injectMapper() {
		return infoCategoryMapper;
	}

}
