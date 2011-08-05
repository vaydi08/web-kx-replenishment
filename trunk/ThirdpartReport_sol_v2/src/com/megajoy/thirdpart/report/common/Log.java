package com.megajoy.thirdpart.report.common;

import org.apache.commons.logging.LogFactory;

/**
 * 扩展的Logger
 * @author HUYAO
 *
 */
public class Log {
	protected org.apache.commons.logging.Log log;
	
	protected Log(Class<?> clazz) {
		log = LogFactory.getLog(clazz);
	}
	
	protected Log(String logger) {
		log = LogFactory.getLog(logger);
	}
	
	public static synchronized Log getLog(Class<?> clazz) {
		return new Log(clazz);
	}
	
	public static synchronized Log getLog(String logger) {
		return new Log(logger);
	}
	
	public void init(String msg,Object... params) {
		log.debug("-init- " + String.format(msg, params));
	}
	
	private String getString(int which_service,String msg,Object... params) {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(which_service).append("]");
		sb.append(msg);
		sb.append(" - ");
		for(Object o : params) {
			sb.append(o);
			sb.append('|');
		}
		
		return sb.toString();
	}
	public void ldebug(int which_service,String msg,Object... params) {
		log.debug(getString(which_service,msg, params));
	}
	
	public void linfo(int which_service,String msg,Object... params) {
		log.info(getString(which_service,msg, params));
	}
	
	public void lerror(int which_service,String msg,Object... params) {
		log.error(getString(which_service,msg, params));
	}
	
	public void lerror(int which_service,String msg,Throwable t,Object... params) {
		log.error(getString(which_service,msg, params),t);
	}
	
	
	public void debug(Object obj) {
		log.debug(obj);
	}

	public void debug(Object obj, Throwable throwable) {
		log.debug(obj,throwable);
	}

	public void info(Object obj) {
		log.info(obj);
	}

	public void info(Object obj, Throwable throwable) {
		log.info(obj, throwable);
	}

	public void warn(Object obj) {
		log.warn(obj);
	}

	public void warn(Object obj, Throwable throwable) {
		log.warn(obj, throwable);
	}

	public void error(Object obj) {
		log.error(obj);
	}

	public void error(Object obj, Throwable throwable) {
		log.error(obj, throwable);
	}
}
