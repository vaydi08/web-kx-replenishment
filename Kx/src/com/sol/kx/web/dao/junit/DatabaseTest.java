package com.sol.kx.web.dao.junit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sol.kx.web.dao.CompareDao;
import com.sol.kx.web.service.util.PoiUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class DatabaseTest {

	@Autowired
	CompareDao compareDao;
	
	@Test
	public void test() throws ParseException {
		try {
//			File file = new File("f:/cargo_sale.XLS");
//			FileInputStream is = new FileInputStream(file);
//			
//			//HSSFWorkbook wb = new HSSFWorkbook(is);
//
//			PoiUtil poi = new PoiUtil(file);
//			poi.hasRow();
//			poi.hasRow();
//			
//			DateFormat df = new SimpleDateFormat("dd-MMM-yy HH:mm:ss",Locale.ENGLISH);
//			
//			System.out.println(poi.getValue(0, ""));
//			System.out.println(poi.getValue(1, ""));
//			System.out.println(df.parse((String) poi.getValue(5, "")));
//			System.out.println(poi.getValue(58, ""));
//			poi.close();
			
			compareDao.cargoSaleCreateTempTable();
			compareDao.cargoSaleDataUpdate("0001286738", 1, 1305610058000l, "金桥国际", "26ty01", 6.45, 1);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
