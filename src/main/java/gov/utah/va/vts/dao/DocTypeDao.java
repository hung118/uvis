package gov.utah.va.vts.dao;

import gov.utah.va.vts.model.DocType;

/**
 * DocTypeDao interface.
 * 
 * @author hnguyen
 *
 */
public interface DocTypeDao extends BaseEntityDao<DocType> {
	
	/**
	 * Finds doc type name by ID.
	 * 
	 * @param id
	 * @return
	 */
	public String findDocNameById(Long id);
	
}
