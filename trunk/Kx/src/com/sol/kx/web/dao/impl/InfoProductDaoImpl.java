package com.sol.kx.web.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.sol.util.c3p0.Condition;
import org.sol.util.c3p0.DataConsoleUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.InfoProductDao;
import com.sol.kx.web.dao.pojo.InfoProduct;

@Repository
public class InfoProductDaoImpl extends BaseDaoImpl implements InfoProductDao {
	@Value("${sql.info.product.find}")
	private String SQL_FINDPRODUCT;
	
	@Value("${sql.info.product.count}")
	private String SQL_FINDPRODUCT_COUNT;
	
	/* (non-Javadoc)
	 * @see com.sol.kx.web.dao.impl.InfoProductDao#findProducts(int, int, org.sol.util.c3p0.Condition)
	 */
	public List<InfoProduct> findProducts(int page,int pageSize,Condition condition) throws Exception {
		Map<String,Class<?>> smap = DataConsoleUtil.getClassDefine(InfoProduct.class,"type");
		smap.put("type1_id", Integer.class);
		smap.put("type1_cname", String.class);
		smap.put("type2_id", Integer.class);
		smap.put("type2_cname", String.class);
		smap.put("type3_id", Integer.class);
		smap.put("type3_cname", String.class);
		smap.put("type4_id", Integer.class);
		smap.put("type4_cname", String.class);
		if(condition == null) 
			return super.findByPage(Constants.DB, InfoProduct.class, SQL_FINDPRODUCT,
					page,pageSize, smap);
		else
			return super.findByPage(Constants.DB, InfoProduct.class, SQL_FINDPRODUCT + condition.getWhere(),
				page,pageSize, smap,condition.getParams());
	}

	public int findProductsCount(Condition condition) throws SQLException {
		if(condition == null)
			return super.findReturn(Constants.DB, SQL_FINDPRODUCT_COUNT);
		else
			return super.findReturn(Constants.DB, SQL_FINDPRODUCT_COUNT + condition.getWhere(), condition.getParams());
	}
}
