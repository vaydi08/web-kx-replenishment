package com.sol.kx.replenishment.action;

import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("session")
@Results({@Result(name = "success",location = "1.jsp")})
@ParentPackage("struts-default")
public class MainAction extends ActionSupport{

	private static final long serialVersionUID = 1L;

	public String execute() {
		return SUCCESS;
	}
}
