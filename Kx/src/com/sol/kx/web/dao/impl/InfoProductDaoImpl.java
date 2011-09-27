package com.sol.kx.web.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sol.util.c3p0.Condition;
import org.sol.util.c3p0.dataEntity2.InsertEntity;
import org.sol.util.c3p0.dataEntity2.SelectEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.sol.kx.web.dao.InfoProductDao;
import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.dao.pojo.InfoProductDetail;

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
	
	@Value("${sql.info.product.insert.notexists}")
	private String SQL_INSERT_NOTEXISTS;
	
	public int addNotExists(InfoProduct infoProduct) throws SQLException {
		List<Object> list = new ArrayList<Object>(8);
		list.add(infoProduct.getType1().getId());
		list.add(infoProduct.getType2().getId());
		list.add(infoProduct.getType3().getId());
		list.add(infoProduct.getType4().getId());
		list.add(infoProduct.getPname());
		list.add(infoProduct.getPcode());
		list.add(infoProduct.getUnit());
		
		return dataConsole.insertPrepareSQLAndReturnKey(SQL_INSERT_NOTEXISTS,
				infoProduct.getType1().getId(),infoProduct.getType2().getId(),infoProduct.getType3().getId(),
				infoProduct.getType4().getId(),infoProduct.getPname(),infoProduct.getPcode(),infoProduct.getUnit());
	}
	
	@Value("${sql.info.product.find.code2id}")
	private String SQL_CODE2ID;
	
	public List<InfoProduct> findCode2Id() throws Exception {
		Map<String,Class<?>> smap = new HashMap<String, Class<?>>(2);
		smap.put("id", Integer.class);
		smap.put("pcode", String.class);
		
		return dataConsole.find(SQL_CODE2ID, InfoProduct.class, smap, null);
	}
	
	
	public int addProductDetail(InsertEntity entity) throws Exception {		
		return dataConsole.updatePrepareSQL(entity.getFullSql(), entity.getCriteria().getParamList());
	}

	
	public List<InfoProductDetail> findProductDetails(SelectEntity entity) throws Exception {
		return find2(entity);
	}
}
