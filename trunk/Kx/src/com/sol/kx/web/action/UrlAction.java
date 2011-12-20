package com.sol.kx.web.action;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sol.kx.web.common.Constants;
import com.sol.kx.web.dao.pojo.SysUser;
import com.sol.kx.web.service.SysAuthService;

@Controller
@Scope("session")
@Results({@Result(name="test",location="/beanTest.jsp")})
public class UrlAction extends ActionSupport{

	private static final long serialVersionUID = 1L;
	private static final String HREF = "href";
	
	private String ext;
	
	private String url;
	
	private String type;
	
	@Autowired
	private SysAuthService sysAuthService;
	
	public UrlAction() {
		ext = "jsp";
		type = "redirect";
	}
	
	@Override
	public String execute() {
		SysUser user = (SysUser) ActionContext.getContext().getSession().get(Constants.SESSION_USER);
		// 检查登录状态
		if(user == null) {
			url = "/gologin";
			ext = "html";
			return HREF;
		}
		
		// 检查权限系统
		if(!ext.equals("js")) {
			if(!sysAuthService.checkAuth(url, user)) {
				url = "/403";
				ext = "html";			
			}
		}
		
		return HREF;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
