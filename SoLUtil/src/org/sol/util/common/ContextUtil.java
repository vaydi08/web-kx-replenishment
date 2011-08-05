/**
 *  通用工具
 * @author SoL
 * @since JDK1.6  
 * @version 1.0.0  
 */
package org.sol.util.common;


/**
 * IoC框架 对象获取工具
 * @author HUYAO
 *
 */
public class ContextUtil {
	
	public static Object getObject(String name) {
		return SpringContextUtil.getBean(name);
	}
	
	public static Object getObject(String name,Object... objs) {
		return SpringContextUtil.getBean(name, objs);
	}
	
	public static <T> T getObject(Class<T> clazz) {
		return SpringContextUtil.getBean(clazz);
	}

}
 