package gov.utah.va.vts.dao;

import java.util.List;

import gov.utah.va.vts.model.DataImport;

public interface DataImportDao extends BaseEntityDao<DataImport> {

	/**
	 * Finds data import records by source id.
	 * @param sourceId
	 * @return
	 */
	public List<DataImport> findDataImportsBySource(Long sourceId);
	
}
