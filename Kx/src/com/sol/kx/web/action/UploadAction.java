package com.sol.kx.web.action;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.sol.kx.web.service.InfoProductService;
import com.sol.kx.web.service.bean.ImportResultBean;
@Controller
@Scope("session")
@Results({@Result(name = "importResult", location = "/importResult.jsp")})
public class UploadAction extends ActionSupport{

	private static final long serialVersionUID = 1L;

	private File[] Filedata;
	
	private boolean success;
	private String errorMsg;
	// 提交请求结果
	protected ImportResultBean[] result;
	
	@Autowired
	private InfoProductService infoProductService;
	
	public String execute() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");

		if(Filedata == null) {
			success = false;
			errorMsg = "上传的文件不是Excel文件";
			return "importResult";
		}
		
		success = true;
		
		result = infoProductService.importExcel(Filedata,3);
		
		return "importResult";
	}

	public ImportResultBean[] getResult() {
		return result;
	}

	public void setFiledata(File[] filedata) {
		Filedata = filedata;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

}
