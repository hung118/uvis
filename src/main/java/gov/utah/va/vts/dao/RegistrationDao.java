package gov.utah.va.vts.dao;

import java.util.List;

import gov.utah.va.vts.model.Registration;

public interface RegistrationDao extends BaseEntityDao<Registration> {

	/**
	 * Finds all registration records that have not been imported.
	 * @return list of Registration objects.
	 */
	public List<Registration> findRegisteredRecords();
	
}
