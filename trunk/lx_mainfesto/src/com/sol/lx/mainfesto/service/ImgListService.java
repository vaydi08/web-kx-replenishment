package com.sol.lx.mainfesto.service;

import java.util.List;

import com.sol.lx.mainfesto.dao.pojo.ImgList;

public interface ImgListService extends BaseService<ImgList>{
	public List<ImgList> find();
}
