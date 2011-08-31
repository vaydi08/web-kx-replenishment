package com.sol.kx.web.service;

import com.sol.kx.web.service.bean.PagerBean;
import com.sol.kx.web.service.bean.ResultBean;

public interface BaseService<T> {
	public PagerBean<T> find(PagerBean<T> bean,T obj);
	
	public ResultBean add(T po);
	
	public ResultBean update(T po);
	
	public ResultBean delete(T po);
}
