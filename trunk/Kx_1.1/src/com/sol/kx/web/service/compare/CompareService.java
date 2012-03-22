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
import com.sol.kx.web.common.Logger;
import com.sol.kx.web.common.PoiUtil;
import com.sol.kx.web.dao.compare.CompareMapper;
import com.sol.kx.web.dao.pojo.CargoCompare;
import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.dao.pojo.CompareSupplyExcelProperty;
import com.sol.kx.web.service.ExceptionHandler;
import com.sol.kx.web.service.bean.CargoBean;
import com.sol.kx.web.service.bean.CompareBean;
import com.sol.kx.web.service.bean.PagerBean;

@Service
public class CompareService {
	@Autowired
	private ExceptionHandler exceptionHandler;
	
	@Autowired
	private CompareMapper compareMapper;
	
	@Autowired
	private CompareExcelService compareExcelService;
	
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,timeout = Constants.DB_TIMEOUT)
	public CompareBean compareSupply(File uploadFile,Compare obj) {
//		PagerBean<Compare> bean = new PagerBean<Compare>();
		CompareBean bean = new CompareBean();
		
		try {
			// 创建库存表
			String tablename = compareMapper.createSupplyTempTable();
			obj.setTablename(tablename);
			
			// 读取excel文件
			List<Compare> list = readSupplyFile(uploadFile, tablename);
			
			// 插入数据
			List<Compare> unmatcherList = new ArrayList<Compare>(list.size() / 2);
			for(Compare compare : list) {
				if(compareMapper.insertSupplyTempTable(compare) == 0) {
					// 未匹配产品代码
					unmatcherList.add(compare);
				}
			}
			bean.setUnmatcherList(unmatcherList);
			
			// 比较数据
			bean.setDataList(compareMapper.compareSupply(obj));
			
			// 未核定及其他情况 返回null的部分
			bean.setCompareErrorList(compareMapper.compareSupplyNull(obj));
		} catch (Exception e) {
			exceptionHandler.onExcelException(e.getMessage(), e);
			throw new PersistenceException(e);
		}
		
		return bean;
	}
	
	public InputStream exportDownloadSupply(CompareBean bean) {
		if(bean == null)
			return null;
		
		PoiUtil poi = new PoiUtil();
		
		exportSupplyBeanList(poi, "补货建议单", bean.getDataList());
		if(bean.getUnmatcherList() != null && bean.getUnmatcherList().size() > 0)
			exportSupplyUnmatcherList(poi,"不匹配记录",bean.getUnmatcherList());
		if(bean.getCompareErrorList() != null && bean.getCompareErrorList().size() > 0)
			exportSupplyCompareErrorList(poi,"未核定及其他情况",bean.getCompareErrorList());
		
		return poi.getExcel();
	}
	
	private void exportSupplyBeanList(PoiUtil poi,String sheetname,List<Compare> list) {
		poi.newSheet(sheetname);
		poi.newRow();
		poi.setValuesDefault("产品名称","显示名称","门店名称","产品代码","克重范围","实际库存",
				"一般日 核定库存","一般日 建设补货量","节假日 核定库存","节假日 建设补货量");
		
		for(Compare po : list) {
			poi.newRow();
			poi.setValuesDefault(po.getPname(),po.getDispname(),po.getShopname(),po.getPcode(),
					po.getMinweight() + "-" + po.getMaxweight(),po.getKucun(),
					po.getStock_type1(),po.getNeed_stocktype1(),
					po.getStock_type2(),po.getNeed_stocktype2());
		}
		
		for(int i = 0; i < 10; i ++)
			poi.autoSize(i);
		
		poi.autoFilterOnTitle(10);
	}
	
	private void exportSupplyUnmatcherList(PoiUtil poi,String sheetname,List<Compare> list) {
		poi.newSheet(sheetname);
		poi.newRow();
		poi.setValuesDefault("原EXCEL行数","产品代码","克重","门店名称","显示名称","货品编号");
		
		for(Compare po : list) {
			poi.newRow();
			poi.setValuesDefault(po.getLine(),po.getPcode(),po.getWeight(),
					po.getShopname(),po.getDispname(),po.getSerial());
		}
		
		for(int i = 0; i < 6; i ++)
			poi.autoSize(i);
		
		poi.autoFilterOnTitle(6);
	}
	
	private void exportSupplyCompareErrorList(PoiUtil poi,String sheetname,List<Compare> list) {
		poi.newSheet(sheetname);
		poi.newRow();
		poi.setValuesDefault("产品代码","克重","门店名称","显示名称","货品编号");
		
		for(Compare po : list) {
			poi.newRow();
			poi.setValuesDefault(po.getPcode(),po.getWeight(),
					po.getShopname(),po.getDispname(),po.getSerial());
		}
		
		for(int i = 0; i < 5; i ++)
			poi.autoSize(i);
		
		poi.autoFilterOnTitle(5);
	}
	
	@Transactional(readOnly = false)
	public void removeSupplyTempTable(Compare obj) {
		compareMapper.removeSupplyTempTable(obj);
	}
	
	
	
	
	// 分货比对
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,timeout = Constants.DB_TIMEOUT)
	public CargoBean compareCargo(File cargoSupplyFile,File cargoSaleFile,File cargoStockFile,Integer minallot) {
		CargoBean bean = new CargoBean();
		compareCargo(bean,cargoSupplyFile,cargoSaleFile,cargoStockFile,minallot,1);
		compareCargo(bean,cargoSupplyFile,cargoSaleFile,cargoStockFile,minallot,2);
		return bean;
	}
	
	private void compareCargo(CargoBean bean,File cargoSupplyFile,File cargoSaleFile,File cargoStockFile,Integer minallot,int stocktype) {

			List<String> errList = new ArrayList<String>(100);

			// 生成临时表
			String supplyTablename = compareMapper.createCargoSupplyTempTable();
			String saleTablename = compareMapper.createCargoSaleTempTable();
			String stockTablename = compareMapper.createCargoStockTempTable();
			
			// 读取EXCEL
			List<CargoCompare> supplyList = null;
			List<CargoCompare> saleList = null;
			List<CargoCompare> stockList = null;
			try {
				supplyList = readCargoSupplyFile(cargoSupplyFile, supplyTablename, errList);
				saleList = readCargoSaleFile(cargoSaleFile, saleTablename, errList);
				stockList = readCargoStockFile(cargoStockFile, stockTablename, errList);
			} catch (Exception e) {
				throw new PersistenceException();
			}
			
			// 插入EXCEL数据到临时表
			for(CargoCompare obj : supplyList)
				compareMapper.updateCargoSupply(obj);
			for(CargoCompare obj : saleList)
				compareMapper.insertCargoSale(obj);
			for(CargoCompare obj : stockList)
				compareMapper.insertCargoStock(obj);
			
			// 比对
			String cargoCompareTablename = compareMapper.createCargoCompareTempTable(supplyTablename,saleTablename,minallot);
			List<CargoCompare> compareStockList = compareMapper.selectCargoStock(stockTablename);
			
			for(Iterator<CargoCompare> it = compareStockList.iterator(); it.hasNext();) {
				CargoCompare compareStock = it.next();
				compareStock.setTablename(cargoCompareTablename);
				CargoCompare compare = compareMapper.selectCargoCompare(compareStock);
				if(compare.getNum() > 0) {
					compareStock.setShopname(compare.getShopname());
					compareStock.setTablename(stockTablename);
					compareMapper.updateCargoStock(compareStock);
					it.remove();
				}
			}
			
			for(Iterator<CargoCompare> it = compareStockList.iterator(); it.hasNext();) {
				CargoCompare compareStock = it.next();
				compareStock.setMinallot(-1);
				compareStock.setTablename(cargoCompareTablename);
				CargoCompare compare = compareMapper.selectCargoCompare(compareStock);
				if(compare.getNum() > 0) {
					compareStock.setShopname(compare.getShopname());
					compareStock.setTablename(stockTablename);
					compareMapper.updateCargoStock(compareStock);
					it.remove();
				}
			}
			
			List<CargoCompare> result = compareMapper.selectCargoCompareResult(cargoCompareTablename);
			List<CargoCompare> stocks = compareMapper.selectCargoStock(stockTablename);
			
			if(stocktype == 1) {
				bean.setResultStocktype1List(result);
				bean.setStockStocktype1List(stocks);
			} else {
				bean.setResultStocktype2List(result);
				bean.setStockStocktype2List(stocks);
			}
			if(errList.size() > 0)
				bean.setReserve(new Object[]{errList});

	}
	
	private List<CargoCompare> readCargoSupplyFile(File file,String tablename,List<String> errList) throws IOException {
		PoiUtil poi = new PoiUtil(file);
		
		List<CargoCompare> list = new ArrayList<CargoCompare>(1000);
		CompareSupplyExcelProperty pro = compareExcelService.loadSupplyProperty();
		
		while(poi.hasRow()) {
			if(poi.getRowNo() < pro.getStartrow() - 1)
				continue;
			
			try {
				list.add(new CargoCompare(tablename,poi.getValue(pro.getPcode()-1, "").toString(),
						poi.getValue(pro.getShopname() - 1,"").toString(), (Double)poi.getValue(pro.getPweight()-1, 0)));
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
	
	
	
	private List<Compare> readSupplyFile(File file,String tablename) throws IOException {
		PoiUtil poi = new PoiUtil(file);
		
		List<Compare> list = new ArrayList<Compare>(1000);
		CompareSupplyExcelProperty pro = compareExcelService.loadSupplyProperty();

		while(poi.hasRow()) {
			if(poi.getRowNo() < pro.getStartrow() - 1)
				continue;
			
			try {
				list.add(new Compare(
						poi.getRowNo() + 1,tablename,
						poi.getValue(pro.getPcode()-1, "").toString(),(Double)poi.getValue(pro.getPweight()-1, 0),
						poi.getValue(pro.getShopname() - 1,"").toString(),
						poi.getValue(pro.getDispname() - 1, "").toString(),poi.getValue(pro.getSerial() - 1,"").toString()));
			} catch (Exception e) {
				//errList.add("第" + poi.getRowNo() + "行数据导入失败,原因:" + e.getMessage());
				Logger.SERVICE.error("第" + poi.getRowNo() + "行数据导入失败,原因:" + e.getMessage());
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
