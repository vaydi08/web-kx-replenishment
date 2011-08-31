package com.sol.kx.web.dao.impl;

import java.util.List;

import org.sol.util.c3p0.Condition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.sol.kx.web.dao.InfoProductDao;
import com.sol.kx.web.dao.pojo.InfoProduct;

@Repository
public class InfoProductDaoImpl extends BaseDaoImpl implements InfoProductDao {
	@Value("${sql.info.product.find}")
	private String SQL_FINDPRODUCT;
	@Value("${sql.info.product.count}")
	private String SQL_FINDCOUNT;
	
	public List<InfoProduct> find(int page, int pageSize,Condition condition) throws Exception {

		return dataConsole.findByPage(InfoProduct.class, SQL_FINDPRODUCT,condition, page, pageSize);
	}
	
	public int findCount(Condition condition) throws Exception {
		return dataConsole.findCount(SQL_FINDCOUNT, condition);
	}

}
