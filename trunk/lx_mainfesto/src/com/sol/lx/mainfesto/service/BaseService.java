package com.sol.lx.mainfesto.service;

import com.sol.lx.mainfesto.service.bean.PagerBean;
import com.sol.lx.mainfesto.service.bean.ResultBean;

public interface BaseService<T> {
	public PagerBean<T> find(T obj);
	public PagerBean<T> findByPage(PagerBean<T> bean,T obj);
	public ResultBean insert(T obj);
	public ResultBean delete(T obj);
	public ResultBean update(T obj);
}
