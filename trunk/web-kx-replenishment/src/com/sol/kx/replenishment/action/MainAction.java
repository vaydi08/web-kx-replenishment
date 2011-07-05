package com.sol.kx.replenishment.action;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

@Controller
@Scope("session")
@Results({@Result(name = "top",location = "top.jsp"),
		  @Result(name = "menu",location = "menu.jsp")})
public class MainAction extends ActionSupport{

	private static final long serialVersionUID = 1L;

	@Override
	public String execute() {
		return SUCCESS;
	}
	
	public String top() {
		return "top";
	}
	public String menu() {
		return "menu";
	}
}
