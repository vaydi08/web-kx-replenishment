package com.sol.kx.web.dao;

import java.util.List;

public interface BaseDao {
	public <X> List<X> find(Class<X> clazz,Object obj,int page,int pageSize) throws Exception;
	public int findCount(Object obj) throws Exception;
	public void add(Object obj) throws Exception;
	public void update(Object obj) throws Exception;
	public void delete(Object obj) throws Exception;
}
