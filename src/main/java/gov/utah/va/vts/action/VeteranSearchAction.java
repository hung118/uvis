package gov.utah.va.vts.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.utah.dts.det.util.Util;
import gov.utah.va.vts.model.Report;
import gov.utah.va.vts.model.ReportProperty;
import gov.utah.va.vts.model.User;
import gov.utah.va.vts.model.Veteran;

/**
 * VeteranSearch action class.
 * @author hnguyen
 *
 */
public class VeteranSearchAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;

	private ReportProperty reportProperty;
	private List<Veteran> veterans = new ArrayList<Veteran>();
	private Report report;
	private String pageSize;
	private Integer totalRecordsCount = new Integer(0);
	private List<String> ageRanges;
	
	public String displaySearch() throws Exception {
		String target = "";
		Object searchObjSes = request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
		if (request.getParameter("adhoc") != null) {	// start adhoc search
			target = getText("SEARCH_ADHOC_RESULT");
		} else if (request.getParameter("vaCurrent") != null) { // start VSO search
			target = getText("SEARCH_VSO_RESULT");
		} else if (request.getParameter("standard") != null) {	// start standard search
			reportProperty = new ReportProperty();
			Integer[] ca = new Integer[1];
			ca[0] = new Integer(1);
			reportProperty.setVaCurrentArray(ca);
			target = getText("SEARCH_STANDARD_RESULT");
		} else if (searchObjSes != null && searchObjSes instanceof ReportProperty) {	// search object is on session
			reportProperty = (ReportProperty)searchObjSes;
			if (reportProperty.getAdhoc() != null && reportProperty.getAdhoc().booleanValue()) {	// adhoc search
				veterans = service.findVeteransAdhoc(reportProperty);
				totalRecordsCount = service.getTotalVeteransCountAdhoc(reportProperty.getSql());
				target = getText("SEARCH_ADHOC_RESULT");
			} else if (!"".equals(Util.convertNullToBlank(reportProperty.getSsn())) && "1".equals(reportProperty.getVaCurrent())) {	// vso search
				veterans = service.findVeteranVSO(reportProperty.getSsn());
				totalRecordsCount = 1;
				target = getText("SEARCH_VSO_RESULT");
			} else {	// standard search
				veterans = service.findVeterans(reportProperty);
				totalRecordsCount = service.getTotalVeteransCount(reportProperty);		
				target = getText("SEARCH_STANDARD_RESULT");
			}
		}		

		return target;
	}
	
	public String newSearch() throws Exception {
		reportProperty = null;
		request.getSession().removeAttribute(getText("SEARCH_OBJ_SES"));
		
		String target = "";
		if (request.getParameter("adhoc") != null) {	// adhoc new search
			target = getText("SEARCH_ADHOC_RESULT");
		} else {	// standard new search, there's no new VSO search
			reportProperty = new ReportProperty();
			Integer[] ca = new Integer[1];
			ca[0] = new Integer(1);
			reportProperty.setVaCurrentArray(ca);
			target = getText("SEARCH_STANDARD_RESULT");
		}

		return target;
	}
	
	public String search() throws Exception {
		logger.debug("entering search() ...");
		
		String target = "";
		if (reportProperty.getAdhoc() != null) {	// adhoc search
			veterans = service.findVeteransAdhoc(reportProperty);
			totalRecordsCount = service.getTotalVeteransCountAdhoc(reportProperty.getSql().replace("and rownum < 300", ""));
			target = getText("SEARCH_ADHOC_RESULT");
		} else if (reportProperty.getVaCurrent() != null) {	// VSO search
			StringBuffer ssn = new StringBuffer();
			ssn.append(reportProperty.getSsn1().length() < 1 ? "" : reportProperty.getSsn1());
			ssn.append(reportProperty.getSsn2().length() < 1 ? "" : "-" + reportProperty.getSsn2());
			ssn.append(reportProperty.getSsn3().length() < 1 ? "" : "-" + reportProperty.getSsn3());
			reportProperty.setSsn(ssn.toString());
			veterans = service.findVeteranVSO(reportProperty.getSsn());
			totalRecordsCount = 1;
			target = getText("SEARCH_VSO_RESULT");
		} else {	// standard search
			gatherData();
			veterans = service.findVeterans(reportProperty);
			totalRecordsCount = service.getTotalVeteransCount(reportProperty);
			target = getText("SEARCH_STANDARD_RESULT");
		}
		
		request.getSession().setAttribute(getText("SEARCH_OBJ_SES"), reportProperty);
		return target;
	}
			
	public String submitSearch() throws Exception {
		logger.debug("entering submitSearch ...");
		
		reportProperty = (ReportProperty)request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
		String target = "";
		if (reportProperty.getAdhoc() != null) {	// adhoc submit search
			veterans = service.findVeteransAdhoc(reportProperty);
			totalRecordsCount = service.getTotalVeteransCountAdhoc(reportProperty.getSql());
			target = getText("SEARCH_ADHOC_RESULT");
		} else {	// standard submit search, there's no submit VSO search
			gatherData();
			veterans = service.findVeterans(reportProperty);
			totalRecordsCount = service.getTotalVeteransCount(reportProperty);
			target = getText("SEARCH_STANDARD_RESULT");
		}

		return target;
	}
		
	public String page() throws Exception {
		
		logger.debug("entering page()...");
		
		reportProperty = (ReportProperty)request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
		String target = "";
		if (reportProperty.getAdhoc() != null) {
			veterans = service.findVeteransAdhoc(reportProperty);
			totalRecordsCount = service.getTotalVeteransCountAdhoc(reportProperty.getSql());
			target = getText("SEARCH_ADHOC_RESULT");
		} else {
			veterans = service.findVeterans(reportProperty);
			totalRecordsCount = service.getTotalVeteransCount(reportProperty);
			target = getText("SEARCH_STANDARD_RESULT");
		}
		
		return target;
	}
	
	public String setSessionPageSize() throws Exception {
		
		logger.debug("entering setSessionPageSize ...");
		
		String pageSizeStr = request.getParameter(getText("PAGESIZE_SES"));
		if ("all".equals(pageSizeStr)) {
			request.getSession().setAttribute(getText("PAGESIZE_SES"), "");
		} else {
			request.getSession().setAttribute(getText("PAGESIZE_SES"), pageSizeStr);
		}
		
		reportProperty = (ReportProperty)request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
		String target = "";
		if (reportProperty.getAdhoc() != null) {
			veterans = service.findVeteransAdhoc(reportProperty);
			totalRecordsCount = service.getTotalVeteransCountAdhoc(reportProperty.getSql());
			target = getText("SEARCH_ADHOC_RESULT");
		} else {
			veterans = service.findVeterans(reportProperty);
			totalRecordsCount = service.getTotalVeteransCount(reportProperty);
			target = getText("SEARCH_STANDARD_RESULT");
		}
		reportProperty = null;
		
		return target;
	}
	
	public void validate() {
		
		logger.debug("entering validate ...");
		if (request.getParameter("search") != null) {	// validate form only Search button is clicked
			if (reportProperty == null) {
				addActionError(getText("error.required", Util.getStringArray(getText("reportProperty.data"))));
			}
		}
	}
	
	public String saveQuery() throws Exception {	
		logger.debug("entering saveQuery ...");
		
		ReportProperty reportProperty = (ReportProperty)request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
		if (reportProperty.getAdhoc().booleanValue()) {	// Hibernate doesn't run get methods
			reportProperty.setObjNames(reportProperty.getObjNames());
			reportProperty.setObjValues(reportProperty.getObjValues());
			reportProperty.setOperators(reportProperty.getOperators());
			reportProperty.setSortedBy(reportProperty.getSortedBy());
			reportProperty.setSortedType(reportProperty.getSortedType());
			reportProperty.setSource(reportProperty.getSource());
		}
		
		reportProperty.setActive(new Integer(1));
		reportProperty.setInsertTimestamp(new Date());
		reportProperty.setUpdateTimestamp(new Date());
		service.saveReportProperty(reportProperty);

		report.setUser((User)request.getSession().getAttribute(getText("USER")));
		report.setReportProperty(reportProperty);
		report.setActive(new Integer(1));
		report.setInsertTimestamp(new Date());
		report.setUpdateTimestamp(new Date());
		service.saveReport(report);
		
		return getText("REPORT_LIST");
	}
	
	public void setVeterans(List<Veteran> veterans) {
		this.veterans = veterans;
	}

	public List<Veteran> getVeterans() {
		return veterans;
	}

	public ReportProperty getReportProperty() {
		return reportProperty;
	}

	public void setReportProperty(ReportProperty reportProperty) {
		this.reportProperty = reportProperty;
	}

	public Report getReport() {
		return report;
	}

	public void setReport(Report report) {
		this.report = report;
	}
	
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getPageSize() {
		pageSize = (String) request.getSession().getAttribute(getText("PAGESIZE_SES"));
		
		if (pageSize == null) {
			return "25";
		} else if ("all".equals(pageSize)){
			return "";
		} else {
			return pageSize;
		}
	}

	public Integer getTotalRecordsCount() {
		return totalRecordsCount;
	}

	public void setTotalRecordsCount(Integer totalRecordsCount) {
		this.totalRecordsCount = totalRecordsCount;
	}

	private void gatherData() {
		
		// gather data and put reportProperty search object on session for later use.
		if (reportProperty.getSourceArray().length > 0) {
			StringBuffer source = new StringBuffer("");
			for (int i = 0; i < reportProperty.getSourceArray().length; i++) {
				source.append(reportProperty.getSourceArray()[i].toString());
				if (i != reportProperty.getSourceArray().length - 1) {
					source.append(",");
				}
			}
			reportProperty.setSource(source.toString());
		}

		if (reportProperty.getDecorationMedalArray().length > 0) {
			StringBuffer decorationMedals = new StringBuffer("");
			for (int i = 0; i < reportProperty.getDecorationMedalArray().length; i++) {
				decorationMedals.append(reportProperty.getDecorationMedalArray()[i].toString());
				if (i != reportProperty.getDecorationMedalArray().length - 1) {
					decorationMedals.append(",");
				}
			}
			reportProperty.setDecorationMedals(decorationMedals.toString());
		}
		
		if (reportProperty.getVerifiedArray().length > 0) {
			StringBuffer verified = new StringBuffer("");
			for (int i = 0; i < reportProperty.getVerifiedArray().length; i++) {
				verified.append(reportProperty.getVerifiedArray()[i].toString());
				if (i != reportProperty.getVerifiedArray().length - 1) {
					verified.append(",");
				}
			}
			reportProperty.setVerified(verified.toString());
		}

		if (reportProperty.getVaCurrentArray() != null && reportProperty.getVaCurrentArray().length > 0) {
			StringBuffer vaCurrent = new StringBuffer("");
			for (int i = 0; i < reportProperty.getVaCurrentArray().length; i++) {
				vaCurrent.append(reportProperty.getVaCurrentArray()[i].toString());
				if (i != reportProperty.getVaCurrentArray().length - 1) {
					vaCurrent.append(",");
				}
			}
			reportProperty.setVaCurrent(vaCurrent.toString());
		}
		
		if (reportProperty.getReviewedArray().length > 0) {
			StringBuffer reviewed = new StringBuffer("");
			for (int i = 0; i < reportProperty.getReviewedArray().length; i++) {
				reviewed.append(reportProperty.getReviewedArray()[i].toString());
				if (i != reportProperty.getReviewedArray().length - 1) {
					reviewed.append(",");
				}
			}
		}
		
		reportProperty.setPrimaryPhone(reportProperty.getPrimaryPhone1() + "-" + reportProperty.getPrimaryPhone2() + "-" + reportProperty.getPrimaryPhone3());
		if (reportProperty.getPrimaryPhone().length() == 2) reportProperty.setPrimaryPhone("");
		reportProperty.setAltPhone(reportProperty.getAltPhone1() + "-" + reportProperty.getAltPhone2() + "-" + reportProperty.getAltPhone3());
		if (reportProperty.getAltPhone().length() == 2) reportProperty.setAltPhone("");
		
		StringBuffer ssn = new StringBuffer();
		ssn.append(reportProperty.getSsn1().length() < 1 ? "" : reportProperty.getSsn1());
		ssn.append(reportProperty.getSsn2().length() < 1 ? "" : "-" + reportProperty.getSsn2());
		ssn.append(reportProperty.getSsn3().length() < 1 ? "" : "-" + reportProperty.getSsn3());
		reportProperty.setSsn(ssn.toString());
		
		try {
			reportProperty.setDob(Util.convertToDate(reportProperty.getMonth().toString(), reportProperty.getDay().toString(), reportProperty.getYear().toString()));
		} catch (Exception e) {}
		try {
			reportProperty.setServicePeriodFrom(Util.convertToDate(reportProperty.getMonthFrom().toString(), reportProperty.getDayFrom().toString(), reportProperty.getYearFrom().toString()));
		} catch (Exception e) {}
		try {
			reportProperty.setServicePeriodTo(Util.convertToDate(reportProperty.getMonthTo().toString(), reportProperty.getDayTo().toString(), reportProperty.getYearTo().toString()));
		} catch (Exception e) {}
	}

	public List<String> getAgeRanges() {
		int increment = 5;
		int startAge = 20;
		int endAge = startAge + increment;
		ageRanges = new ArrayList<String>();
		while (endAge < 101) {
			ageRanges.add(startAge + "-" + endAge);
			startAge = endAge + 1;
			endAge = endAge + increment;
		}
		return ageRanges;
	}

	public void setAgeRanges(List<String> ageRanges) {
		this.ageRanges = ageRanges;
	}
	
}
