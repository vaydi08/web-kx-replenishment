package com.sol.kx.web.service.compare;

import java.lang.reflect.Method;
import java.util.List;

import org.sol.util.common.StringUtil;
import org.sol.util.mybatis.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.kx.web.dao.compare.CompareExcelPropertyMapper;
import com.sol.kx.web.dao.pojo.CompareExcelProperty;
import com.sol.kx.web.dao.pojo.CompareSupplyExcelProperty;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.bean.PagerBean;

@Service
public class CompareExcelService extends BaseService{

	@Autowired
	private CompareExcelPropertyMapper compareExcelPropertyMapper;
	
	public CompareSupplyExcelProperty loadSupplyProperty() {
		List<CompareExcelProperty> list = compareExcelPropertyMapper.loadProperty("库存表");
		
		return loadPropertyFromDatabase(CompareSupplyExcelProperty.class, list);
	}
	
	public void selectProperty(PagerBean<CompareExcelProperty> bean,CompareExcelProperty input) {
		bean.setDataList(compareExcelPropertyMapper.loadProperty(input.getCategory()));
	}
	
	private <T> T loadPropertyFromDatabase(Class<T> clazz,List<CompareExcelProperty> list) {
		T obj = null;
		try {
			obj = clazz.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
		
		for(CompareExcelProperty pro : list) {
			String name = pro.getName();
			Integer col = pro.getCol();
			
			try {
				Method method = clazz.getDeclaredMethod(StringUtil.setMethod(name),Integer.class);
				method.invoke(obj,col);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return obj;
	}

	@Override
	protected BaseMapper injectMapper() {
		return compareExcelPropertyMapper;
	}
}
