package com.sol.kx.web.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.sol.util.c3p0.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.InfoProductDao;
import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.service.InfoProductService;
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

	
	public void importExcel(File[] file) {
		
	}
	
	/**
	 * 处理一个导入文件
	 * @param file
	 */
	private void process(File file) {
		// POI工具
		PoiUtil poi = null;
		try {
			poi = new PoiUtil(file);
			// 遍历
			while(poi.hasRow()) {
				InfoProduct infoProduct = new InfoProduct();
				while(poi.hasCell()) {
					System.out.println(poi.getValue());
				}
			}
		} catch (IOException e) {
//			result = ResultBean.RESULT_ERR(e.getMessage());
			
			Logger.SYS.error("上传文件失败",e);
		} finally {
			if(poi != null) 
				poi.close();
		}
	}
}
