package com.sol.kx.web.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.CompareDao;
import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.service.CompareService;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.util.PoiUtil;

@Service
public class CompareServiceImpl extends BaseServiceImpl<Compare> implements CompareService {

	@Autowired
	private ApplicationContext ctx;
	
	private CompareDao getCompareDao() {
		return ctx.getBean(CompareDao.class);
	}
	
	public PagerBean<Compare> compareSupply(File uploadFile,int shopid,int stocktype) {
		CompareDao dao = getCompareDao();
		
		PagerBean<Compare> bean = new PagerBean<Compare>();
		
		try {
			dao.startTransaction();
			// 建立临时表
			dao.createSupplyTempTable();
			
			// 填充数据
			List<String> errList = new ArrayList<String>();
			readSupplyFile(uploadFile, 3, dao, errList);
			
			List<Compare> list = dao.compareSupply(shopid,stocktype);
			
			dao.removeTempTable();
			dao.commit();
			
			bean.setDataList(list);
			return bean;
		} catch (Exception e) {
			try {
				dao.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			exceptionHandler.onDatabaseException("处理比对数据时产生错误", e);
			bean.setException(new Exception("数据库错误:" + e.getMessage(),e));
			return bean;
		} finally {
			dao.close();
		}
	}

	private void readSupplyFile(File file,int startrow,CompareDao dao,List<String> errList) throws IOException {
		PoiUtil poi = new PoiUtil(file);
		
		while(poi.hasRow()) {
			if(poi.getRowNo() < startrow - 1)
				continue;
			
			try {
				dao.insertSupplyTempTable(poi.getValue(5, "").toString(), (Double)poi.getValue(6, 0));
			} catch (SQLException e) {
				errList.add("第" + poi.getRowNo() + "行数据导入失败,原因:" + e.getMessage());
			}
		}
		
		poi.close();
	}
	@Override
	protected BaseDao getDao() {
		return null;
	}
}
