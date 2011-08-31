package com.sol.kx.web.service.impl;

import org.springframework.stereotype.Service;

import com.sol.kx.web.common.Logger;

@Service
public class ExceptionHandler {

	public void onDatabaseException(String msg,Throwable e) {
		Logger.DB.error(msg,e);
	}
}
