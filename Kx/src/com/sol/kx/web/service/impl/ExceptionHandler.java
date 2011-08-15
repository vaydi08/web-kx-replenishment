package com.sol.kx.web.service.impl;

import org.springframework.stereotype.Service;

@Service
public class ExceptionHandler {

	protected void onDatabaseException(String msg,Throwable e) {
		e.printStackTrace();
	}
}
