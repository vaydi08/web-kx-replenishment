package com.sol.kx.replenishment.dao.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.Transformers;
import org.hibernate.type.Type;
import org.sol.util.common.ReflectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.sol.kx.replenishment.dao.BaseDao;
import com.sol.kx.replenishment.service.bean.PageResult;


public class HibernateDaoImpl<T> extends HibernateTemplate implements BaseDao<T> {

	/* (non-Javadoc)
	 * @see com.megajoy.web.opmanager.dao.BaseDao#save(com.megajoy.web.opmanager.dao.DataSourceHolder.DS, com.megajoy.web.opmanager.dao.DataSourceHolder.DSTYPE, java.lang.Object)
	 */
	public void savePojo(Object pojo) {
		super.save(pojo);
	}
	
	/* (non-Javadoc)
	 * @see com.megajoy.web.opmanager.dao.BaseDao#update(com.megajoy.web.opmanager.dao.DataSourceHolder.DS, com.megajoy.web.opmanager.dao.DataSourceHolder.DSTYPE, java.lang.Object)
	 */
	public void updatePojo(Object pojo) {
		super.update(pojo);
	}
	
	/* (non-Javadoc)
	 * @see com.megajoy.web.opmanager.dao.BaseDao#delete(com.megajoy.web.opmanager.dao.DataSourceHolder.DS, com.megajoy.web.opmanager.dao.DataSourceHolder.DSTYPE, java.lang.Object)
	 */
	public void deletePojo(Object pojo) {
		super.delete(pojo);
	}
	
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	protected Class<T> entityClass;
	
	public HibernateDaoImpl() {
		entityClass = ReflectUtils.getClassGenricType(getClass());
	}
	
	public void deletePojoById(Serializable id) {
		String sql = "delete from " + entityClass.getName() + 
					 " where id = ?";
		super.bulkUpdate(sql, id);
	}
	
	protected String getIdentifier(Class<?> clazz) {
		ClassMetadata cmd = getSessionFactory().getClassMetadata(clazz);
		return cmd.getIdentifierPropertyName();
	}
	
	/* (non-Javadoc)
	 * @see com.megajoy.web.opmanager.dao.BaseDao#executeSql(java.lang.String)
	 */
	public int executeSql(final String sql,final Object... objs) {
		return execute(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery query = session.createSQLQuery(sql);
				for(int i = 0; i < objs.length; i ++)
					query.setParameter(i, objs[i]);
				return query.executeUpdate();
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	public <X> List<X> findSql(final Class<X> clazz,final String sql,final Map<String,Type> scalarMap,final Map<String,Object> map) {
		return executeFind(new HibernateCallback<List>() {
			public List doInHibernate(Session session) throws HibernateException,
					SQLException {
				SQLQuery query = session.createSQLQuery(sql);

				Set<Entry<String,Object>> set = map.entrySet();
				for(Entry<String, Object> en : set)  
					query.setParameter(en.getKey(), en.getValue());
			
				Set<Entry<String,Type>> scalarSet = scalarMap.entrySet();
				for(Entry<String,Type> en : scalarSet)
					query.addScalar(en.getKey(),en.getValue());
				
				query.setResultTransformer(Transformers.aliasToBean(clazz));
		
				return query.list();
			}
		});
	}

	/* (non-Javadoc)
	 * @see com.megajoy.web.opmanager.dao.BaseDao#findAll()
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		return findByCriteria(
				createCriterion(entityClass));
	}
	
	/* (non-Javadoc)
	 * @see com.megajoy.web.opmanager.dao.BaseDao#createCriterion(java.lang.Class, org.hibernate.criterion.Criterion[])
	 */
	public DetachedCriteria createCriterion(Class<?> entityClass,
			Criterion... criterions) {
		DetachedCriteria criteria = DetachedCriteria.forClass(entityClass);
		for(Criterion c : criterions)
			criteria.add(c);
		return criteria;
	}

	/* (non-Javadoc)
	 * @see com.megajoy.web.opmanager.dao.BaseDao#createCriterion(org.hibernate.criterion.Criterion[])
	 */
	public DetachedCriteria createCriterion(Criterion... criterions) {
		return createCriterion(entityClass, criterions);
	}
	
	/* (non-Javadoc)
	 * @see com.megajoy.web.opmanager.dao.BaseDao#listPage(com.megajoy.web.opmanager.dao.PageResult, int, int, org.hibernate.criterion.Criterion[])
	 */
	public PageResult<T> listPage(PageResult<T> result,Criterion... criterions) {
		DetachedCriteria dc = createCriterion(criterions);

		return listPage(result, dc);
	}
	
	/* (non-Javadoc)
	 * @see com.megajoy.web.opmanager.dao.BaseDao#listPage(com.megajoy.web.opmanager.dao.PageResult, org.hibernate.criterion.DetachedCriteria, int, int)
	 */
	public PageResult<T> listPage(final PageResult<T> result,
			final DetachedCriteria detachedCriteria) {
		
		execute(new HibernateCallback<T>() {
			@SuppressWarnings("unchecked")
			public T doInHibernate(Session session) throws HibernateException,
					SQLException {
				int page = result.getPage();
				int pagesize = result.getPageSize();
				Criteria criteria = detachedCriteria.getExecutableCriteria(session);
				int rowcount = (Integer) criteria.setProjection(Projections.rowCount()).uniqueResult();
				criteria.setProjection(null);
				List<T> list = criteria.setFirstResult((page - 1) * pagesize).setMaxResults(pagesize).list();
				
				result.setRowCount(rowcount);
				result.setResult(list);
				
				return null;
			}
		});
		
		return result;
	}

	/* (non-Javadoc)
	 * @see com.megajoy.web.opmanager.dao.BaseDao#getO(java.lang.Class, java.io.Serializable)
	 */
	public <X> X getO(Class<X> entityClass, Serializable id)
			throws DataAccessException {
		return super.get(entityClass, id);
	}
	
	/* (non-Javadoc)
	 * @see com.megajoy.web.opmanager.dao.BaseDao#get(java.io.Serializable)
	 */
	public T getPojo(Serializable id) {
		return super.get(entityClass,id);
	}
}
