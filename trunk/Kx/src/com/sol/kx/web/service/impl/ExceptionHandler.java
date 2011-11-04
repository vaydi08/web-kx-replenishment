package com.sol.kx.web.service.impl;

import org.springframework.stereotype.Service;

import com.sol.kx.web.common.Logger;

@Service
public class ExceptionHandler {

	public void onDatabaseException(String msg,Throwable e) {
		Logger.DB.error(msg,e);
	}
	
	public void onExcelException(String msg,Throwable e) {
		Logger.SERVICE.error(msg,e);
	}
	
	public void onSaveUpload(String filename,String picData,Throwable e) {
		Logger.SERVICE.error("保存上传文件错误 Filename:" + filename + " Data:" + picData,e);
	}
}
