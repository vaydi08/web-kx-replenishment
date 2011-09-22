package com.sol.kx.web.service;

import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.bean.ResultBean;

public interface BaseService<T> {
	public PagerBean<T> find(PagerBean<T> bean,T obj);
	
	public ResultBean add(T po);
	public int addAndReturnKey(T po);
	
	public ResultBean update(T po);
	
	public ResultBean delete(T po);
	
	public PagerBean<T> find2(T obj);
	public PagerBean<T> findByPage2(PagerBean<T> bean,T obj);
	public ResultBean add2(T obj);
	public ResultBean delete2(T obj);
	public ResultBean update2(T obj);
}
