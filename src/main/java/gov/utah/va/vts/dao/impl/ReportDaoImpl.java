package gov.utah.va.vts.dao.impl;

import java.util.List;

import javax.persistence.Query;

import gov.utah.va.vts.dao.ReportDao;
import gov.utah.va.vts.model.Report;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation class for ReportDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="reportDao")
@Transactional
public class ReportDaoImpl extends BaseEntityDaoImpl<Report> implements ReportDao {

	public ReportDaoImpl() {
		super(Report.class);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.ReportDao#findReportsByUserId(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<Report> findReportsByUserId(Long userId) {
		
		Query q;
		if (userId == null) {	// find all reports
			q = entityManager.createQuery("from Report order by report_name");
		} else { // find reports created by userId
			q = entityManager.createQuery("from Report where user_id = :userId order by report_name");
			q.setParameter("userId", userId);			
		}
		
		return q.getResultList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.ReportDao#findReportsBySharable()
	 */
	@SuppressWarnings("unchecked")
	public List<Report> findReportsBySharable() {
		
		Query q = entityManager.createQuery("from Report where sharable = 1 order by report_name");		
		return q.getResultList();
	}

}
