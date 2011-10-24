package com.sol.kx.web.service.impl;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.sol.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.CompareDao;
import com.sol.kx.web.dao.pojo.CargoCompare;
import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.service.CompareService;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.bean.PagerBean_Cargo;
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
			//dao.commit();
			dao.rollback();
			
			bean.setDataList(list);
			if(errList.size() > 0)
				bean.setReserve(new Object[]{errList});
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
	
	public PoiUtil exportDownloadSupply(List<Compare> list) {
		PoiUtil poi = new PoiUtil("补货建议单");
		poi.newRow();
		poi.setValue(0, "产品名称");
		poi.setValue(1, "产品代码");
		poi.setValue(2, "克重范围");
		poi.setValue(3, "核定库存");
		poi.setValue(4, "实际库存");
		poi.setValue(5, "建设补货量");
		
		for(Compare po : list) {
			poi.newRow();
			poi.setValue(0, po.getPname());
			poi.setValue(1, po.getPcode());
			poi.setValue(2, po.getMinweight() + "-" + po.getMaxweight());
			poi.setValue(3, po.getStock());
			poi.setValue(4, po.getKucun());
			poi.setValue(5, po.getNeed());
		}
		
		for(int i = 0; i < 6; i ++)
			poi.autoSize(i);
		
		return poi;
	}
	
	// CARGO
	
	public PagerBean_Cargo compareCargo(File cargoSupplyFile,File cargoSaleFile,File cargoStockFile,int stocktype,Integer minallot) {
		CompareDao dao = getCompareDao();
		
		
		PagerBean_Cargo bean = new PagerBean_Cargo();
		
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
			
			// 比对
			// 生成结果集
			dao.cargoCompareCreateTb(minallot);
			// 获取进货表
			List<CargoCompare> cargoCompares = dao.cargoFindStock();
			for(Iterator<CargoCompare> it = cargoCompares.iterator(); it.hasNext();) {
				CargoCompare cargoCompare = it.next();
				Object[] ret = dao.cargoCompareUpdateTb(cargoCompare);
				if((Integer)ret[0] > 0) {
					dao.cargoStockUpdate((String)ret[1], cargoCompare.getSerial(), cargoCompare.getWeight());
					it.remove();
				}
			}
			
			for(Iterator<CargoCompare> it = cargoCompares.iterator(); it.hasNext();) {
				CargoCompare cargoCompare = it.next();
				Object[] ret = dao.cargoCompareUpdateTbNormal(cargoCompare);
				if((Integer)ret[0] > 0) {
					dao.cargoStockUpdate((String)ret[1], cargoCompare.getSerial(), cargoCompare.getWeight());
					it.remove();
				}
			}
			
			List<CargoCompare> result = dao.cargoFindResult();
			List<CargoCompare> stock = dao.cargoFindStock();
//			System.out.println(cargoCompares);
			
//			dao.commit();
			dao.rollback();
//			System.out.println(errList);
			bean.setDataList(result);
			bean.setStockList(stock);
			if(errList.size() > 0)
				bean.setReserve(new Object[]{errList});
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
	
	public PoiUtil exportDownloadCargo(PagerBean<CargoCompare> cargoBean) {
		PoiUtil poi = new PoiUtil("分货单");
		poi.newRow();
		poi.setValue(0, "门店名称");
		poi.setValue(1, "产品名称");
		poi.setValue(2, "产品代码");
		poi.setValue(3, "克重范围");
		poi.setValue(4, "核定库存");
		poi.setValue(5, "实际库存");
		poi.setValue(6, "最后销售时间");
		poi.setValue(7, "配货流水号 ");
		poi.setValue(8, "配货数量");
		poi.setValue(9, "至少还需配货");
		
		for(CargoCompare po : cargoBean.getDataList()) {
			poi.newRow();
			poi.setValue(0, po.getShopname());
			poi.setValue(1, po.getPname());
			poi.setValue(2, po.getPcode());
			poi.setValue(3, po.getMinweight() + "-" + po.getMaxweight());
			poi.setValue(4, po.getStock());
			poi.setValue(5, po.getStocknow());
			if(po.getSaletime() != null)
				poi.setValue(6, StringUtil.formatTimestamp(po.getSaletime()));
			poi.setValue(7, po.getSerial());
			poi.setValue(8, po.getNum());
			if(po.getMinallot() > 0)
				poi.setValue(9, po.getMinallot());
		}
		
		for(int i = 0; i < 9; i ++)
			poi.autoSize(i);
		
		
		poi.newSheet("货品分配");
		poi.newRow();
		poi.setValue(0, "产品名称");
		poi.setValue(1, "产品代码");
		poi.setValue(2, "流水号 ");
		poi.setValue(3, "克重");
		poi.setValue(4, "分配门店");
		
		for(CargoCompare po : ((PagerBean_Cargo)cargoBean).getStockList()) {
			poi.newRow();
			poi.setValue(0, po.getPname());
			poi.setValue(1, po.getPcode());
			poi.setValue(2, po.getSerial());
			poi.setValue(3, po.getWeight());
			if(po.getShopname() != null)
				poi.setValue(4, po.getShopname());
		}
		
		for(int i = 0; i < 5; i ++)
			poi.autoSize(i);
		
		return poi;
	}
	
	
	
	@Override
	protected BaseDao getDao() {
		return null;
	}
}
