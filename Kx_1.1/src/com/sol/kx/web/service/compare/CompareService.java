package com.sol.kx.web.service.compare;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.common.PoiUtil;
import com.sol.kx.web.dao.compare.CompareMapper;
import com.sol.kx.web.dao.pojo.CargoCompare;
import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.service.ExceptionHandler;
import com.sol.kx.web.service.bean.CargoBean;
import com.sol.kx.web.service.bean.PagerBean;

@Service
public class CompareService {
	@Autowired
	private ExceptionHandler exceptionHandler;
	
	@Autowired
	private CompareMapper compareMapper;
	
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,timeout = Constants.DB_TIMEOUT)
	public PagerBean<Compare> compareSupply(File uploadFile,Compare obj) {
		PagerBean<Compare> bean = new PagerBean<Compare>();
		
		try {
			List<String> errList = new ArrayList<String>();
			
			String tablename = compareMapper.createSupplyTempTable();
			obj.setTablename(tablename);
			
			List<Compare> list = readSupplyFile(uploadFile, tablename, errList);
			for(Compare compare : list)
				compareMapper.insertSupplyTempTable(compare);
			
			List<Compare> datalist = compareMapper.compareSupply(obj);
			bean.setDataList(datalist);
			bean.setReserve(new Object[]{errList});
		} catch (Exception e) {
			exceptionHandler.onExcelException(e.getMessage(), e);
			throw new PersistenceException();
		}
		
		return bean;
	}
	
	public InputStream exportDownloadSupply(PagerBean<Compare> bean) {
		if(bean == null)
			return null;
		
		PoiUtil poi = new PoiUtil();
		
		exportSupplyBeanList(poi, "补货建议单", bean.getDataList());
		
		return poi.getExcel();
	}
	
	private void exportSupplyBeanList(PoiUtil poi,String sheetname,List<Compare> list) {
		poi.newSheet(sheetname);
		poi.newRow();
		poi.setValue(0, "产品名称");
		poi.setValue(1, "产品代码");
		poi.setValue(2, "克重范围");
		poi.setValue(3, "实际库存");
		poi.setValue(4, "一般日 核定库存");
		poi.setValue(5, "一般日 建设补货量");
		poi.setValue(6, "节假日 核定库存");
		poi.setValue(7, "节假日 建设补货量");
		
		for(Compare po : list) {
			poi.newRow();
			poi.setValue(0, po.getPname());
			poi.setValue(1, po.getPcode());
			poi.setValue(2, po.getMinweight() + "-" + po.getMaxweight());
			poi.setValue(3, po.getKucun());
			poi.setValue(4, po.getStock_type1());
			poi.setValue(5, po.getNeed_stocktype1());
			poi.setValue(6, po.getStock_type2());
			poi.setValue(7, po.getNeed_stocktype2());
		}
		
		for(int i = 0; i < 8; i ++)
			poi.autoSize(i);
	}
	
	@Transactional(readOnly = false)
	public void removeSupplyTempTable(Compare obj) {
		compareMapper.removeSupplyTempTable(obj);
	}
	
	
	
	
	// 分货比对
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,timeout = Constants.DB_TIMEOUT)
	public CargoBean compareCargo(File cargoSupplyFile,File cargoSaleFile,File cargoStockFile,Integer minallot) {
		CargoBean bean = new CargoBean();
		
		try {
			List<String> errList = new ArrayList<String>(100);
			
//			fillCargoBean(dao, bean, 1, cargoSupplyFile, cargoSaleFile, cargoStockFile, minallot);
//			fillCargoBean(dao, bean, 2, cargoSupplyFile, cargoSaleFile, cargoStockFile, minallot);
			
			String supplyTablename = compareMapper.createCargoSupplyTempTable();
			String saleTablename = compareMapper.createCargoSaleTempTable();
			String stockTablename = compareMapper.createCargoStockTempTable();
			
			List<CargoCompare> supplyList = readCargoSupplyFile(cargoSupplyFile, supplyTablename, errList);
			List<CargoCompare> saleList = readCargoSaleFile(cargoSaleFile, saleTablename, errList);
			List<CargoCompare> stockList = readCargoStockFile(cargoStockFile, stockTablename, errList);
			
			for(CargoCompare obj : supplyList)
				compareMapper.updateCargoSupply(obj);
			for(CargoCompare obj : saleList)
				compareMapper.insertCargoSale(obj);
			for(CargoCompare obj : stockList)
				compareMapper.insertCargoStock(obj);
			
			return bean;
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("处理分货数据时产生错误","", e);
			bean.setException(new Exception("数据库错误:" + e.getMessage(),e));
			throw new PersistenceException();
		}
	}
	
	private List<CargoCompare> readCargoSupplyFile(File file,String tablename,List<String> errList) throws IOException {
		PoiUtil poi = new PoiUtil(file);
		
		List<CargoCompare> list = new ArrayList<CargoCompare>(1000);
		SupplyProperties pro = new SupplyProperties();
		
		while(poi.hasRow()) {
			if(poi.getRowNo() < pro.i_startrow - 1)
				continue;
			
			try {
				list.add(new CargoCompare(tablename,poi.getValue(pro.i_pcode-1, "").toString(),poi.getValue(pro.i_shopname - 1,"").toString(), (Double)poi.getValue(pro.i_pweight-1, 0)));
			} catch (Exception e) {
				errList.add("第" + poi.getRowNo() + "行数据导入失败,原因:" + e.getMessage());
			}
		}
		
		poi.close();
		
		return list;
	}
	
	private List<CargoCompare> readCargoSaleFile(File file,String tablename,List<String> errList) throws IOException {
		PoiUtil poi = new PoiUtil(file);

		DateFormat df = new SimpleDateFormat("dd-MMM-yy HH:mm:ss",Locale.ENGLISH);
		
		List<CargoCompare> list = new LinkedList<CargoCompare>();
		SaleProperties pro = new SaleProperties();
		
		while(poi.hasRow()) {
			if(poi.getRowNo() < pro.i_startrow - 1)
				continue;
			
			try {
				String serial = (String)poi.getValue(pro.i_serial - 1, "");
				Integer sale = ((Double)poi.getValue(pro.i_num - 1, 1)).intValue();
				if(sale < 0) {
					for(Iterator<CargoCompare> it = list.iterator(); it.hasNext();) {
						CargoCompare obj = it.next();
						if(obj.getSerial().equals(serial)) {
							it.remove();
							break;
						}
					}
					continue;
				}
				Timestamp saletime;
				try {
					saletime = new Timestamp(df.parse((String)poi.getValue(pro.i_saletime-1,"")).getTime());
				} catch (Exception e) {
					saletime = new Timestamp((new Date()).getTime());
				}
				String shopname = (String)poi.getValue(pro.i_shopname - 1, "");
				String pcode = (String)poi.getValue(pro.i_pcode - 1, "");
				Double weight = (Double)poi.getValue(pro.i_pweight - 1, 0.0);
				
				list.add(new CargoCompare(tablename, serial, sale, saletime,shopname,pcode,weight));

			} catch (Exception e) {
				errList.add("销售数据 - 第" + poi.getRowNo() + "行数据导入失败,原因:" + e.getMessage());
			}
		}
		
		poi.close();
		
		return list;
	}
	
	private List<CargoCompare> readCargoStockFile(File file,String tablename,List<String> errList) throws IOException {
		PoiUtil poi = new PoiUtil(file);
		
		List<CargoCompare> list = new ArrayList<CargoCompare>(1000);
		StockProperties pro = new StockProperties();
		
		while(poi.hasRow()) {
			if(poi.getRowNo() < pro.i_startrow - 1)
				continue;
			
			try {
				String serial = (String)poi.getValue(pro.i_serial - 1, "");
				Integer num = ((Double)poi.getValue(pro.i_num - 1, 1)).intValue();
				String pcode = (String)poi.getValue(pro.i_pcode - 1, "");
				Double weight = (Double)poi.getValue(pro.i_pweight - 1, 0.0);
				list.add(new CargoCompare(tablename,serial,num,pcode,weight));
			} catch (Exception e) {
				errList.add("进货数据 - 第" + poi.getRowNo() + "行数据导入失败,原因:" + e.getMessage());
			}
		}
		
		poi.close();
		
		return list;
	}
	
	
	
	private List<Compare> readSupplyFile(File file,String tablename,List<String> errList) throws IOException {
		PoiUtil poi = new PoiUtil(file);
		
		List<Compare> list = new ArrayList<Compare>(1000);
		SupplyProperties pro = new SupplyProperties();

		while(poi.hasRow()) {
			if(poi.getRowNo() < pro.i_startrow - 1)
				continue;
			
			try {
				list.add(new Compare(tablename,poi.getValue(pro.i_pcode-1, "").toString(),(Double)poi.getValue(pro.i_pweight-1, 0)));
			} catch (Exception e) {
				errList.add("第" + poi.getRowNo() + "行数据导入失败,原因:" + e.getMessage());
			}
		}
		
		poi.close();
		
		return list;
	}
	
	private class ExcelColumnProperties {
		protected void loadFromProperties(String filename) {
			InputStream is = this.getClass().getResourceAsStream("/MyBatisConf/" + filename);
			Properties prop = new Properties();
			try {
				prop.load(is);
			} catch (IOException e) {
				exceptionHandler.onExcelException(e.getMessage(), e);
			} finally {
				try {
					if(is != null)
						is.close();
				} catch (IOException e) {
				}
			}
			
			Field[] fields = this.getClass().getDeclaredFields();
			for(Field field : fields) {
				try {
					field.setAccessible(true);
					if(field.getName().startsWith("i_"))
						field.set(this, Integer.parseInt(prop.getProperty(field.getName())));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private class SupplyProperties extends ExcelColumnProperties {
		private int i_startrow;
		private int i_pcode;
		private int i_pweight;
		private int i_shopname;
		
		public SupplyProperties() {
			loadFromProperties("Supply.properties");
		}
	}
	private class SaleProperties extends ExcelColumnProperties {
		private int i_startrow;
		private int i_serial;
		private int i_num;
		private int i_saletime;
		private int i_shopname;
		private int i_pcode;
		private int i_pweight;
		
		public SaleProperties() {
			loadFromProperties("Sale.properties");
		}
	}
	private class StockProperties extends ExcelColumnProperties {
		private int i_startrow;
		private int i_serial;
		private int i_num;
		private int i_pcode;
		private int i_pweight;
		
		public StockProperties() {
			loadFromProperties("Stock.properties");
		}
	}
}
