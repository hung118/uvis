package gov.utah.va.vts.dao;

import java.util.List;

import gov.utah.va.vts.model.Report;

/**
 * Report Dao interface.
 * 
 * @author hnguyen
 *
 */
public interface ReportDao extends BaseEntityDao<Report> {
	
	/**
	 * Finds all reports specified by user id.
	 * @param userId
	 * @return
	 */
	public List<Report> findReportsByUserId(Long userId);
	
	/**
	 * Finds all sharable reports.
	 * @return
	 */
	public List<Report> findReportsBySharable();
}
