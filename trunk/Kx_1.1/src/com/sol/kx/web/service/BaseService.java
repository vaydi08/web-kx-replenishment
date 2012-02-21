package com.sol.kx.web.service;

import org.sol.util.mybatis.BaseMapper;
import org.sol.util.mybatis.MyBatisPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sol.kx.web.common.Constants;
import com.sol.kx.web.common.Logger;
import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.bean.ResultBean;

public abstract class BaseService {
	@Autowired
	protected ExceptionHandler exceptionHandler;

	@Transactional(readOnly = true)
	public <T extends MyBatisPojo> void select(PagerBean<T> bean,T obj) {
		if (Constants.DEBUG)
			Logger.SERVICE.debug("查询数据 "+ obj.tablename());
		
		try {
			bean.setDataList(injectMapper().select(obj));
		} catch (Exception e) {
			bean.setException(e);
			exceptionHandler.onDatabaseException("查询错误",obj.tablename(), e);
		}
	}
	
	@Transactional(readOnly = true)
	public <T extends MyBatisPojo> void selectByPage(PagerBean<T> bean,T obj) {
		if (Constants.DEBUG)
			Logger.SERVICE.ldebug("查询分页数据", obj.tablename(), bean.getPage(),
					bean.getPageSize(), obj.toString());
		try {
			obj.setPage(bean.getPage());
			obj.setPageSize(bean.getPageSize());
			bean.setDataList(injectMapper().selectByPage(obj));
			bean.setCount(injectMapper().count(obj));
		} catch (Exception e) {
			bean.setException(e);
			exceptionHandler.onDatabaseException("查询错误",obj.tablename(), e);
		}
	}
	
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,timeout = Constants.DB_TIMEOUT)
	public <T extends MyBatisPojo> ResultBean insert(T obj) {
		if (Constants.DEBUG)
			Logger.SERVICE.debug("插入数据 " + obj.toString());
		
		try {
			injectMapper().insert(obj);
			return ResultBean.RESULT_SUCCESS(obj);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("查询错误",obj.tablename(), e);
			return ResultBean.RESULT_ERR(e.getMessage(), obj);
		}
	}
	
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,timeout = Constants.DB_TIMEOUT)
	public <T extends MyBatisPojo> ResultBean update(T obj) {
		if (Constants.DEBUG)
			Logger.SERVICE.debug("更新数据 " + obj.toString());
		
		try {
			injectMapper().update(obj);
			return ResultBean.RESULT_SUCCESS(obj);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("更新错误",obj.tablename(), e);
			return ResultBean.RESULT_ERR(e.getMessage(), obj);
		}
	}
	
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,timeout = Constants.DB_TIMEOUT)
	public <T extends MyBatisPojo> ResultBean delete(T obj) {
		if (Constants.DEBUG)
			Logger.SERVICE.debug("删除数据 " + obj.toString());
		
		try {
			injectMapper().delete(obj);
			return ResultBean.RESULT_SUCCESS(obj);
		} catch (Exception e) {
			exceptionHandler.onDatabaseException("删除错误",obj.tablename(), e);
			return ResultBean.RESULT_ERR(e.getMessage(), obj);
		}
	}

	protected BaseMapper baseMapper;

	protected abstract BaseMapper injectMapper();

}
