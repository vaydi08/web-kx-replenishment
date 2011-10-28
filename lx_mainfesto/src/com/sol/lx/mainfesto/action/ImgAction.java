package com.sol.lx.mainfesto.action;

import java.io.File;
import java.util.List;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.sol.lx.mainfesto.dao.pojo.ImgList;
import com.sol.lx.mainfesto.service.ImgListService;

@Controller
@Scope("session")
@Results({
	@Result(name = "imgList",location = "/imgList.jsp"),
	@Result(name = "upload",location = "/upload.jsp")
})
public class ImgAction extends ActionSupport{
	
	private static final long serialVersionUID = 1L;

	@Autowired
	private ImgListService imgListService;
	
	private List<ImgList> imgList;
	
	public String list() {
		imgList = imgListService.find();
		
		return "imgList";
	}
	
	private File[] Filedata;
	private boolean success;
	
	public String upload() {
		System.out.println(Filedata[0]);
		success = true;
		return "upload";
	}

	public List<ImgList> getImgList() {
		return imgList;
	}

	public void setFiledata(File[] filedata) {
		Filedata = filedata;
	}

	public boolean isSuccess() {
		return success;
	}
}
