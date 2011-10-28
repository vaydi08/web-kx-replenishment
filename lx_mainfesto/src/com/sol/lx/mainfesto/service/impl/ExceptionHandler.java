package com.sol.lx.mainfesto.service.impl;

import org.springframework.stereotype.Service;

import com.sol.lx.mainfesto.common.Logger;


@Service
public class ExceptionHandler {

	public void onDatabaseException(String msg,Throwable e) {
		Logger.DB.error(msg,e);
	}
	
	public void onExcelException(String msg,Throwable e) {
		Logger.SERVICE.error(msg,e);
	}
}
