package com.sol.lx.mainfesto.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.sol.lx.mainfesto.dao.ImgListDao;
import com.sol.lx.mainfesto.dao.pojo.ImgList;

@Repository
public class ImgListDaoImpl extends BaseDaoImpl implements ImgListDao{
	@Value("${sql.img_list.find}")
	private String SQL_FIND;
	
	public List<ImgList> find() throws SQLException {
		return dataConsole.find(SQL_FIND, ImgList.class,dataConsole.parseSmap(ImgList.class, 
				"id","image","text"));
	}
	
	@Value("${sql.img_list.insert}")
	private String SQL_INSERT;
	
	public int insert(String image,String text) throws SQLException {
		return dataConsole.updatePrepareSQL(SQL_INSERT, image,text);
	}
}
