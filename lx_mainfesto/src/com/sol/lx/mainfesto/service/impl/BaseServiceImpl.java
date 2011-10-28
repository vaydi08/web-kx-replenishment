package com.sol.lx.mainfesto.service.impl;

import java.util.List;

import javax.persistence.Table;

import org.sol.util.c3p0.dataEntity2.CountEntity;
import org.sol.util.c3p0.dataEntity2.DeleteEntity;
import org.sol.util.c3p0.dataEntity2.InsertEntity;
import org.sol.util.c3p0.dataEntity2.SelectEntity;
import org.sol.util.c3p0.dataEntity2.UpdateEntity;
import org.springframework.beans.factory.annotation.Autowired;

import com.sol.lx.mainfesto.common.Logger;
import com.sol.lx.mainfesto.dao.BaseDao;
import com.sol.lx.mainfesto.service.BaseService;
import com.sol.lx.mainfesto.service.bean.PagerBean;
import com.sol.lx.mainfesto.service.bean.ResultBean;


public abstract class BaseServiceImpl<T> implements BaseService<T>{

	protected abstract BaseDao getDao();
	
	@Autowired
	protected ExceptionHandler exceptionHandler;
	
	protected PagerBean<T> setBeanValue(PagerBean<T> bean,List<T> dataList,int count) {
		if(bean == null)
			bean = new PagerBean<T>();
		
		bean.setException(null);
		bean.setDataList(dataList);
		bean.setCount(count);
		
		return bean;
	}
	
	
	
	/**
	 * SERVER公用函数
	 */
	public PagerBean<T> find(T obj) {
		Logger.SERVICE.ldebug("查询[" + obj.getClass().getAnnotation(Table.class).name() + "]数据",obj.toString());
		PagerBean<T> bean = new PagerBean<T>();
		try {
			SelectEntity selectEntity = new SelectEntity();
			selectEntity.init(obj);
			bean.setDataList((List<T>) getDao().find(selectEntity));
			return bean;
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询" + obj.getClass().getAnnotation(Table.class).name() + "错误", e);
			bean.setException(e);
			return bean;
		}
	}
	
	public PagerBean<T> findByPage(PagerBean<T> bean,T obj) {
		Logger.SERVICE.ldebug("查询[" + obj.getClass().getAnnotation(Table.class).name() + "]数据",obj.toString());
		try {
			SelectEntity selectEntity = new SelectEntity();
			selectEntity.init(obj);
			CountEntity countEntity = new CountEntity();
			countEntity.init(obj);
			return setBeanValue(bean, 
					getDao().findByPage(selectEntity, bean.getPage(), bean.getPageSize(), "id"), 
					getDao().findCount(countEntity));
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询" + obj.getClass().getAnnotation(Table.class).name() + "错误", e);
			bean.setException(e);
			return bean;
		}
	}
	
	public ResultBean insert(T obj) {
		Logger.SERVICE.ldebug("插入[" + obj.getClass().getAnnotation(Table.class).name() + "]数据", obj.toString());
		try {
			InsertEntity entity = new InsertEntity();
			entity.init(obj,false);
			getDao().insert(entity);
			return ResultBean.RESULT_SUCCESS();
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("插入记录错误", e);
			return ResultBean.RESULT_ERR(e.getMessage());
		}
	}
	
	public ResultBean delete(T obj) {
		Logger.SERVICE.ldebug("删除[" + obj.getClass().getAnnotation(Table.class).name() + "]数据", obj.toString());
		try {
			DeleteEntity entity = new DeleteEntity();
			entity.init(obj);
			getDao().delete(entity);
			return ResultBean.RESULT_SUCCESS();
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("删除记录错误", e);
			return ResultBean.RESULT_ERR(e.getMessage());
		}
	}

	public ResultBean update(T obj) {
		Logger.SERVICE.ldebug("更新[" + obj.getClass().getAnnotation(Table.class).name() + "]数据", obj.toString());
		try {
			UpdateEntity entity = new UpdateEntity();
			entity.init(obj,false);
			getDao().update(entity);
			return ResultBean.RESULT_SUCCESS();
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("更新记录错误", e);
			return ResultBean.RESULT_ERR(e.getMessage());
		}
	}
}
