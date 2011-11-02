package com.sol.lx.mainfesto.service;

import java.io.File;
import java.util.List;

import com.sol.lx.mainfesto.dao.pojo.ImgList;

public interface ImgListService extends BaseService<ImgList>{
	public List<ImgList> find();
	public boolean insert(String image,String text);
		
	public boolean pressText(String[] pressText, File srcImgFile,int color);
}
