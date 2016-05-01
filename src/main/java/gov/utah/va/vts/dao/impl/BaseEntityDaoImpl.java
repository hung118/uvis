package gov.utah.va.vts.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.utah.va.vts.dao.BaseEntityDao;
import gov.utah.va.vts.model.BaseEntityAbstract;

/**
 * Implemtation class for BaseEntityDao interface.
 * 
 * @author HNGUYEN
 *
 * @param <T>
 */
public class BaseEntityDaoImpl<T extends BaseEntityAbstract> implements BaseEntityDao<T>  {
	
	/**
	 * base logging capability for all extending services
	 */
	protected Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * the class of the entity
	 */
	private Class<T> type;
	
	/**
	 * the injected entity manager (used JPA sources).
	 */
	@PersistenceContext(unitName = "vts")
	protected EntityManager entityManager;
	
	/**
	 * public constructor that sets up the class type.
	 * @param type
	 */
	public BaseEntityDaoImpl(Class<T> type){
		this.type = type;
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.dts.det.dao.BaseEntityDao#delete(gov.utah.dts.det.model.BaseEntity)
	 */
	public void delete(T entity) {
		
		T me = entityManager.merge(entity);
		// remove the object from the store
		entityManager.remove(me);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.dts.det.dao.BaseEntityDao#findById(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public T findById(Long id) {
		Query q = entityManager.createQuery("from " + type.getName()
				+ " where ID = :id");
		
		q.setParameter("id", id);

		T entity = null;

		try {
			entity = (T) q.getSingleResult();
		} catch (NoResultException nre) {
			// return null when not found
			logger.info("Unable to find the requested entity: " + type + "["
					+ id + "]");
		}

		return entity;
	}
	

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.BaseEntityDao#findByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public T findByName(String name) {
		Query q = entityManager.createQuery("from " + type.getName()
				+ " where name = :name");
		
		q.setParameter("name", name);

		T entity = null;

		try {
			entity = (T) q.getSingleResult();
		} catch (NoResultException nre) {
			// return null when not found
			logger.info("Unable to find the requested entity: " + type + "["
					+ name + "]");
		}

		return entity;	
	}


	/*
	 * (non-Javadoc)
	 * @see gov.utah.dts.det.dao.BaseEntityDao#listAll(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<T> listAll(String orderBy) {
		String hql = "from " + type.getName() + " order by " + orderBy;
		
		Query q = entityManager.createQuery(hql);

		return q.getResultList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.BaseEntityDao#listAllActive(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<T> listAllActive(String orderBy) {
		String hql = "from " + type.getName() + " where active = 1 order by " + orderBy;
		
		Query q = entityManager.createQuery(hql);

		return q.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.dts.det.dao.BaseEntityDao#save(gov.utah.dts.det.model.BaseEntity)
	 */
	public T save(T entity) {
		// get past null pointer exceptions
		if (entity == null)
			return entity;

		// if it already has an id... then merge (update).
		if (entity.getId() != null) {	//merge
			entity = entityManager.merge(entity);
		} else { // insert
			entityManager.persist(entity);
		}

		return entity;
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.dts.det.dao.BaseEntityDao#flush()
	 */
	public void flush() {
		entityManager.flush();		
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.BaseEntityDao#rollback()
	 */
	public void rollback() {
		entityManager.getTransaction().rollback();
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.BaseEntityDao#clear()
	 */
	public void clear() {
		entityManager.clear();
	}
}
