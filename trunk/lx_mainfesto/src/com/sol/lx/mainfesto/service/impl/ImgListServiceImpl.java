package com.sol.lx.mainfesto.service.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sol.lx.mainfesto.dao.BaseDao;
import com.sol.lx.mainfesto.dao.ImgListDao;
import com.sol.lx.mainfesto.dao.pojo.ImgList;
import com.sol.lx.mainfesto.service.ImgListService;

@Service
public class ImgListServiceImpl extends BaseServiceImpl<ImgList> implements ImgListService {

	@Autowired
	private ImgListDao imgListDao;
	
	public List<ImgList> find() {
		try {
			return imgListDao.find();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected BaseDao getDao() {
		return imgListDao;
	}

}
