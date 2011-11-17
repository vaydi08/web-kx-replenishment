package com.sol.kx.web.dao.impl;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sol.util.c3p0.dataEntity2.Criteria;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.sol.kx.web.dao.InfoProductDao;
import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.dao.pojo.StockCheck;

@Repository
public class InfoProductDaoImpl extends BaseDaoImpl implements InfoProductDao {
	// 查找产品列表
	@Value("${sql.info.product.find}")
	private String SQL_FINDPRODUCT;
	
	public List<InfoProduct> find(int page, int pageSize,Criteria criteria) throws Exception {
		String sql = SQL_FINDPRODUCT + criteria.getWhereSql();
		return super.findByPage2(InfoProduct.class, sql, "info_product", "id", 
				page, pageSize, "p.", dataConsole.parseSmap(InfoProduct.class, 
						"id","type1","type1name","type2","type2name","type3","type3name",
						"type4","type4name","pname","pcode","unit","image"), 
				criteria.getParamListWithoutId());
	}
	
	// 查找是否存在重复记录
	@Value("${sql.info.product.checkexists}")
	private String SQL_FIND_CHECKEXISTS;
	
	public boolean checkExists(String pcode) throws SQLException {
		List<Object> param = new ArrayList<Object>(1);
		param.add(pcode);
		return (Integer)dataConsole.findReturn(SQL_FIND_CHECKEXISTS, Types.INTEGER, param) == 1;
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
		return dataConsole.insertPrepareSQLAndReturnKey(SQL_INSERT_NOTEXISTS,
				infoProduct.getType1(),infoProduct.getType2(),infoProduct.getType3(),
				infoProduct.getType4(),infoProduct.getPname(),infoProduct.getPcode(),
				infoProduct.getImage(),infoProduct.getPcode());
	}
	
//	@Value("${sql.info.product.find.code2id}")
//	private String SQL_CODE2ID;
//	
//	public List<InfoProduct> findCode2Id() throws Exception {
//		Map<String,Class<?>> smap = new HashMap<String, Class<?>>(2);
//		smap.put("id", Integer.class);
//		smap.put("pcode", String.class);
//		
//		return dataConsole.find(SQL_CODE2ID, InfoProduct.class, smap, null);
//	}
	
	// 导出
	@Value("${sql.info.product.export}")
	private String SQL_EXPORT;
	
	public List<InfoProduct> findExport() throws Exception {
		return dataConsole.find(SQL_EXPORT, InfoProduct.class,
				dataConsole.parseSmap(InfoProduct.class, "pname","pcode","image"),
				null);
	}

}
