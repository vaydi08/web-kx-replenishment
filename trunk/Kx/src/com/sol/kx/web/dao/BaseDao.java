package com.sol.kx.web.dao;

import java.sql.SQLException;
import java.util.List;

import org.sol.util.c3p0.dataEntity2.CountEntity;
import org.sol.util.c3p0.dataEntity2.DeleteEntity;
import org.sol.util.c3p0.dataEntity2.InsertEntity;
import org.sol.util.c3p0.dataEntity2.SelectEntity;
import org.sol.util.c3p0.dataEntity2.UpdateEntity;

public interface BaseDao {
	public <X> List<X> find(Class<X> clazz,Object obj,int page,int pageSize,String order) throws Exception;
	public int findCount(Object obj) throws Exception;
	public void add(Object obj) throws Exception;
	public int addAndReturnKey(Object obj) throws SQLException;
	public void update(Object obj) throws Exception;
	public void delete(Object obj) throws Exception;
	
	
	public <X> List<X> find2(SelectEntity entity) throws Exception;
	public int findCount2(CountEntity entity) throws Exception;
	public void add2(InsertEntity entity) throws SQLException;
	public void update2(UpdateEntity entity) throws SQLException;
	public void delete2(DeleteEntity entity) throws SQLException;
}
