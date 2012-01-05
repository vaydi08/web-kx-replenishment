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
import com.sol.kx.web.dao.pojo.StockCheckSum;
import com.sol.kx.web.dao.pojo.StockChecked;

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
	
	
	// 查询核定统计数
	@Value("${sql.stock.check.sum}")
	private String SQL_SUM;
	
	public StockCheckSum findStockCheckSum(Integer shopid,Integer pid) throws Exception {
		List<Object> param = new ArrayList<Object>();
		param.add(pid);
		param.add(shopid);
		param.add(shopid);
		param.add(pid);
		param.add(shopid);
		param.add(shopid);
		
		return dataConsole.get(StockCheckSum.class, SQL_SUM, 
				dataConsole.parseSmap(StockCheckSum.class, 
						"shop_stocktype1","shop_stocktype2",
						"shop_product_stocktype1","shop_product_stocktype2",
						"sum_type1_stocktype1","sum_type1_stocktype2",
						"sum_type2_stocktype1","sum_type2_stocktype2"), param);
	}

	// 核定数 复制
	@Value("${sql.stock.check.copy}")
	private String SQL_CHECK_COPY;
	
	public void copyCheck(Integer pid,Integer shopid,Integer clevel) throws SQLException {
		List<Object> list = new ArrayList<Object>(2);
		list.add(pid);
		list.add(shopid);
		
		dataConsole.updatePrepareSQL(SQL_CHECK_COPY.replaceAll(":clevel", clevel.toString()), list);
	}
	
	// 已作核定统计
	@Value("${sql.stock.checked.type1}")
	private String SQL_CHECKED_TYPE1;
	@Value("${sql.stock.checked.type234}")
	private String SQL_CHECKED_TYPE234;
	
	public List<StockChecked> findCheckedType1(Integer shopid) throws Exception {
		List<Object> list = new ArrayList<Object>(1);
		list.add(shopid);
		
		return dataConsole.find(SQL_CHECKED_TYPE1,StockChecked.class,
				dataConsole.parseSmap(StockChecked.class, 
						"stock_type1","sum_stock_type1","stock_type2","sum_stock_type2",
						"ptype","cname"),list);
	}
	
	public List<StockChecked> findCheckedType234(Integer shopid,Integer parent,Integer clevel,Integer parentlevel) throws Exception {
		List<Object> list = new ArrayList<Object>(1);
		list.add(shopid);
		list.add(parent);
		
		return dataConsole.find(
				SQL_CHECKED_TYPE234.replaceAll(":clevel", clevel.toString()).replace(":parentlevel",parentlevel.toString()),
				StockChecked.class,
				dataConsole.parseSmap(StockChecked.class, 
						"stock_type1","sum_stock_type1","stock_type2","sum_stock_type2",
						"ptype","cname"),list);
	}
}
