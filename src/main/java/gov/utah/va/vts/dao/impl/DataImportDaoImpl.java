package gov.utah.va.vts.dao.impl;

import java.util.List;

import javax.persistence.Query;

import gov.utah.va.vts.dao.DataImportDao;
import gov.utah.va.vts.model.DataImport;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation class from DataImportDao interface.
 * @author HNGUYEN
 *
 */
@Repository (value="dataImportDao")
@Transactional
public class DataImportDaoImpl extends BaseEntityDaoImpl<DataImport> implements DataImportDao {

	public DataImportDaoImpl() {
		super(DataImport.class);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.DataImportDao#findDataImportsBySource(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<DataImport> findDataImportsBySource(Long sourceId) {
		String sql =  "select * from (select d.* from data_import d where d.source = :sourceId order by d.insert_timestamp desc) where rownum < 501";
		
		Query q = entityManager.createNativeQuery(sql, DataImport.class);
		q.setParameter("sourceId", sourceId);
		
		return q.getResultList();
	}

}
