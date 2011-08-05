package com.megajoy.thirdpart.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.megajoy.thirdpart.report.service.bean.ConfigBeanFactory;

public abstract class BaseServiceImpl {
	@Autowired
	protected ExceptionHandler exceptionHandler;
	
	@Autowired
	protected ConfigBeanFactory configBeanFactory;
	
}
