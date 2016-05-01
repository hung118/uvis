package gov.utah.va.vts.dao.impl;

import javax.persistence.Query;

import gov.utah.va.vts.dao.DocTypeDao;
import gov.utah.va.vts.model.DocType;

import org.springframework.stereotype.Repository;

/**
 * Implementation class for DocTypeDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="docTypeDao")
public class DocTypeDaoImpl extends BaseEntityDaoImpl<DocType> implements DocTypeDao {

	public DocTypeDaoImpl() {
		super(DocType.class);
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.DocTypeDao#findNameById(java.lang.Long)
	 */
	@Override
	public String findDocNameById(Long id) {
		String sql = "select name from doc_type where id = :id";
		Query q = entityManager.createNativeQuery(sql);
		q.setParameter("id", id);
		
		return (String) q.getSingleResult();
	}

}
