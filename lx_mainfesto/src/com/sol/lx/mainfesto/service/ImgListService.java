package com.sol.lx.mainfesto.service;

import java.io.File;
import java.util.List;

import com.sol.lx.mainfesto.dao.pojo.ImgList;

public interface ImgListService extends BaseService<ImgList>{
	public List<ImgList> find();
	public boolean insert(String image,String text);
		
	public void compress(File tempImg,String savePath);
	public boolean pressText(String text1,String text2, File srcImgFile,int color,int x,int y);
}
