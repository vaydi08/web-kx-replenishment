package com.sol.kx.web.service.impl;

import java.util.List;

import javax.persistence.Table;

import org.sol.util.c3p0.dataEntity2.CountEntity;
import org.sol.util.c3p0.dataEntity2.DeleteEntity;
import org.sol.util.c3p0.dataEntity2.InsertEntity;
import org.sol.util.c3p0.dataEntity2.SelectEntity;
import org.sol.util.c3p0.dataEntity2.UpdateEntity;
import org.springframework.beans.factory.annotation.Autowired;

import com.sol.kx.web.common.Logger;
import com.sol.kx.web.dao.BaseDao;
import com.sol.kx.web.service.BaseService;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.bean.ResultBean;

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
	@SuppressWarnings("unchecked")
	public PagerBean<T> find(PagerBean<T> bean,T obj) {
		return find(bean, obj, getDao(), (Class<T>)obj.getClass());
	}
	
	protected PagerBean<T> find(PagerBean<T> bean,Object obj,BaseDao dao,Class<T> clazz) {
		Logger.SERVICE.ldebug("查询[" + obj.getClass().getAnnotation(Table.class).name() + "]数据",bean.getPage(),bean.getPageSize(),
				obj.toString());
		try {
			return setBeanValue(bean, 
					dao.find(clazz,obj,bean.getPage(), bean.getPageSize(),"id"), 
					dao.findCount(obj));
		} catch (Exception e) {
			bean.setException(e);
			exceptionHandler.onDatabaseException("查询[" + obj.getClass().getAnnotation(Table.class).name() + "]时的错误", e);
			return bean;
		}
	}
	
	public ResultBean add(T po) {
		Logger.SERVICE.ldebug("插入[" + po.getClass().getAnnotation(Table.class).name() + "]数据", po.toString());
		try {
			getDao().add(po);
			return ResultBean.RESULT_SUCCESS();
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("插入记录错误", e);
			return ResultBean.RESULT_ERR(e.getMessage());
		}
	}
	
	public int addAndReturnKey(T po) {
		Logger.SERVICE.ldebug("导入[" + po.getClass().getAnnotation(Table.class).name() + "]数据", po.toString());
		try {
			return getDao().addAndReturnKey(po);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("导入记录错误", e);
			return -1;
		}
	}

	public ResultBean delete(T po) {
		Logger.SERVICE.ldebug("删除[" + po.getClass().getAnnotation(Table.class).name() + "]数据", po.toString());
		try {
			getDao().delete(po);
			return ResultBean.RESULT_SUCCESS();
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("删除记录错误", e);
			return ResultBean.RESULT_ERR(e.getMessage());
		}
	}

	public ResultBean update(T po) {
		Logger.SERVICE.ldebug("更新[" + po.getClass().getAnnotation(Table.class).name() + "]数据", po.toString());
		try {
			getDao().update(po);
			return ResultBean.RESULT_SUCCESS();
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("更新记录错误", e);
			return ResultBean.RESULT_ERR(e.getMessage());
		}
	}
	
	public PagerBean<T> find2(T obj) {
		Logger.SERVICE.ldebug("查询[" + obj.getClass().getAnnotation(Table.class).name() + "]数据",obj.toString());
		PagerBean<T> bean = new PagerBean<T>();
		try {
			SelectEntity selectEntity = new SelectEntity();
			selectEntity.init(obj);
			bean.setDataList((List<T>) getDao().find2(selectEntity));
			return bean;
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询" + obj.getClass().getAnnotation(Table.class).name() + "错误", e);
			bean.setException(e);
			return bean;
		}
	}
	
	public PagerBean<T> findByPage2(PagerBean<T> bean,T obj) {
		Logger.SERVICE.ldebug("查询[" + obj.getClass().getAnnotation(Table.class).name() + "]数据",obj.toString());
		try {
			SelectEntity selectEntity = new SelectEntity();
			selectEntity.init(obj);
			CountEntity countEntity = new CountEntity();
			countEntity.init(obj);
			return setBeanValue(bean, 
					getDao().findByPage2(selectEntity, bean.getPage(), bean.getPageSize(), "id"), 
					getDao().findCount2(countEntity));
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询" + obj.getClass().getAnnotation(Table.class).name() + "错误", e);
			bean.setException(e);
			return bean;
		}
	}
	
	public ResultBean add2(T obj) {
		Logger.SERVICE.ldebug("插入[" + obj.getClass().getAnnotation(Table.class).name() + "]数据", obj.toString());
		try {
			InsertEntity entity = new InsertEntity();
			entity.init(obj,false);
			getDao().add2(entity);
			return ResultBean.RESULT_SUCCESS();
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("插入记录错误", e);
			return ResultBean.RESULT_ERR(e.getMessage());
		}
	}
	
	public ResultBean delete2(T obj) {
		Logger.SERVICE.ldebug("删除[" + obj.getClass().getAnnotation(Table.class).name() + "]数据", obj.toString());
		try {
			DeleteEntity entity = new DeleteEntity();
			entity.init(obj);
			getDao().delete2(entity);
			return ResultBean.RESULT_SUCCESS();
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("删除记录错误", e);
			return ResultBean.RESULT_ERR(e.getMessage());
		}
	}

	public ResultBean update2(T obj) {
		Logger.SERVICE.ldebug("更新[" + obj.getClass().getAnnotation(Table.class).name() + "]数据", obj.toString());
		try {
			UpdateEntity entity = new UpdateEntity();
			entity.init(obj,false);
			getDao().update2(entity);
			return ResultBean.RESULT_SUCCESS();
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("更新记录错误", e);
			return ResultBean.RESULT_ERR(e.getMessage());
		}
	}
}
