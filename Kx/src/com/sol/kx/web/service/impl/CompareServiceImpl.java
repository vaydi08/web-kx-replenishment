package com.sol.kx.web.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
			readSupplyFile(uploadFile, 2, dao, errList);
			
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
			} catch (Exception e) {
				errList.add("第" + poi.getRowNo() + "行数据导入失败,原因:" + e.getMessage());
			}
		}
		
		poi.close();
	}
	
	// CARGO
	
	public PagerBean<Compare> compareCargo(File cargoSupplyFile,File cargoSaleFile,File cargoStockFile,int stocktype) {
		CompareDao dao = getCompareDao();
		
		
		PagerBean<Compare> bean = new PagerBean<Compare>();
		
		try {
			dao.startTransaction();
			// supply
			// 建立临时表
			dao.cargoSupplyCreateTempTable(stocktype);
			
			// 填充数据
			List<String> errList = new ArrayList<String>();
			readCargoSupplyFile(cargoSupplyFile, 2, dao, errList);
			
			
			// sale
			// 建立临时表
			dao.cargoSaleCreateTempTable();
			
			// 填充数据
			readCargoSaleFile(cargoSaleFile, 2, dao, errList,stocktype);
			
			
			// stock
			// 建立临时表
			dao.cargoStockCreateTempTable();
			
			// 填充数据
			readCargoStockFile(cargoStockFile, 2, dao, errList);
			
//			dao.cargoSupplyRemoveTempTable();
//			dao.cargoSaleRemoveTempTable();
//			dao.cargoStockRemoveTempTable();
			
			dao.commit();
			System.out.println(errList);
			//bean.setDataList(list);
			return bean;
		} catch (Exception e) {
			try {
				dao.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			exceptionHandler.onDatabaseException("处理分货数据时产生错误", e);
			bean.setException(new Exception("数据库错误:" + e.getMessage(),e));
			return bean;
		} finally {
			dao.close();
		}
	}

	private void readCargoSupplyFile(File file,int startrow,CompareDao dao,List<String> errList) throws IOException {
		PoiUtil poi = new PoiUtil(file);
		
		while(poi.hasRow()) {
			if(poi.getRowNo() < startrow - 1)
				continue;
			
			try {
				dao.cargoSupplyDataUpdate(poi.getValue(5, "").toString(), poi.getValue(0, "").toString(), (Double)poi.getValue(6, 0));
			} catch (Exception e) {
				errList.add("库存数据 - 第" + poi.getRowNo() + "行数据导入失败,原因:" + e.getMessage());
			}
		}
		
		poi.close();
	}
	
	private void readCargoSaleFile(File file,int startrow,CompareDao dao,List<String> errList,Integer stocktype) throws IOException {
		PoiUtil poi = new PoiUtil(file);

		DateFormat df = new SimpleDateFormat("dd-MMM-yy HH:mm:ss",Locale.ENGLISH);
		
		while(poi.hasRow()) {
			if(poi.getRowNo() < startrow - 1)
				continue;
			
			try {
				String serial = (String)poi.getValue(0, "");
				Integer sale = ((Double)poi.getValue(2, 1)).intValue();
				if(sale < 0) {
					dao.cargoSaleDateDelete(serial);
					continue;
				}
				long saletime;
				try {
					saletime = df.parse((String)poi.getValue(5,"")).getTime();
				} catch (Exception e) {
					saletime = (new Date()).getTime();
				}
				String shopname = (String)poi.getValue(1, "");
				String pcode = (String)poi.getValue(58, "");
				Double weight = (Double)poi.getValue(59, 0.0);
				
				dao.cargoSaleDataUpdate(serial, sale, saletime, shopname, pcode, weight, stocktype);
			} catch (Exception e) {
				errList.add("销售数据 - 第" + poi.getRowNo() + "行数据导入失败,原因:" + e.getMessage());
			}
		}
		
		poi.close();
	}
	
	private void readCargoStockFile(File file,int startrow,CompareDao dao,List<String> errList) throws IOException {
		PoiUtil poi = new PoiUtil(file);
		
		while(poi.hasRow()) {
			if(poi.getRowNo() < startrow - 1)
				continue;
			
			try {
				String serial = (String)poi.getValue(0, "");
				Integer num = ((Double)poi.getValue(1, 1)).intValue();
				String pcode = (String)poi.getValue(2, "");
				Double weight = (Double)poi.getValue(3, 0.0);
				dao.cargoStockDataUpdate(serial,num,pcode,weight);
			} catch (Exception e) {
				errList.add("进货数据 - 第" + poi.getRowNo() + "行数据导入失败,原因:" + e.getMessage());
			}
		}
		
		poi.close();
	}
	
	
	@Override
	protected BaseDao getDao() {
		return null;
	}
}
