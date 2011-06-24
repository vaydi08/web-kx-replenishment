package com.sol.kx.replenishment.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.type.Type;

import com.sol.kx.replenishment.service.bean.PageResult;

/**
 * DAO操作接口
 * @author HUYAO
 *
 * @param <T>
 */
public interface BaseDao<T> {

	/**
	 * 插入记录
	 * @param ds
	 * @param dsType
	 * @param pojo
	 */
	public void savePojo(Object pojo);
	
	/**
	 * 更新记录
	 * @param ds
	 * @param dsType
	 * @param pojo
	 */
	public void updatePojo(Object pojo);
	
	/**
	 * 删除记录
	 * @param ds
	 * @param dsType
	 * @param pojo
	 */
	public void deletePojo(Object pojo);
	
	/**
	 * 根据id删除
	 * @param id
	 */
	public void deletePojoById(Serializable id);
	
	/**
	 * 执行原生SQL
	 * @param sql
	 * @return
	 */
	public int executeSql(final String sql,final Object... objs);
	
	/**
	 * 执行原生SQL查询
	 * @param <X> 
	 * @param clazz 反射类
	 * @param sql 
	 * @param map 参数表
	 * @return
	 */
	public <X> List<X> findSql(final Class<X> clazz,final String sql,final Map<String,Type> scalarMap,final Map<String,Object> map);
		
	/**
	 * 创建条件查询
	 * @param entityClass 反射类
	 * @param criterions 条件
	 * @return
	 */
	public DetachedCriteria createCriterion(Class<?> entityClass,Criterion... criterions);
	
	/**
	 * 创建条件查询
	 * @param criterions 条件
	 * @return
	 */
	public DetachedCriteria createCriterion(Criterion... criterions);

	/**
	 * 分页方法
	 * @param page 页数
	 * @param pagesize 每页大小
	 * @param map 查询参数
	 * @return
	 */
	public PageResult<T> listPage(PageResult<T> result,Criterion... criterions);
	
	/**
	 * 分页方法
	 * @param result 页结果bean
	 * @param detachedCriteria 查询封装
	 * @param page 页数
	 * @param pagesize 每页大小
	 * @return
	 */
	public PageResult<T> listPage(final PageResult<T> result,final DetachedCriteria detachedCriteria);
	
	/**
	 * 获取一个实体
	 * @param <X> 
	 * @param clazz 反射类
	 * @param id 主键
	 * @return
	 */
	public <X> X getO(final Class<X> clazz,final Serializable id);
	
	/**
	 * 获取一个实体
	 * @param id 主键
	 * @return
	 */
	public T getPojo(final Serializable id);
}
