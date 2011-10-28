package com.sol.lx.mainfesto.dao;

import java.sql.SQLException;
import java.util.List;

import org.sol.util.c3p0.dataEntity2.CountEntity;
import org.sol.util.c3p0.dataEntity2.DeleteEntity;
import org.sol.util.c3p0.dataEntity2.InsertEntity;
import org.sol.util.c3p0.dataEntity2.SelectEntity;
import org.sol.util.c3p0.dataEntity2.UpdateEntity;

public interface BaseDao {
	public List find(SelectEntity entity) throws Exception;
	public List findByPage(SelectEntity entity,int page,int pageSize,String order) throws Exception;
	public int findCount(CountEntity entity) throws Exception;
	public void insert(InsertEntity entity) throws SQLException;
	public void update(UpdateEntity entity) throws SQLException;
	public void delete(DeleteEntity entity) throws SQLException;
}
