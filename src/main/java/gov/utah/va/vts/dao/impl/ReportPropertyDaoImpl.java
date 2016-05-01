package gov.utah.va.vts.dao.impl;

import gov.utah.va.vts.dao.ReportPropertyDao;
import gov.utah.va.vts.model.ReportProperty;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation class for ReportPropertyDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="reportPropertyDao")
@Transactional
public class ReportPropertyDaoImpl extends BaseEntityDaoImpl<ReportProperty> implements ReportPropertyDao {

	public ReportPropertyDaoImpl() {
		super(ReportProperty.class);
	}

}
