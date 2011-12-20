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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sol.kx.web.action.compare.CargoBean;
import com.sol.kx.web.action.compare.SupplyBean;
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
	
	@Value("${supply.startrow}")
	private Integer SUPPLY_STARTROW;
	@Value("${supply.pcode}")
	private Integer SUPPLY_PCODE;
	@Value("${supply.pweight}")
	private Integer SUPPLY_PWEIGHT;
	
	public SupplyBean compareSupply(File uploadFile,int shopid) {
		CompareDao dao = getCompareDao();
		
		SupplyBean bean = new SupplyBean();
		
		try {
			dao.startTransaction();
			// 建立临时表
			dao.createSupplyTempTable();
			
			// 填充数据
			List<String> errList = new ArrayList<String>();
			readSupplyFile(uploadFile, SUPPLY_STARTROW, dao, errList);
			
			List<Compare> list1 = dao.compareSupply(shopid,1);
			List<Compare> list2 = dao.compareSupply(shopid,2);
			
			dao.removeTempTable();
			//dao.commit();
			dao.rollback();
			
			bean.setStocktype1List(list1);
			bean.setStocktype2List(list2);
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
				dao.insertSupplyTempTable(poi.getValue(SUPPLY_PCODE-1, "").toString(), (Double)poi.getValue(SUPPLY_PWEIGHT-1, 0));
			} catch (Exception e) {
				errList.add("第" + poi.getRowNo() + "行数据导入失败,原因:" + e.getMessage());
			}
		}
		
		poi.close();
	}
	
	public PoiUtil exportDownloadSupply(SupplyBean bean) {
		PoiUtil poi = new PoiUtil();
		
		exportSupplyBeanList(poi, "补货建议单 一般日", bean.getStocktype1List());
		exportSupplyBeanList(poi, "补货建议单 节假日", bean.getStocktype2List());
		
		return poi;
	}
	
	private void exportSupplyBeanList(PoiUtil poi,String sheetname,List<Compare> list) {
		poi.newSheet(sheetname);
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
	}
	
	
	@Value("${cargo.supply.startrow}")
	private Integer CARGO_SUPPLY_STARTROW;
	@Value("${cargo.supply.pcode}")
	private Integer CARGO_SUPPLY_PCODE;
	@Value("${cargo.supply.shopname}")
	private Integer CARGO_SUPPLY_SHOPNAME;
	@Value("${cargo.supply.pweight}")
	private Integer CARGO_SUPPLY_PWEIGHT;
	
	@Value("${cargo.sale.startrow}")
	private Integer CARGO_SALE_STARTROW;
	@Value("${cargo.sale.serial}")
	private Integer CARGO_SALE_SERIAL;
	@Value("${cargo.sale.num}")
	private Integer CARGO_SALE_NUM;
	@Value("${cargo.sale.saletime}")
	private Integer CARGO_SALE_TIME;
	@Value("${cargo.sale.shopname}")
	private Integer CARGO_SALE_SHOPNAME;
	@Value("${cargo.sale.pcode}")
	private Integer CARGO_SALE_PCODE;
	@Value("${cargo.sale.pweight}")
	private Integer CARGO_SALE_PWEIGHT;
	
	@Value("${cargo.stock.startrow}")
	private Integer CARGO_STOCK_STARTROW;
	@Value("${cargo.stock.serial}")
	private Integer CARGO_STOCK_SERIAL;
	@Value("${cargo.stock.num}")
	private Integer CARGO_STOCK_NUM;
	@Value("${cargo.stock.pcode}")
	private Integer CARGO_STOCK_PCODE;
	@Value("${cargo.stock.pweight}")
	private Integer CARGO_STOCK_PWEIGHT;
	// CARGO
	
	public CargoBean compareCargo(File cargoSupplyFile,File cargoSaleFile,File cargoStockFile,Integer minallot) {
		CompareDao dao = getCompareDao();
		
		
		CargoBean bean = new CargoBean();
		
		try {
			fillCargoBean(dao, bean, 1, cargoSupplyFile, cargoSaleFile, cargoStockFile, minallot);
			fillCargoBean(dao, bean, 2, cargoSupplyFile, cargoSaleFile, cargoStockFile, minallot);
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
	
	private void fillCargoBean(CompareDao dao,CargoBean bean,int stocktype,
			File cargoSupplyFile,File cargoSaleFile,File cargoStockFile,Integer minallot) throws Exception {

			dao.startTransaction();
			// supply
			// 建立临时表
			dao.cargoSupplyCreateTempTable(stocktype);
			
			// 填充数据
			List<String> errList = new ArrayList<String>();
			readCargoSupplyFile(cargoSupplyFile, CARGO_SUPPLY_STARTROW, dao, errList);
			
			
			// sale
			// 建立临时表
			dao.cargoSaleCreateTempTable();
			
			// 填充数据
			readCargoSaleFile(cargoSaleFile, CARGO_SALE_STARTROW, dao, errList,stocktype);
			
			
			// stock
			// 建立临时表
			dao.cargoStockCreateTempTable();
			
			// 填充数据
			readCargoStockFile(cargoStockFile, CARGO_STOCK_STARTROW, dao, errList);
			
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
			
			if(stocktype == 1) {
				bean.setResultStocktype1List(result);
				bean.setStockStocktype1List(stock);
			} else {
				bean.setResultStocktype2List(result);
				bean.setStockStocktype2List(stock);
			}
			if(errList.size() > 0)
				bean.setReserve(new Object[]{errList});

	}

	private void readCargoSupplyFile(File file,int startrow,CompareDao dao,List<String> errList) throws IOException {
		PoiUtil poi = new PoiUtil(file);
		
		while(poi.hasRow()) {
			if(poi.getRowNo() < startrow - 1)
				continue;
			
			try {
				dao.cargoSupplyDataUpdate(poi.getValue(CARGO_SUPPLY_PCODE-1, "").toString(), poi.getValue(CARGO_SUPPLY_SHOPNAME-1, "").toString(), (Double)poi.getValue(CARGO_SUPPLY_PWEIGHT-1, 0));
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
				String serial = (String)poi.getValue(CARGO_SALE_SERIAL-1, "");
				Integer sale = ((Double)poi.getValue(CARGO_SALE_NUM-1, 1)).intValue();
				if(sale < 0) {
					dao.cargoSaleDateDelete(serial);
					continue;
				}
				long saletime;
				try {
					saletime = df.parse((String)poi.getValue(CARGO_SALE_TIME-1,"")).getTime();
				} catch (Exception e) {
					saletime = (new Date()).getTime();
				}
				String shopname = (String)poi.getValue(CARGO_SALE_SHOPNAME-1, "");
				String pcode = (String)poi.getValue(CARGO_SALE_PCODE-1, "");
				Double weight = (Double)poi.getValue(CARGO_SALE_PWEIGHT-1, 0.0);
				
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
				String serial = (String)poi.getValue(CARGO_STOCK_SERIAL-1, "");
				Integer num = ((Double)poi.getValue(CARGO_STOCK_NUM-1, 1)).intValue();
				String pcode = (String)poi.getValue(CARGO_STOCK_PCODE-1, "");
				Double weight = (Double)poi.getValue(CARGO_STOCK_PWEIGHT-1, 0.0);
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
