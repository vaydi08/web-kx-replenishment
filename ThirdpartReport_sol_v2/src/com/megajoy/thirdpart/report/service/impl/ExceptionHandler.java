package com.megajoy.thirdpart.report.service.impl;

import org.springframework.stereotype.Service;

import com.megajoy.thirdpart.report.common.Constants;
import com.megajoy.thirdpart.report.common.Logger;

@Service
public class ExceptionHandler {
	public void onInit(Throwable e) {
		Logger.SYS.error("初始化系统时产生的错误",e);
	}
	
	public void onViewSync(int which_service,Throwable e) {
		Logger.SYS.lerror(which_service,"扫描 View_thirdpart 到 Thirdpart_report 时 产生错误",e);
	}
	
	public void onQuery(int sendstate,Throwable e) {
		Logger.SYS.error(
				(sendstate == Constants.SENDSTATE.READY.code ? "待发数据 " : "重发数据")
				+ "扫描 Thirdpart_report 时产生错误", e);
	}
	
	public void onHttpPost(int which_service,Throwable e) {
		Logger.SYS.lerror(which_service,"发送HTTP数据错误",e);
	}
	
	public void onUpdateSendstate(int which_service,Throwable e) {
		Logger.DB.lerror(which_service, "更新Sendstate状态失败",e);
	}
	
	public void onResumeUnsendData(Throwable e) {
		Logger.SYS.error("更新未发送数据 还原时产生错误",e);
	}
}
