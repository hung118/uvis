package gov.utah.va.vts.dao.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import gov.utah.va.vts.dao.VeteranDao;
import gov.utah.va.vts.model.Dashboard;
import gov.utah.va.vts.model.ReportProperty;
import gov.utah.va.vts.model.Veteran;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation class for VeteranDao interface.
 * 
 * @author HNGUYEN
 *
 */
@SuppressWarnings("unchecked")
@Repository (value="veteranDao")
@Transactional
public class VeteranDaoImpl extends BaseEntityDaoImpl<Veteran> implements VeteranDao {
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	static final HashMap<String, String> sortMap = new HashMap<String, String>();
	static {
		sortMap.put("Last Name", "last_name");
		sortMap.put("First Name", "first_name");
		sortMap.put("City", "city");
		sortMap.put("Zip", "zip");
		sortMap.put("SSN", "ssn");
		sortMap.put("Date of Birth", "date_of_birth");
		sortMap.put("Source", "r.name");
		sortMap.put("Current", "va_current");
		sortMap.put("Verified", "verified");
		sortMap.put("Reviewed", "reviewed");
		sortMap.put("Receiving Compensation and Pension", "va_enrolled");
		sortMap.put("Reviewed", "reviewed");
		sortMap.put("Enrolled in VA Medical", "va_med_enrolled");
	}
	
	public VeteranDaoImpl() {
		super(Veteran.class);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranDao#findVeterans_2(gov.utah.va.vts.model.ReportProperty)
	 */
	public List<Veteran> findVeterans(ReportProperty reportProperty) {

		String sql = getSqlString(reportProperty);
		sql += "1 = 1 and rownum < 300 order by v.last_name, v.first_name";	// restrict to only 300 records to be displayed.
				
		logger.debug("sql: " + sql.toString());
		Query q = entityManager.createNativeQuery(sql.toString(), Veteran.class);
		
		// debug error: Can not set gov.utah.va.vts.model.Ethnicity field gov.utah.va.vts.model.Veteran.ethnicity to gov.utah.va.vts.model.RecordType
		// or javax.persistence.RollbackException: Transaction marked as rollbackOnly.
		// occurs in org.hibernat.property.DirectPropertyAccess line 105
		// to prevent this problem, removed (rollbackFor=Exception.class) in all daoImpl classes
		// id of ethnicity and of recordTye must be different.
		/*List<Veteran> tmp = null;
		try {
			tmp = q.getResultList();;
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		return tmp;*/
		
		return q.getResultList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranDao#findVeteranVSO(java.lang.String)
	 */
	public List<Veteran> findVeteranVSO(String ssn) {
		String hql = "from Veteran where ssn = :ssn and va_current = 1 ";
		
		Query q = entityManager.createQuery(hql);
		q.setParameter("ssn", ssn);
			
		return q.getResultList();	
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranDao#findVeteransAdhoc(gov.utah.va.vts.model.ReportProperty)
	 */
	public List<Veteran> findVeteransAdhoc(ReportProperty reportProperty) {
		String sql = getSqlAdhocString(reportProperty);
		sql += "rownum < 300 ";	// restrict to only 300 records to be displayed
		reportProperty.setSql(sql);
		String orderBy = "order by ";
		if (reportProperty.getSortedByArray() != null) {
			for (int i = 0; i < reportProperty.getSortedByArray().length; i++) {
				orderBy = orderBy + sortMap.get(reportProperty.getSortedByArray()[i]) + " " + reportProperty.getSortedTypeArray()[i];
				if (i < reportProperty.getSortedByArray().length - 1) {
					orderBy = orderBy + ",";
				}
			}
		} else {	// default to sort last_name
			String[] defaultSortBy = {"Last Name"};
			String[] defaultSortType = {"asc"};
			reportProperty.setSortedByArray(defaultSortBy);
			reportProperty.setSortedTypeArray(defaultSortType);
			orderBy = "order by last_name";
		}
		
		sql = sql + orderBy;
		Query q = entityManager.createNativeQuery(sql, Veteran.class);
				
		return q.getResultList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranDao#getTotalVeteransCount(gov.utah.va.vts.model.ReportProperty)
	 */
	public Integer getTotalVeteransCount(ReportProperty reportProperty) {
		
		String sql = getSqlString(reportProperty);
		sql += "1 = 1";
		sql = "select count(*) from (" + sql + ")";
		
		Query q = entityManager.createNativeQuery(sql);
		return new Integer(((BigDecimal)q.getSingleResult()).intValue());
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranDao#getTotalVeteransCountAdhoc(java.lang.String)
	 */
	public Integer getTotalVeteransCountAdhoc(String sql) {
		sql = "select count(*) from (" + sql + ")";
		
		Query q = entityManager.createNativeQuery(sql);
		return new Integer(((BigDecimal)q.getSingleResult()).intValue());
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranDao#findVeterans_all(gov.utah.va.vts.model.ReportProperty)
	 */
	public List<Veteran> findVeterans_all(ReportProperty reportProperty) {
		
		String sql = getSqlString(reportProperty);
		sql += "1 = 1 order by v.last_name, v.first_name";
				
		logger.debug("sql: " + sql.toString());
		Query q = entityManager.createNativeQuery(sql.toString(), Veteran.class);
				
		return q.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranDao#findVeteransAdhoc_all(gov.utah.va.vts.model.ReportProperty)
	 */
	public List<Veteran> findVeteransAdhoc_all(ReportProperty reportProperty) {
		
		String sql = getSqlAdhocString(reportProperty);
		sql += "1 = 1 order by v.last_name, v.first_name";
				
		logger.debug("sql: " + sql.toString());
		Query q = entityManager.createNativeQuery(sql.toString(), Veteran.class);
				
		return q.getResultList();
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranDao#findVeteran(java.lang.String)
	 */
	public List<Veteran> findVeteransBySsn(String ssn) {
		String hql = "from Veteran where ssn = :ssn order by source ";
		
		Query q = entityManager.createQuery(hql);
		q.setParameter("ssn", ssn);
			
		return q.getResultList();	
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranDao#findVeterans_unreviewed()
	 */
	public List<Dashboard> findVeterans_unreviewed() {
		
		StringBuffer sql = new StringBuffer("select ");
		sql.append("(select id from veteran_info where ssn = v.ssn and rownum < 2 ) as \"id\", ")
		.append(" ssn, ")
		.append("(select last_name from veteran_info where ssn = v.ssn and va_current = 1 and rownum < 2 ) as \"lastname\", ")
		.append("(select first_name from veteran_info where ssn = v.ssn and va_current = 1 and rownum < 2 ) as \"firstname\", ")
		.append("count(*) as \"number_of_sources\" ")
		.append("from veteran_info v ")
		.append("where v.reviewed = 0 and rownum < 26 ")		// limit to 25 records retrieved
		.append("group by v.ssn ")
		.append("order by 3, 4");
		
		List<Dashboard> dashboards = new ArrayList<Dashboard>();
		try {
			Query q = entityManager.createNativeQuery(sql.toString());
			List<Object[]> rows = q.getResultList();
			for (Object[] row : rows) {
				Dashboard d = new Dashboard();
				d.setId(new Long(((BigDecimal)row[0]).longValue()));
				d.setSsn((String)row[1]);
				d.setLastName((String)row[2]);
				d.setFirstName((String)row[3]);
				d.setNumberOfSources(new Integer(((BigDecimal)row[4]).intValue()));
				
				dashboards.add(d);
			}
		} catch (Exception e) {
			e.printStackTrace();	// for debug
		}
		
		return dashboards;
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranDao#findVeterans_unreviewed_total()
	 */
	public Integer findVeterans_unreviewed_total() {
		
		Integer total = null;
		
		final String SQL = "SELECT COUNT(*) FROM (SELECT ssn FROM VETERAN_INFO WHERE REVIEWED = 0 group by ssn)";
		Query q = entityManager.createNativeQuery(SQL);
		
		try {
			total = new Integer(((java.math.BigDecimal)q.getSingleResult()).intValue());
		} catch (NoResultException nre) {
			// return null when not found
		}
		
		return total;
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranDao#getTotalUniqueSsn(java.util.Date)
	 */
	@Override
	public Long getTotalUniqueSsn(Date date) {

		String sql = "select count(*) from " + 
				"(select unique(ssn) from veteran_info where trunc(insert_timestamp) <= trunc(to_date('" + sdf.format(date) + "', 'mm/dd/yyyy')))";
		
		Query q = entityManager.createNativeQuery(sql);
		Object result = q.getSingleResult();
		
		return new Long(((BigDecimal)result).longValue());
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranDao#getTotalUniqueSsn(java.util.Date, java.util.Date)
	 */
	@Override
	public Long getTotalUniqueSsn(Date fromDate, Date toDate) {
		
		String sql = "select count(*) from " + 
				"(select unique(ssn) from veteran_info where trunc(insert_timestamp) <= trunc(to_date('" + sdf.format(toDate) + "', 'mm/dd/yyyy')) and " +
				"trunc(insert_timestamp) >= trunc(to_date('" + sdf.format(fromDate) + "', 'mm/dd/yyyy')))";
		
		Query q = entityManager.createNativeQuery(sql);
		Object result = q.getSingleResult();
		
		return new Long(((BigDecimal)result).longValue());
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranDao#getTotalDD214(java.util.Date, java.util.Date)
	 */
	public Long getTotalDD214(Date fromDate, Date toDate) {
		
		String sql = "select count(*) from " + 
				"(select unique(ssn) from veteran_info where source = 2 and trunc(insert_timestamp) <= trunc(to_date('" + sdf.format(toDate) + "', 'mm/dd/yyyy')) and " +
				"trunc(insert_timestamp) >= trunc(to_date('" + sdf.format(fromDate) + "', 'mm/dd/yyyy')))";
		
		Query q = entityManager.createNativeQuery(sql);
		Object result = q.getSingleResult();
		
		return new Long(((BigDecimal)result).longValue());
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranDao#findVeteransByZipCodes(java.lang.String)
	 */
	@Override
	public List<Veteran> findVeteransByZipCodes(String zipCodes) {
		String sql = "select v.* from veteran_info v ";
		sql += "where v.date_of_death is null and v.va_current = 1 and substr(v.zip, 1, 5) in (" + convertToQuotesList(zipCodes) + ") and v.address1 is not null order by v.last_name, v.first_name";
				
		Query q = entityManager.createNativeQuery(sql, Veteran.class);
				
		return q.getResultList();	
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranDao#findVeteransBySED(java.lang.Long[], java.util.Date, java.util.Date)
	 */
	public List<Veteran> findVeteransBySED(Long[] sources, Date fromDate, Date toDate) {
		StringBuffer sb = new StringBuffer("(");
		for (int i = 0; i < sources.length; i++) {
			if (i == sources.length - 1) {
				sb.append("source = " + sources[i] + ")");
			} else {
				sb.append("source = " + sources[i] + " or ");
			}
		}
		
		String sql =
				"select * from veteran_info where date_of_death is null and va_current = 1 and email is not null and email_option = 2 and " + sb.toString() + " and trunc(update_timestamp) <= trunc(to_date('" + sdf.format(toDate) + "', 'mm/dd/yyyy')) and " +
				"trunc(update_timestamp) >= trunc(to_date('" + sdf.format(fromDate) + "', 'mm/dd/yyyy'))";
		
		Query q = entityManager.createNativeQuery(sql, Veteran.class);
		
		return q.getResultList();
	}
	
	private String getWildCardClause(ReportProperty reportProperty) {
		
		StringBuffer retStr = new StringBuffer("");
		
		if (reportProperty.getLastName() != null && (reportProperty.getLastName().contains("*") || reportProperty.getLastName().contains("?"))) {
			retStr.append("upper(v.last_name) like '" + reportProperty.getLastName().toUpperCase().replaceAll("\\*", "%").replaceAll("\\?", "_") + "' and ");
		}
		
		if (reportProperty.getFirstName() != null && (reportProperty.getFirstName().contains("*") || reportProperty.getFirstName().contains("?"))) {
			retStr.append("upper(v.first_name) like '" + reportProperty.getFirstName().toUpperCase().replaceAll("\\*", "%").replaceAll("\\?", "_") + "' and ");
		}
		
		if (reportProperty.getAddress1() != null && (reportProperty.getAddress1().contains("*") || reportProperty.getAddress1().contains("?"))) {
			retStr.append("upper(v.address1) like '" + reportProperty.getAddress1().toUpperCase().replaceAll("\\*", "%").replaceAll("\\?", "_") + "' and ");
		}
		
		if (reportProperty.getCity() != null && (reportProperty.getCity().contains("*") || reportProperty.getCity().contains("?"))) {
			retStr.append("upper(v.city) like '" + reportProperty.getCity().toUpperCase().replaceAll("\\*", "%").replaceAll("\\?", "_") + "' and ");
		}
		
		if (reportProperty.getEmail() != null && (reportProperty.getEmail().contains("*") || reportProperty.getEmail().contains("?"))) {
			retStr.append("upper(v.email) like '" + reportProperty.getEmail().toUpperCase().replaceAll("\\*", "%").replaceAll("\\?", "_") + "' and ");
		}
		
		if (reportProperty.getNote() != null && (reportProperty.getNote().contains("*") || reportProperty.getNote().contains("?"))) {
			retStr.append("upper(n.note_text) like '" + reportProperty.getNote().toUpperCase().replaceAll("\\*", "%").replaceAll("\\?", "_") + "' and ");
		}
		
		return retStr.toString();
	}

	private String getSqlString(ReportProperty reportProperty) {
		
		StringBuffer sql = new StringBuffer("select unique(v.id), v.* from " +
					"veteran_info v left join veteran_service_period vsp on v.id = vsp.veteran_id " +
					"left join veteran_benefit vb on v.id = vb.veteran_id " +
					"left join veteran_note vn on v.id = vn.veteran_id " +
					"left join note n on vn.note_id = n.id " +
					"left join veteran_decoration vd on v.id = vd.veteran_id " +
					"where ");
		
		if (reportProperty.getSourceArray().length > 0) {
			sql.append("(");
			for (int i = 0; i < reportProperty.getSourceArray().length; i++) {
				sql.append("v.source = " + reportProperty.getSourceArray()[i].intValue());
				if (i == reportProperty.getSourceArray().length - 1) {
					sql.append(" ");
				} else {
					sql.append(" or ");
				}
			}
			sql.append(") and ");			
		}
		
		if (reportProperty.getVaEnrolled()) {
			sql.append("v.va_enrolled = 1 and ");
		}
		
		if (reportProperty.getVaMedEnrolled()) {
			sql.append("v.va_med_enrolled = 1 and ");
		}
		
		if (reportProperty.getVerifiedArray().length == 1) {
			sql.append("v.verified = " + reportProperty.getVerifiedArray()[0].intValue() + " and ");
		}
		
		if (reportProperty.getVaCurrentArray() != null && reportProperty.getVaCurrentArray().length == 1) {
			sql.append("v.va_current = " + reportProperty.getVaCurrentArray()[0].intValue() + " and ");
		}
		
		if (reportProperty.getReviewedArray().length == 1) {
			sql.append("v.reviewed = " + reportProperty.getReviewedArray()[0].intValue() + " and ");
		}
		
		if (reportProperty.getDeceased()) {
			sql.append("v.date_of_death is not null and ");
		} else {
			sql.append("v.date_of_death is null and ");	// Redmine 14034
		}
		
		String wildCardClause = getWildCardClause(reportProperty); 
		if (!"".equals(wildCardClause)) { // handle wildcard case - Redmine 12092
			sql.append(wildCardClause);
		} else {		// non-wildcard case
			sql.append("upper(nvl(v.last_name, ' ')) like '%" + reportProperty.getLastName().trim().toUpperCase() + "%' and ");		
			sql.append("upper(nvl(v.first_name, ' ')) like '%" + reportProperty.getFirstName().trim().toUpperCase() + "%' and ");
			sql.append("upper(nvl(v.address1, ' ')) like '%" + reportProperty.getAddress1().trim().toUpperCase() + "%' and ");
			sql.append("upper(nvl(v.city, ' ')) like '%" + reportProperty.getCity().trim().toUpperCase() + "%' and ");
			sql.append("nvl(v.state, ' ') like '%" + reportProperty.getState() + "%' and ");
			sql.append("nvl(v.zip, ' ') like '%" + reportProperty.getZip() + "%' and ");
			sql.append("nvl(v.primary_phone, ' ') like '%" + reportProperty.getPrimaryPhone() + "%' and ");
			sql.append("nvl(v.alt_phone, ' ') like '%" + reportProperty.getAltPhone() + "%' and ");
			sql.append("v.ssn like '%" + reportProperty.getSsn()+ "%' and ");
			sql.append("lower(nvl(lower(v.email), ' ')) like '%" + reportProperty.getEmail().trim().toLowerCase() + "%' and ");
			sql.append("upper(nvl(v.gender, ' ')) like '%" + reportProperty.getGender().trim().toUpperCase() + "%' and ");
			sql.append("upper(nvl(v.rural, ' ')) like '%" + reportProperty.getRural() + "%' and ");
			
			if (reportProperty.getDob() != null) {
				sql.append("trunc(v.date_of_birth) = trunc(to_date('" + sdf.format(reportProperty.getDob()) + "', 'mm/dd/yyyy')) and ");
			} else {	// search for year only
				if (reportProperty.getYear() != null) {
					sql.append("to_char(v.date_of_birth, 'yyyy') = '" + reportProperty.getYear().toString() + "' and ");
				}
			}
			
			if (reportProperty.getServiceEraId() != null) {
				sql.append("vsp.service_era_id = " + reportProperty.getServiceEraId().intValue()  + " and ");
			}
			
			if (reportProperty.getCombatZoneId() != null) {
				sql.append("vsp.combat_zone_id = " + reportProperty.getCombatZoneId().intValue() + " and ");
			}
			
			if (reportProperty.getDischargeTypeId() != null) {
				sql.append("vsp.discharge_type_id = " + reportProperty.getDischargeTypeId().intValue() + " and ");
			}
			
			if (reportProperty.getServiceBranchId() != null) {
				sql.append("vsp.service_branch_id = " + reportProperty.getServiceBranchId().intValue() + " and ");
			}
			
			if (reportProperty.getServicePeriodFrom() != null && reportProperty.getServicePeriodTo() != null) {
				sql.append("trunc(vsp.start_date) >= trunc(to_date('" + sdf.format(reportProperty.getServicePeriodFrom()) + "', 'mm/dd/yyyy')) and ");
				sql.append("trunc(vsp.end_date) <= trunc(to_date('" + sdf.format(reportProperty.getServicePeriodTo()) + "', 'mm/dd/yyyy')) and ");
			} else if (reportProperty.getServicePeriodFrom() != null) {
				sql.append("trunc(vsp.start_date) = trunc(to_date('" + sdf.format(reportProperty.getServicePeriodFrom()) + "', 'mm/dd/yyyy')) and ");
			} else if (reportProperty.getServicePeriodTo() != null) {
				sql.append("trunc(vsp.end_date) = trunc(to_date('" + sdf.format(reportProperty.getServicePeriodTo()) + "', 'mm/dd/yyyy')) and ");
			}
			
			if (reportProperty.getBenefitTypeId() != null) {
				sql.append("vb.benefit_type_id = " + reportProperty.getBenefitTypeId().intValue() + " and ");
			}
			
			if (reportProperty.getNote() != null) {
				sql.append("nvl(upper(n.note_text), ' ') like '%" + reportProperty.getNote().trim().toUpperCase() + "%' and ");
			}	
			
			if (reportProperty.getDecorationMedalArray().length > 0) {
				sql.append("(");
				for (int i = 0; i < reportProperty.getDecorationMedalArray().length; i++) {
					sql.append("vd.decoration_medal_id = " + reportProperty.getDecorationMedalArray()[i].intValue());
					if (i == reportProperty.getDecorationMedalArray().length - 1) {
						sql.append(" ");
					} else {
						sql.append(" or ");
					}
				}
				sql.append(") and ");			
			}
			
			if (!(reportProperty.getAgeRange() == null || "".equals(reportProperty.getAgeRange()))) {
				sql.append(convertAgeRange(reportProperty.getAgeRange()));
			}
			
		}
		
		return sql.toString();
	}
	
	/**
	 * Converts age range to date range interval where clause.
	 * 
	 * @param ageRange is of format: 20-25 or 26-30 till 100.
	 * @return String
	 */
	private String convertAgeRange(String ageRange) {
		String[] ages = ageRange.split("-");
		int fromAge = Integer.parseInt(ages[0]);
		int toAge = Integer.parseInt(ages[1]);
		
		Calendar fromDate = Calendar.getInstance();
		fromDate.add(Calendar.YEAR, -fromAge);		
		Calendar toDate = Calendar.getInstance();
		toDate.add(Calendar.YEAR, -toAge);
		
		String retString = "trunc(v.date_of_birth) >= trunc(to_date('" + sdf.format(toDate.getTime()) + "', 'mm/dd/yyyy')) and " +
				"trunc(v.date_of_birth) <= trunc(to_date('" + sdf.format(fromDate.getTime()) + "', 'mm/dd/yyyy')) and ";
		
		return retString;
	}
	
	private String getSqlAdhocString(ReportProperty reportProperty) {
		StringBuffer sql = new StringBuffer("select unique(v.id), r.name, v.* from " +
				"veteran_info v left join veteran_service_period vsp on v.id = vsp.veteran_id " +
				"left join veteran_benefit vb on v.id = vb.veteran_id " +
				"left join veteran_note vn on v.id = vn.veteran_id " +
				"left join note n on vn.note_id = n.id " +
				"left join veteran_decoration vd on v.id = vd.veteran_id " +
				"left join record_type r on v.source = r.id " +
				"where ");
	
		int index = 0;
		boolean deceasedFound = false;
		while (index < reportProperty.getObjectNames().length) {
			if ("Last Name".equals(reportProperty.getObjectNames()[index])) {
				sql.append("upper(v.last_name) " + getOpWithValue(reportProperty.getOperatorsArray()[index], reportProperty.getObjectValues()[index].trim().toUpperCase()) + " and ");
			} else if ("First Name".equals(reportProperty.getObjectNames()[index])) {
				sql.append("upper(v.first_name) " + getOpWithValue(reportProperty.getOperatorsArray()[index], reportProperty.getObjectValues()[index].trim().toUpperCase()) + " and ");
			} else if ("SSN".equals(reportProperty.getObjectNames()[index])) {
				sql.append("v.ssn " + getOpWithValue(reportProperty.getOperatorsArray()[index], reportProperty.getObjectValues()[index]) + " and ");		
			} else if ("Street Address".equals(reportProperty.getObjectNames()[index])) {
				sql.append("upper(v.address1) " + getOpWithValue(reportProperty.getOperatorsArray()[index], reportProperty.getObjectValues()[index].trim().toUpperCase()) + " and ");
			} else if ("City".equals(reportProperty.getObjectNames()[index])) {
				sql.append("upper(v.city) " + getOpWithValue(reportProperty.getOperatorsArray()[index], reportProperty.getObjectValues()[index].trim().toUpperCase()) + " and ");
			} else if ("State".equals(reportProperty.getObjectNames()[index])) {
				sql.append("upper(v.state) " + getOpWithValue(reportProperty.getOperatorsArray()[index], reportProperty.getObjectValues()[index].trim().toUpperCase()) + " and ");
			} else if ("Zip".equals(reportProperty.getObjectNames()[index])) {
				sql.append("v.zip " + getOpWithValue(reportProperty.getOperatorsArray()[index], reportProperty.getObjectValues()[index].trim()) + " and ");
			} else if ("Primary Phone".equals(reportProperty.getObjectNames()[index])) {
				sql.append("v.primary_phone " + getOpWithValue(reportProperty.getOperatorsArray()[index], reportProperty.getObjectValues()[index].trim()) + " and ");
			} else if ("Alternate Phone".equals(reportProperty.getObjectNames()[index])) {
				sql.append("v.alt_phone " + getOpWithValue(reportProperty.getOperatorsArray()[index], reportProperty.getObjectValues()[index].trim()) + " and ");
			} else if ("Date of Birth".equals(reportProperty.getObjectNames()[index])) {
				sql.append("v.date_of_birth between to_date('" + reportProperty.getObjectValues()[index] + "', 'mm/dd/yyyy') and to_date('" + reportProperty.getObjectValues()[index + 1] + "', 'mm/dd/yyyy') and ");
				index = index + 1;	
			} else if ("Email".equals(reportProperty.getObjectNames()[index])) {
				sql.append("upper(v.email) " + getOpWithValue(reportProperty.getOperatorsArray()[index], reportProperty.getObjectValues()[index].trim().toUpperCase()) + " and ");
			} else if ("Gender".equals(reportProperty.getObjectNames()[index])) {
				sql.append("v.gender = '" + reportProperty.getGender() + "' and ");
			} else if ("Current".equals(reportProperty.getObjectNames()[index])) {
				if (new Boolean(reportProperty.getObjectValues()[index]).booleanValue()) {
					sql.append("v.va_current = 1 and ");
				} else {
					sql.append("v.va_current = 0 and ");
				}
			} else if ("Deceased".equals(reportProperty.getObjectNames()[index])) {
				if (new Boolean(reportProperty.getObjectValues()[index]).booleanValue()) {
					sql.append("v.date_of_death is not null and ");
				} else {
					sql.append("v.date_of_death is null and ");
				}
				deceasedFound = true;
			} else if ("Receiving Compensation and Pension".equals(reportProperty.getObjectNames()[index])) {
				if (new Boolean(reportProperty.getObjectValues()[index]).booleanValue()) {
					sql.append("v.va_enrolled = 1 and ");
				} else {
					sql.append("v.va_enrolled = 0 and ");
				}
			} else if ("Enrolled in VA Medical".equals(reportProperty.getObjectNames()[index])) {
				if (new Boolean(reportProperty.getObjectValues()[index]).booleanValue()) {
					sql.append("v.va_med_enrolled = 1 and ");
				} else {
					sql.append("v.va_med_enrolled = 0 and ");
				}
			} else if ("Verified".equals(reportProperty.getObjectNames()[index])) {
				if (new Boolean(reportProperty.getObjectValues()[index]).booleanValue()) {
					sql.append("v.verified = 1 and ");
				} else {
					sql.append("v.verified = 0 and ");
				}
			} else if ("Reviewed".equals(reportProperty.getObjectNames()[index])) {
				if (new Boolean(reportProperty.getObjectValues()[index]).booleanValue()) {
					sql.append("v.reviewed = 1 and ");
				} else {
					sql.append("v.reviewed = 0 and ");
				}
			} else if ("Zip Code Designation".equals(reportProperty.getObjectNames()[index])) {
				sql.append("v.rural = '" + reportProperty.getObjectValues()[index] + "' and ");
			} else if ("Source".equals(reportProperty.getObjectNames()[index])) {
				sql.append("v.source in (");
				for (int i = 0; i < reportProperty.getSourceArray().length; i++) {
					sql.append(reportProperty.getSourceArray()[i]);
					if (i < reportProperty.getSourceArray().length - 1) {
						sql.append(",");
					}
				}
				sql.append(") and ");
			}
			
			index = index + 1;
		}
		
		if (!deceasedFound) {	// default to exclude deceased
			sql.append("v.date_of_death is null and ");
		}
				
		return sql.toString();
	}
	
	private String getOpWithValue(String op, String value) {
		String ret = "";
		switch (Integer.parseInt(op)) {
		case 1:	// equals
			ret = "= '" + value + "'";
			break;
		case 2:	// starts with
			ret = "like '" + value + "%'";
			break;
		case 3: 	// ends with
			ret = "like '%" + value + "'";
			break;
		case 4:		// contains
			ret = "like '%" + value + "%'";
			break;
		case 5:					
			ret = "in (" + convertToQuotesList(value) + ")";
			break;
		}
		
		return ret;
	}
	
	/**
	 * Converts list of numeric string separated by spaces to list of single quotes string for db uses.
	 * @param numStrList
	 * @return
	 */
	private String convertToQuotesList(String numStrList) {
		String ret = "";
		String[] temp = numStrList.trim().split(" ");
		for (int i = 0; i < temp.length; i++) {
			ret += "'" + temp[i] + "'";
			if (i < temp.length - 1) {
				ret += ",";
			}
		}
		
		return ret;
	}

}
