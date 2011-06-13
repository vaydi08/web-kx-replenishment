package com.sol.kx.replenishment.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;

@Component
@Scope("session")
public class MainAction extends ActionSupport{

	private static final long serialVersionUID = 1L;

	public String execute() {
		return SUCCESS;
	}
}
