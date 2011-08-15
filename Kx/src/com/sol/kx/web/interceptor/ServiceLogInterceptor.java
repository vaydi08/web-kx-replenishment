package com.sol.kx.web.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.sol.util.log.Logger;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class ServiceLogInterceptor {
	@Pointcut("execution (public * com.megajoy.thirdpart.report.service.*.* (..))")
	public void inService() {
		
	}
	
	@Around("inService()")
	public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
		long start = System.currentTimeMillis();
		
		Object result = pjp.proceed();
		
		long end = System.currentTimeMillis();
		
		Logger.SYS.debug("执行 [" + pjp.getTarget().getClass().getSimpleName() + "." + pjp.getSignature().getName() + "] 耗时:" + (end - start));
		
		return result;
	}
}
