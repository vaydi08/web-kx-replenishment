package com.sol.kx.web.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sol.util.c3p0.DataConsoleAnnotation;
import org.sol.util.c3p0.dataEntity.CountDataEntity;
import org.sol.util.c3p0.dataEntity.SelectDataEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.InfoCategoryDao;
import com.sol.kx.web.dao.pojo.InfoCategory;

@Repository
public class InfoCategoryDaoImpl extends BaseDaoImpl implements InfoCategoryDao {
	@Value("${sql.info.category.find.type}")
	private String SQL_CATEGORY_TYPE;
	
	public List<InfoCategory> findCategoryType(int clevel) throws Exception {
		DataConsoleAnnotation dataConsole = new DataConsoleAnnotation(Constants.DB, Constants.TRANS_TIMEOUT);
		Map<String,Class<?>> smap = new HashMap<String, Class<?>>(3);
		smap.put("id",Integer.class);
		smap.put("ccode",String.class);
		smap.put("cname",String.class);
		
		List<Object> list = new ArrayList<Object>(1);
		list.add(clevel);
		return dataConsole.find(SQL_CATEGORY_TYPE,InfoCategory.class, smap, list);
	}

	@Value("${sql.info.category.find.mapping}")
	private String SQL_CATEGORY_MAPPING;
	
	public List<InfoCategory> findCategoryMapping() throws Exception {
		Map<String,Class<?>> smap = new HashMap<String, Class<?>>(2);
		smap.put("id",Integer.class);
		smap.put("ccode",String.class);
		smap.put("clevel", Integer.class);
		
		List<Object> list = null;
		
		return find(SQL_CATEGORY_MAPPING,InfoCategory.class,smap,list);
	}

	public List<InfoCategory> findCustom(Class<InfoCategory> clazz, InfoCategory obj, int page, int pageSize,
			String order) throws Exception {
		SelectDataEntity dataEntity = new SelectDataEntity(clazz);
		dataEntity.orderDesc("id");
		
		if(obj.getCcode() != null)
			dataEntity.like("ccode", obj.getCcode());
		if(obj.getCname() != null)
			dataEntity.like("cname", obj.getCname());
		
		return dataConsole.findByPage(clazz, dataEntity, page, pageSize, order);
	}
	
	public int findCountCustom(InfoCategory obj) throws Exception {
		CountDataEntity dataEntity = new CountDataEntity(InfoCategory.class);
		
		if(obj.getCcode() != null)
			dataEntity.like("ccode", obj.getCcode());
		if(obj.getCname() != null)
			dataEntity.like("cname", obj.getCname());
		
		return dataConsole.findCount(dataEntity);
	}
}
