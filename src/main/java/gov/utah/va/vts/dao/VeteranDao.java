package gov.utah.va.vts.dao;

import java.util.Date;
import java.util.List;

import gov.utah.va.vts.model.Dashboard;
import gov.utah.va.vts.model.ReportProperty;
import gov.utah.va.vts.model.Veteran;

public interface VeteranDao extends BaseEntityDao<Veteran> {

	/**
	 * Search veterans.
	 * 
	 * @param reportProperty
	 * @return list of 300 max veteran objects.
	 */
	public List<Veteran> findVeterans(ReportProperty reportProperty);
	
	/**
	 * Search VSO veteran by ssn.
	 * 
	 * @param ssn
	 * @return
	 */
	public List<Veteran> findVeteranVSO(String ssn);
	
	/** Search ad hoc veterans.
	 * 
	 * @param reportProperty
	 * @return
	 */
	public List<Veteran> findVeteransAdhoc(ReportProperty reportProperty);
	
	/**
	 * Gets total search veteran records.
	 * 
	 * @param reportProperty
	 * @return
	 */
	public Integer getTotalVeteransCount(ReportProperty reportProperty);
	
	/**
	 * Get total search adhoc veteran records.
	 * @param sql
	 * @return
	 */
	public Integer getTotalVeteransCountAdhoc(String sql);
	
	/**
	 * Search all veterans not limited by 300. This method is called by report action class.
	 * 
	 * @param reportProperty
	 * @return
	 */
	public List<Veteran> findVeterans_all(ReportProperty reportProperty);
	
	/**
	 * Search adhoc all veterans not limited by 300. This method is called by report action class.
	 * 
	 * @param reportProperty
	 * @return
	 */
	public List<Veteran> findVeteransAdhoc_all(ReportProperty reportProperty);

	/**
	 * Finds veterans by ssn.
	 * @param ssn
	 * @return
	 */
	public List<Veteran> findVeteransBySsn(String ssn);
	
	/**
	 * Finds unreviewd veteran records.
	 * @return
	 */
	public List<Dashboard> findVeterans_unreviewed();
	
	/**
	 * Finds unreviewd veteran total records.
	 * @return
	 */
	public Integer findVeterans_unreviewed_total();

	/**
	 * Gets total number of unique SSN with specified date.
	 * @param date
	 * @return
	 */
	public Long getTotalUniqueSsn(Date date);
	
	/**
	 * Gets total number of unique SSN created during specified period.
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public Long getTotalUniqueSsn(Date fromDate, Date toDate);
	
	/**
	 * Gets total number of DD 214 created during specified period.
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public Long getTotalDD214(Date fromDate, Date toDate);
	
	/**
	 * Finds all veterans by zip codes.
	 * @param zipCodes
	 * @return list
	 */
	public List<Veteran> findVeteransByZipCodes(String zipCodes);
	
	/**
	 * Finds current veterans with email by sources and modified date range.
	 * @param sources
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public List<Veteran> findVeteransBySED(Long[] sources, Date fromDate, Date toDate);
	
}
