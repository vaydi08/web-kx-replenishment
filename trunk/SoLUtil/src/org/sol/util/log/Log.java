package org.sol.util.log;

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
	
	private String getString(String msg,Object... params) {
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append(" - ");
		for(Object o : params) {
			sb.append(o);
			sb.append('|');
		}
		
		return sb.toString();
	}
	public void ldebug(String msg,Object... params) {
		debug(getString(msg, params));
	}
	
	public void linfo(String msg,Object... params) {
		info(getString(msg, params));
	}
	
	public void lerror(String msg,Object... params) {
		error(getString(msg, params));
	}
	
	public void lerror(String msg,Throwable t,Object... params) {
		error(getString(msg, params),t);
	}
	
	
	public void debug(Object obj) {
		if(log.isDebugEnabled())
			log.debug(obj);
	}

	public void debug(Object obj, Throwable throwable) {
		if(log.isDebugEnabled())
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
