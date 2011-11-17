package com.sol.kx.web.dao.impl;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.sol.util.c3p0.DataConsoleAnnotation;
import org.sol.util.c3p0.dataEntity.CountDataEntity;
import org.sol.util.c3p0.dataEntity.SelectDataEntity;
import org.sol.util.c3p0.dataEntity2.CountEntity;
import org.sol.util.c3p0.dataEntity2.Criteria;
import org.sol.util.c3p0.dataEntity2.DeleteEntity;
import org.sol.util.c3p0.dataEntity2.SelectEntity;
import org.sol.util.c3p0.dataEntity2.UpdateEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.StockCheckDao;
import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.dao.pojo.StockCheck;

@Repository
public class StockCheckDaoImpl extends BaseDaoImpl implements StockCheckDao {
	@Value("${sql.stock.product.find.simplelist}")
	private String SQL_PRODUCTLIST;
	
	public List<InfoProduct> findProductList(Map<String,Integer> map,int page,int pageSize) throws Exception {
		SelectDataEntity dataEntity = new SelectDataEntity(InfoProduct.class);

		for(Entry<String, Integer> en : map.entrySet()) 
			dataEntity.eq(en.getKey(), en.getValue());
		
		dataEntity.orderDesc("id");
		
		Map<String, Class<?>> smap = new HashMap<String, Class<?>>(3);
		smap.put("id", Integer.class);
		smap.put("pname", String.class);
		smap.put("pcode", String.class);
		smap.put("image", String.class);
		dataEntity.setSmap(smap);
		
		return super.find(InfoProduct.class,SQL_PRODUCTLIST,dataEntity,page,pageSize,"id");
	}
	
	@Value("${sql.info.product.count}")
	private String SQL_PRODUCTLIST_COUNT;
	
	public int findProductListCount(Map<String,Integer> map) throws Exception {
		CountDataEntity dataEntity = new CountDataEntity(InfoProduct.class);
		
		for(Entry<String, Integer> en : map.entrySet()) 
			dataEntity.eq(en.getKey(), en.getValue());
		
		return super.findCount(SQL_PRODUCTLIST_COUNT, dataEntity);
	}
	
	@Value("${sql.stock.shop.name}")
	private String SQL_SHOP_NAME;
	
	public String findShopNameById(int shopid) throws SQLException {
		return super.findReturnString(SQL_SHOP_NAME, shopid);
	}
	
	@Value("${sql.stock.category.name}")
	private String SQL_CATEGORY_NAME;
	
	public String findCategoryNameById(int id) throws SQLException {
		return super.findReturnString(SQL_CATEGORY_NAME, id);
	}

	@Override
	public List find2(SelectEntity entity) throws Exception {
		entity.getCriteria().order("minweight");
		return super.find2(entity);
	}
	
	@Value("${sql.stock.check.update.select}")
	private String SQL_UPDATE_CHECKSELECT;
	@Value("${sql.stock.check.update}")
	private String SQL_UPDATE_CHECK;

	public void updateStockCheck(StockCheck obj) throws Exception {
		DataConsoleAnnotation dataConsole = new DataConsoleAnnotation(Constants.DB,Constants.TRANS_TIMEOUT);
		
		Criteria select = new Criteria();
		select.eq("id",obj.getId());
		select.eq("idnot",obj.getId());
		Integer needupdateid = (Integer) dataConsole.findReturn(SQL_UPDATE_CHECKSELECT, Types.DOUBLE, select.getParamListWithoutId());
		
		UpdateEntity entity = new UpdateEntity();
		entity.init(obj);
		dataConsole.updatePrepareSQL(entity.getFullSql(), entity.getCriteria().getParamList());
		
		if(needupdateid != null) {
			Criteria criteria = new Criteria();
			criteria.eq("minweight", obj.getMaxweight());
			criteria.eq("id", needupdateid);
			dataConsole.updatePrepareSQL(SQL_UPDATE_CHECK, criteria.getParamListWithoutId());
		}
	}
	
	public void deleteStockCheck(StockCheck obj) throws Exception {
		DataConsoleAnnotation dataConsole = new DataConsoleAnnotation(Constants.DB,Constants.TRANS_TIMEOUT);
		
		Criteria select = new Criteria();
		select.eq("id",obj.getId());
		select.eq("idnot",obj.getId());
		Integer needupdateid = (Integer) dataConsole.findReturn(SQL_UPDATE_CHECKSELECT, Types.DOUBLE, select.getParamListWithoutId());
		
		DeleteEntity entity = new DeleteEntity();
		entity.init(obj);
		List<Object> list = new ArrayList<Object>(1);
		list.add(entity.getCriteria().getId());
		dataConsole.updatePrepareSQL(entity.getFullSql(), list);
		
		if(needupdateid != null) {
			Criteria criteria = new Criteria();
			criteria.eq("minweight", obj.getMinweight());
			criteria.eq("id", needupdateid);
			dataConsole.updatePrepareSQL(SQL_UPDATE_CHECK, criteria.getParamListWithoutId());
		}
	}
	
	
	
	@Value("${sql.stock.checked.find}")
	private String SQL_CHEKCED;
	
	@Override
	public List findByPage2(SelectEntity entity,int page,int pageSize,String order) throws Exception {
		StringBuilder sb = new StringBuilder();

		sb.append("select * from(select top ").append(pageSize).append(" * From (");
		sb.append("select top ").append(page * pageSize).append(SQL_CHEKCED.substring(6)).append(") find1 order by " + order + " asc) find2 order by " + order + " desc");

		Map<String,Class<?>> smap = new HashMap<String, Class<?>>();
		smap.put("id", Integer.class);
		smap.put("pname", String.class);
		smap.put("minweight", Double.class);
		smap.put("maxweight", Double.class);
		smap.put("stock", Integer.class);
		smap.put("stocktype", Short.class);
		
		return dataConsole.find(sb.toString(), entity.getClazz(), smap, entity.getCriteria().getParamList());
	}
	
	@Value("${sql.stock.checked.findcount}")
	private String SQL_CHEKCED_COUNT;
	
	@Override
	public int findCount2(CountEntity entity) throws Exception {
		return (Integer)dataConsole.findReturn(SQL_CHEKCED_COUNT, Types.INTEGER, entity.getCriteria().getParamList());
	}
}
