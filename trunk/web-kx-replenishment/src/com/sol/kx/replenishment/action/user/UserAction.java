package com.sol.kx.replenishment.action.user;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;

@Component
@Scope("prototype")
public class UserAction extends ActionSupport{

	private static final long serialVersionUID = 1L;

	@Action(value="test",results=
		{@Result(name = SUCCESS, location = "../1,jsp")})
	public String test() {
		return SUCCESS;
	}
}
