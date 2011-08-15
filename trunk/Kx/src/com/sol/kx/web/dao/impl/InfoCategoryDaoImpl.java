package com.sol.kx.web.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		Map<String,Class<?>> smap = new HashMap<String, Class<?>>(2);
		smap.put("id",Integer.class);
		smap.put("cname",String.class);
		
		return super.find(Constants.DB, InfoCategory.class, SQL_CATEGORY_TYPE, smap, clevel);
	}
}
