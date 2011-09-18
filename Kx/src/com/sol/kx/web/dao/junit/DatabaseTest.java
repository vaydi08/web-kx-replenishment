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
import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.dao.pojo.InfoShop;
import com.sol.kx.web.dao.pojo.StockCheck;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class DatabaseTest {
	@Autowired
	private StockCheckDao dao;

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
		StockCheck obj = new StockCheck();
		//obj.setId(3);
		obj.setPid(1);
		obj.setShopid(2);
		

		SelectEntity select = new SelectEntity();
		CountEntity count = new CountEntity();
		InsertEntity insert = new InsertEntity();
		UpdateEntity update = new UpdateEntity();
		DeleteEntity delete = new DeleteEntity();
		
		try {
			select.init(obj);
			select.getCriteria().order("id");
			System.out.println(select.getFullSql());
			System.out.println(select.getCriteria().getParamList());
			System.out.println(select.getSmap());
			
			count.init(obj);
			System.out.println(count.getFullSql());
			System.out.println(count.getCriteria().getParamList());
			
			insert.init(obj);
			System.out.println(insert.getFullSql());
			System.out.println(insert.getCriteria().getParamListWithoutId());
			
			update.init(obj);
			System.out.println(update.getFullSql());
			System.out.println(update.getCriteria().getParamList());
			
			
			delete.init(obj);
			System.out.println(delete.getFullSql());
			System.out.println(delete.getCriteria().getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
