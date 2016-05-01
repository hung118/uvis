package gov.utah.va.vts.dao;

import java.util.List;

import gov.utah.va.vts.model.VeteranServicePeriod;

/**
 * VeteranServicePeriod Dao interface.
 * 
 * @author hnguyen
 *
 */
public interface VeteranServicePeriodDao extends BaseEntityDao<VeteranServicePeriod> {
		
	/**
	 * Finds veteran service periods by veteran id.
	 * @param veteranId
	 * @return
	 */
	public List<VeteranServicePeriod> findVeteranServicePeriods(Long veteranId);

	/**
	 * Deletes all veteranServicePeriods by veteran id.
	 * @param veteranId
	 */
	public void deleteVeteranServicePeriodsByVeteranId(Long veteranId);
	
	/**
	 * Deletes all veteran service period decoration medals (vsp_dm table) specified by veteranServicePeriodId.
	 * @param veteranServicePeriodId
	 */
	public void deleteVspDm(Long veteranServicePeriodId);
}
