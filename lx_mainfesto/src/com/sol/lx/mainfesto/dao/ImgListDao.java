package com.sol.lx.mainfesto.dao;

import java.sql.SQLException;
import java.util.List;

import com.sol.lx.mainfesto.dao.pojo.ImgList;

public interface ImgListDao extends BaseDao{
	public List<ImgList> find() throws SQLException;
	
	public int insert(String image,String text) throws SQLException;
}
