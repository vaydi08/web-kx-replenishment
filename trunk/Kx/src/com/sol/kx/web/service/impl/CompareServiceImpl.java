package com.sol.kx.web.service.impl;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.CompareDao;
import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.service.CompareService;

@Service
public class CompareServiceImpl extends BaseServiceImpl<Compare> implements CompareService {

	@Autowired
	private ApplicationContext ctx;
	
	private CompareDao getCompareDao() {
		return ctx.getBean(CompareDao.class);
	}
	
	public List<Compare> compareSupply(File uploadFile,int shopid,int stocktype) {
		CompareDao dao = getCompareDao();
		
		try {
			dao.startTransaction();
			// 建立临时表
			dao.createSupplyTempTable();
			
			// 填充数据
			dao.insertSupplyTempTable("11sb01", 9.22);
			dao.insertSupplyTempTable("11sb02", 5.62);
			dao.insertSupplyTempTable("11mb05", 25.25);
			dao.insertSupplyTempTable("11ss03", 17.25);
			dao.insertSupplyTempTable("11sb01", 12.25);
			
			List<Compare> list = dao.compareSupply(shopid,stocktype);
			
			dao.removeTempTable();
			dao.commit();
			
			return list;
		} catch (Exception e) {
			try {
				dao.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			exceptionHandler.onDatabaseException("处理比对数据时产生错误", e);
			return null;
		} finally {
			dao.close();
		}
	}

	@Override
	protected BaseDao getDao() {
		return null;
	}
}
