package com.sol.kx.web.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.sol.kx.web.common.Logger;
import com.sol.kx.web.service.bean.ResultBean;
import com.sol.kx.web.service.util.PoiUtil;

@Controller
@Scope("session")
public class UploadAction extends ActionSupport{

	private static final long serialVersionUID = 1L;

	private File[] Filedata;
	
	// 提交请求结果
	protected ResultBean result;
	
	public String execute() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setCharacterEncoding("utf-8");

		if(Filedata == null) {
			result = ResultBean.RESULT_ERR("上传的文件不是Excel文件");
			return "result";
		}
			
		for(File file : Filedata)
			process(file);
		
		result = ResultBean.RESULT_SUCCESS();

		return "result";
	}
	
	private void process(File file) {
		PoiUtil poi = null;
		try {
			poi = new PoiUtil(file);
			while(poi.hasRow()) {
				while(poi.hasCell()) {
					System.out.println(poi.getValue());
				}
			}
		} catch (IOException e) {
			result = ResultBean.RESULT_ERR(e.getMessage());
			
			Logger.SYS.error("上传文件失败",e);
		} finally {
			if(poi != null) 
				poi.close();
		}
	}

	public ResultBean getResult() {
		return result;
	}

	public void setFiledata(File[] filedata) {
		Filedata = filedata;
	}

}
