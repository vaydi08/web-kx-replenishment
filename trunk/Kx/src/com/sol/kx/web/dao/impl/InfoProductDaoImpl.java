package com.sol.kx.web.dao.impl;

import java.sql.SQLException;
import java.sql.Types;
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
import com.sol.kx.web.dao.pojo.StockCheck;

@Repository
public class InfoProductDaoImpl extends BaseDaoImpl implements InfoProductDao {
	@Value("${sql.info.product.find}")
	private String SQL_FINDPRODUCT;
	@Value("${sql.info.product.count}")
	private String SQL_FINDCOUNT;
	
	public List<InfoProduct> find(int page, int pageSize,Condition condition) throws Exception {

		return dataConsole.findByPage(InfoProduct.class, SQL_FINDPRODUCT,condition, page, pageSize);
	}
	
	@Value("${sql.info.product.find.fuzzy}")
	private String SQL_FINDPRODUCT_FUZZY;
	
	public List<InfoProduct> findFuzzy(int page,int pageSize,String value) throws Exception {
		StringBuilder sb = new StringBuilder();

		sb.append("select * from(select top ").append(pageSize).append(" * From (");
		sb.append("select top ").append(page * pageSize).append(SQL_FINDPRODUCT_FUZZY.substring(6)).append(") find1 order by id asc) find2 order by id desc");

		Map<String,Class<?>> smap = new HashMap<String, Class<?>>(8);
		smap.put("id", Integer.class);
		smap.put("type1_cname", String.class);
		smap.put("type2_cname", String.class);
		smap.put("type3_cname", String.class);
		smap.put("type4_cname", String.class);
		smap.put("pname", String.class);
		smap.put("pcode", String.class);
		smap.put("unit", String.class);
		
		List<Object> params = new ArrayList<Object>(2);
		params.add("%" + value + "%");
		params.add("%" + value + "%");
		
		return dataConsole.find(sb.toString(), InfoProduct.class, smap, params);
	}
	
	@Value("${sql.info.product.quickLocator}")
	private String SQL_QUICKLOCATOR;
	
	public List<StockCheck> findQuickLocator(Integer pid) throws Exception {
		Map<String,Class<?>> smap = dataConsole.parseSmap(StockCheck.class, 
				"id","shopname","minweight","maxweight","stock","stocktype");
		List<Object> params = new ArrayList<Object>(1);
		params.add(pid);
		return dataConsole.find(SQL_QUICKLOCATOR,StockCheck.class, smap,params);
	}
	
	public int findCount(Condition condition) throws Exception {
		return dataConsole.findCount(SQL_FINDCOUNT, condition);
	}
	
	@Value("${sql.info.product.count.fuzzy}")
	private String SQL_FINDCOUNT_FUZZY;
	
	public int findCountFuzzy(String value) throws SQLException {
		List<Object> params = new ArrayList<Object>(2);
		params.add("%" + value + "%");
		params.add("%" + value + "%");
		return (Integer)dataConsole.findReturn(SQL_FINDCOUNT_FUZZY, Types.INTEGER,params);
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