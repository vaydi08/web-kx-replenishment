package com.sol.kx.web.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.dao.StockCheckDao;
import com.sol.kx.web.dao.pojo.InfoProduct;
import com.sol.kx.web.dao.pojo.StockCheck;
import com.sol.kx.web.dao.pojo.StockCheckSum;
import com.sol.kx.web.service.StockCheckService;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.bean.ResultBean;

@Service
public class StockCheckServiceImpl extends BaseServiceImpl<StockCheck> implements StockCheckService{

	@Autowired
	private StockCheckDao stockCheckDao;
	
	public PagerBean<InfoProduct> findProducts(PagerBean<InfoProduct> bean,Map<String,Integer> map,int shopid) {
		Logger.SERVICE.ldebug("查询[info_product]数据",bean.getPage(),bean.getPageSize(),
				map.toString());
		try {
			List<String> reserve = new ArrayList<String>(5);
			reserve.add(findShopNameById(shopid));
			Integer typeid = map.get("type1");
			if(typeid != null) reserve.add(findCategoryNameById(typeid));
			typeid = map.get("type2");
			if(typeid != null) reserve.add(findCategoryNameById(typeid));
			typeid = map.get("type3");
			if(typeid != null) reserve.add(findCategoryNameById(typeid));
			typeid = map.get("type4");
			if(typeid != null) reserve.add(findCategoryNameById(typeid));
			
			return setProductBeanValue(bean, 
					stockCheckDao.findProductList(map, bean.getPage(), bean.getPageSize()), 
					stockCheckDao.findProductListCount(map),
					reserve.toArray(new String[0]));
		} catch (Exception e) {
			bean.setException(e);
			exceptionHandler.onDatabaseException("查询[info_product]时的错误", e);
			return bean;
		}
	}
	
	protected PagerBean<InfoProduct> setProductBeanValue(PagerBean<InfoProduct> bean,List<InfoProduct> dataList,int count,String[] reserve) {
		if(bean == null)
			bean = new PagerBean<InfoProduct>();
		
		bean.setException(null);
		bean.setDataList(dataList);
		bean.setCount(count);
		
		bean.setReserve(reserve);
		
		return bean;
	}
	
	protected String findShopNameById(int shopid) {
		Logger.SERVICE.debug("查询[info_shop]名称 " + shopid);
		try {
			return stockCheckDao.findShopNameById(shopid);
		} catch (SQLException e) {
			exceptionHandler.onDatabaseException("查询[info_shop]名称 " + shopid, e);
			return null;
		}
	}
	
	protected String findCategoryNameById(int id) {
		Logger.SERVICE.debug("查询[info_category]名称 " + id);
		try {
			return stockCheckDao.findCategoryNameById(id);
		} catch (SQLException e) {
			exceptionHandler.onDatabaseException("查询[info_category]名称 " + id, e);
			return null;
		}
	}
	
	public StockCheckSum findStockCheckSum(StockCheck obj) {
		Logger.SERVICE.debug("查询[stock_check]数量统计 " + obj.toString());
		try {
			return stockCheckDao.findStockCheckSum(obj.getShopid(), obj.getPid());
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询[stock_check]数量统计 " + obj.toString(), e);
			return null;
		}
	}
	
	@Override
	public ResultBean update2(StockCheck obj) {
		Logger.SERVICE.ldebug("更新[stock_check]数据", obj.toString());
		try {
			stockCheckDao.updateStockCheck(obj);
			return ResultBean.RESULT_SUCCESS(obj);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("更新记录错误", e);
			return ResultBean.RESULT_ERR(e.getMessage(),obj);
		}
	}
	
	@Override
	public ResultBean delete2(StockCheck obj) {
		Logger.SERVICE.ldebug("删除[stock_check]数据", obj.toString());
		try {
			stockCheckDao.deleteStockCheck(obj);
			return ResultBean.RESULT_SUCCESS(obj);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("删除记录错误", e);
			return ResultBean.RESULT_ERR(e.getMessage(),obj);
		}
	}
	
	@Override
	protected BaseDao getDao() {
		return stockCheckDao;
	}
	
}
