package gov.utah.va.vts.dao;

import java.util.Map;

import gov.utah.va.vts.model.ServiceBranch;

/**
 * ServiceBranch Dao interface.
 * 
 * @author hnguyen
 *
 */
public interface ServiceBranchDao extends BaseEntityDao<ServiceBranch> {
		
	/**
	 * Finds all lookup branches.
	 * @return Map object
	 */
	public Map<String, String> findLkupBranches();
	
}
