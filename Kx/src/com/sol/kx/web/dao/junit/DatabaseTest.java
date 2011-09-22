package com.sol.kx.web.dao.junit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sol.util.c3p0.Condition;
import org.sol.util.c3p0.dataEntity.CountDataEntity;
import org.sol.util.c3p0.dataEntity.DeleteDataEntity;
import org.sol.util.c3p0.dataEntity.InsertDataEntity;
import org.sol.util.c3p0.dataEntity.SelectDataEntity;
import org.sol.util.c3p0.dataEntity.UpdateDataEntity;
import org.sol.util.c3p0.dataEntity2.CountEntity;
import org.sol.util.c3p0.dataEntity2.Criteria;
import org.sol.util.c3p0.dataEntity2.DeleteEntity;
import org.sol.util.c3p0.dataEntity2.InsertEntity;
import org.sol.util.c3p0.dataEntity2.SelectEntity;
import org.sol.util.c3p0.dataEntity2.UpdateEntity;
import org.sol.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sol.kx.web.dao.InfoProductDao;
import com.sol.kx.web.dao.StockCheckDao;
import com.sol.kx.web.dao.StockCheckedDao;
import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.dao.pojo.InfoShop;
import com.sol.kx.web.dao.pojo.StockCheck;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class DatabaseTest {
	@Autowired
	private StockCheckedDao dao;

	//@Test
	public void test2() {
		InfoProduct po = new InfoProduct();
		
		String a = "getAsdf";
		System.out.println(a.replaceFirst("get([A-Z])", "$1"));
	}
	
	private void setField(Object obj,String fieldname,Object value) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException  {
		if(fieldname.indexOf('.') > 0) {
			String thisfieldname = fieldname.substring(0,fieldname.indexOf('.'));
			Method thismethod = obj.getClass().getMethod(StringUtil.getMethod(thisfieldname));
			setField(thismethod.invoke(obj),fieldname.substring(fieldname.indexOf('.') + 1),value);
		} else {
			Method thismethod = obj.getClass().getMethod(StringUtil.setMethod(fieldname),value.getClass());
			thismethod.invoke(obj, value);
		}
	}


	
	@Test
	public void test() {
		dao.test();
	}
}
