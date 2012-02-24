package com.sol.kx.web.service.compare;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.common.PoiUtil;
import com.sol.kx.web.dao.compare.CompareMapper;
import com.sol.kx.web.dao.pojo.Compare;
import com.sol.kx.web.service.ExceptionHandler;
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
	
	@Transactional(readOnly = false)
	public void removeSupplyTempTable(Compare obj) {
		compareMapper.removeSupplyTempTable(obj);
	}
	
	
	
	private List<Compare> readSupplyFile(File file,String tablename,List<String> errList) throws IOException {
		PoiUtil poi = new PoiUtil(file);
		
		List<Compare> list = new ArrayList<Compare>(1000);
		SupplyProperties pro = new SupplyProperties();

		while(poi.hasRow()) {
			if(poi.getRowNo() < pro.startrow - 1)
				continue;
			
			try {
				list.add(new Compare(tablename,poi.getValue(pro.pcode-1, "").toString(),(Double)poi.getValue(pro.pweight-1, 0)));
			} catch (Exception e) {
				errList.add("第" + poi.getRowNo() + "行数据导入失败,原因:" + e.getMessage());
			}
		}
		
		poi.close();
		
		return list;
	}
	
	private class SupplyProperties {
		private int startrow;
		private int pcode;
		private int pweight;
		
		public SupplyProperties() {
			loadFromProperties();
		}
		
		// 从配置文件加载
		private void loadFromProperties() {
			InputStream is = this.getClass().getResourceAsStream("/MyBatisConf/Supply.properties");
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
			
			this.startrow = Integer.parseInt(prop.getProperty("supply.startrow","2"));
			this.pcode = Integer.parseInt(prop.getProperty("supply.pcode","6"));
			this.pweight = Integer.parseInt(prop.getProperty("supply.pweight","7"));
		}
	}
}
