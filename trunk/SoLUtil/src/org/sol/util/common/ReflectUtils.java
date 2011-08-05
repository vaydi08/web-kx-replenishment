package org.sol.util.common;

import java.lang.reflect.ParameterizedType;

/**
 * 反射工具
 * @author HUYAO
 *
 */
public class ReflectUtils {
	/**
	 * 获取泛型类型
	 * @param clazz 实际类
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClassGenricType(final Class clazz) {
		return (Class<T>)((ParameterizedType)clazz.getGenericSuperclass()).getActualTypeArguments()[0];
	}
}
