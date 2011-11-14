package com.sol.kx.web.action;

import java.io.InputStream;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import com.sol.kx.web.service.ImageService;

@Controller
@Scope("session")
@Results({
	@Result(name = Action.SUCCESS,type = "stream",
			params = {"contentType","image/jpeg","inputName","inputStream",
			"bufferSize","4096","contentDisposition","inline;filename=img.jpg"})
})
public class PicAction extends ActionSupport{

	private static final long serialVersionUID = 1L;

	@Autowired
	private ImageService imageService;
	
	private String img;
	
	@Override
	public String execute() {
		return SUCCESS;
	}
	
	public InputStream getInputStream() {
		return imageService.load(img);
	}

	public void setImg(String img) {
		this.img = img;
	}
}
