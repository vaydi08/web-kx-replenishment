package com.sol.kx.web.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sol.util.c3p0.DataConsoleAnnotation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.InfoCategoryDao;
import com.sol.kx.web.dao.pojo.InfoCategory;

@Repository
public class InfoCategoryDaoImpl extends BaseDaoImpl implements InfoCategoryDao {
	@Value("${sql.info.category.find.type}")
	private String SQL_CATEGORY_TYPE;
	
	/* (non-Javadoc)
	 * @see com.sol.kx.web.dao.impl.InfoCategoryDao#findCategoryType(int)
	 */
	public List<InfoCategory> findCategoryType(int clevel) throws Exception {
		DataConsoleAnnotation dataConsole = new DataConsoleAnnotation(Constants.DB,Constants.TRANS_TIMEOUT);
		Map<String,Class<?>> smap = new HashMap<String, Class<?>>(2);
		smap.put("id",Integer.class);
		smap.put("cname",String.class);
		
		List<Object> list = new ArrayList<Object>(1);
		list.add(clevel);
		return dataConsole.find(SQL_CATEGORY_TYPE,InfoCategory.class, smap, list);
	}

}
