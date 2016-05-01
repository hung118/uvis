package gov.utah.va.vts.action;

import gov.utah.dts.det.util.ReportUtil;
import gov.utah.dts.det.util.Util;
import gov.utah.dts.det.util.Validate;
import gov.utah.va.vts.model.BenefitRecipient;
import gov.utah.va.vts.model.BenefitType;
import gov.utah.va.vts.model.DecorationMedal;
import gov.utah.va.vts.model.Report;
import gov.utah.va.vts.model.ReportProperty;
import gov.utah.va.vts.model.ScoreCard;
import gov.utah.va.vts.model.User;
import gov.utah.va.vts.model.Veteran;
import gov.utah.va.vts.model.VeteranAttachment;
import gov.utah.va.vts.model.VeteranBenefit;
import gov.utah.va.vts.model.VeteranServicePeriod;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperCompileManager;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.ImageIOUtil;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.util.ServletContextAware;

/**
 * Report action class.
 * @author hnguyen
 *
 */
public class ReportAction extends BaseActionSupport implements ServletContextAware, ServletResponseAware {

	private static final long serialVersionUID = 1L;
	
	private List<Report> reportList;
	private List<Veteran> veterans;
	private ServletContext servletContext;
	private String imagePathName;
	private String subreportDir;
	
	private ScoreCard scoreCard;
	private String zipCodes;
	private Date fromDate;
	private Date toDate;
	private Long[] sourceArray;
	private Integer randomCount;
	
	private HttpServletResponse response;
	
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	public String list() throws Exception {
		
		logger.debug("entering list ...");
		String type = request.getParameter("type");
		int reportType;
		if (type == null) {
			reportType = 1; // user reports default
		} else {
			reportType = Integer.parseInt(type);
		}
		
		request.getSession().setAttribute(getText("REPORT_TYPE_SES"), new Integer(reportType));
		
		return SUCCESS;
	}

	public String delete() throws Exception {
		
		logger.debug("entering delete ...");
		
		Long id = new Long(request.getParameter("id"));
		Report report = service.findReportById(id);
		
		ReportProperty reportProperty = new ReportProperty();
		reportProperty.setId(report.getReportProperty().getId());
		
		service.deleteReport(report);
		service.deleteReportProperty(reportProperty);
		
		return SUCCESS;
	}
	
	public String run() throws Exception {
		
		logger.debug("entering run...");
		
		Long id = new Long(request.getParameter("id"));
		Report report = service.findReportById(id);
		ReportProperty reportProperty = report.getReportProperty();
		
		// gather data for report entity before put on session for used in displaySearchVeteranSearch action
		if (reportProperty.getAdhoc() != null && reportProperty.getAdhoc().booleanValue()) {
			// nothing, being done in the bean
		} else {
			setStandardSearch(reportProperty);
		}
		
		request.getSession().setAttribute(getText("SEARCH_OBJ_SES"), reportProperty);
		return getText("FORWARD_TO_SEARCH");
	}
	
	public String generatePdf() throws Exception {
		ReportProperty reportProperty = (ReportProperty)request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
		veterans = service.findVeterans_all(reportProperty);
		
		return doGeneratePdf();
	}
	
	public String generatePdfAdhoc() throws Exception {
		ReportProperty reportProperty = (ReportProperty)request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
		veterans = service.findVeteransAdhoc_all(reportProperty);
		
		return doGeneratePdf();
	}

	public String generatePdfInd() throws Exception {
		String ssn = request.getParameter("ssn");
		veterans = service.findVeteranVSO(ssn);
		
		return doGeneratePdf();
	}
	
	public String generateMailingList() throws Exception {
		
		logger.debug("entering generateMailingList ...");
		
		ReportProperty reportProperty = (ReportProperty)request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
		veterans = service.findVeterans_all(reportProperty);
		
		return getText("MAILINGLIST");
	}
	
	public String generateMailingAdhocList() throws Exception {
		ReportProperty reportProperty = (ReportProperty)request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
		veterans = service.findVeteransAdhoc_all(reportProperty);
		
		return getText("MAILINGLIST");
	}
	
	public String generateCsv() throws Exception {
		ReportProperty reportProperty = (ReportProperty)request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
		veterans = service.findVeterans_all(reportProperty);
		
		doCsv();
		
		return null;
	}
	
	public String generateCsvAdhoc() throws Exception {
		ReportProperty reportProperty = (ReportProperty)request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
		veterans = service.findVeteransAdhoc_all(reportProperty);
		
		doCsv();
		
		return null;
	}

	public String generateXls() throws Exception {
		
		logger.debug("entering generateXls ...");
		
		ReportProperty reportProperty = (ReportProperty)request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
		veterans = service.findVeterans_all(reportProperty);
		
		return getText("XLS_REPORT");		
	}
	
	public String generateXlsAdhoc() throws Exception {
		ReportProperty reportProperty = (ReportProperty)request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
		veterans = service.findVeteransAdhoc_all(reportProperty);
		
		return getText("XLS_REPORT");		
	}
	
	public String scoreCard() throws Exception {
		return SUCCESS;
	}
	
	public String generateScoreCard() throws Exception {
		
		logger.debug("entering generateScoreCard ...");
		
		scoreCard.setTotalUniqueSsn(service.getTotalUniqueSsn(scoreCard.getToDate()));
		scoreCard.setTotalUniqueSsnPeriod(service.getTotalUniqueSsn(scoreCard.getFromDate(), scoreCard.getToDate()));
		scoreCard.setTotalDD214(service.getTotalDD214(scoreCard.getFromDate(), scoreCard.getToDate()));
		
		return "scoreCardReport";
	}
	
	public void validateGenerateScoreCard() {
		fromTodateValidation(scoreCard.getFromDate(), scoreCard.getToDate());
	}
		
	public String zipMailingList() throws Exception {
		return SUCCESS;
	}

	public String generateZipMailingList() throws Exception {
		
		logger.debug("entering generateZipMailingList ...");
		
		veterans = service.findVeteransByZipCodes(zipCodes);
		
		return "zipMailingList";
	}
	
	public void validateGenerateZipMailingList() {
		if (Validate.isEmpty(zipCodes)) {
			addActionError(getText("error.required", Util.getStringArray(getText("report.zipCodes"))));
		}
	}
	
	public String emailList() throws Exception {
		sourceArray = new Long[] {7L};
		return SUCCESS;
	}
	
	public String generateEmailList() throws Exception {
		veterans = service.findVeteransBySED(sourceArray, fromDate, toDate);
		return "emailList";
	}
	
	public void validateGenerateEmailList() throws Exception {
		if (sourceArray.length == 0) {
			addActionError(getText("error.required", Util.getStringArray(getText("recordType"))));
		}
		fromTodateValidation(getFromDate(), getToDate());
	}
	
	public String emailRandom() throws Exception {
		sourceArray = new Long[] {7L};
		setRandomCount(1);
		return SUCCESS;
	}
	
	public String generateEmailRandom() throws Exception {
		List<Veteran> tmpVeterans = service.findVeteransBySED(sourceArray, fromDate, toDate);
		if (randomCount > tmpVeterans.size()) {
			addActionError(getText("error.randomCount", Util.getStringArray(randomCount.toString(), String.valueOf(tmpVeterans.size()))));
			return INPUT;
		}
		
		veterans = new ArrayList<Veteran>();
		int rc = randomCount.intValue();
		int max = tmpVeterans.size() - 1;
		for (int i = 0; i < rc; i++) {
			int c = Util.getRandomNumberInRange(0, max);
			veterans.add(tmpVeterans.get(c));
			tmpVeterans.remove(c);
			max = max - 1;
		}
		
		return "emailRandom";
	}
	
	public void validateGenerateEmailRandom() {
		if (randomCount == null) {
			addActionError(getText("error.required", Util.getStringArray(getText("report.randomCount"))));
		}
		if (sourceArray.length == 0) {
			addActionError(getText("error.required", Util.getStringArray(getText("recordType"))));
		}
		fromTodateValidation(getFromDate(), getToDate());;
	}
	
	public List<Report> getReportList() {
		
		Integer reportType = (Integer)request.getSession().getAttribute(getText("REPORT_TYPE_SES"));
		User user = (User)request.getSession().getAttribute(getText("USER"));
		switch (reportType.intValue()) {
			case 1:	// user reports
				reportList = service.findReportsByUserId(user.getId());		
				break;
			case 2:	// system (sharable) reports
				reportList = service.findReportsBySharable();
				break;
			case 3:	// all reports
				reportList = service.findReportsByUserId(null);
				break;
			default:
				reportList = service.findReportsByUserId(user.getId());		
		}
				
		return reportList;
	}

	public void setReportList(List<Report> reportList) {
		this.reportList = reportList;
	}

	public List<Veteran> getVeterans() {
		return veterans;
	}

	public void setVeterans(List<Veteran> veterans) {
		this.veterans = veterans;
	}

	@Override
	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}

	public String getImagePathName() {
		imagePathName = servletContext.getRealPath("/images/" + getText("report.image"));
		return imagePathName;
	}

	public void setImagePathName(String imagePathName) {
		this.imagePathName = imagePathName;
	}

	public String getSubreportDir() {
		subreportDir = servletContext.getRealPath("/WEB-INF/jasper");
		return subreportDir;
	}

	public void setSubreportDir(String subreportDir) {
		this.subreportDir = subreportDir;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public ScoreCard getScoreCard() {
		return scoreCard;
	}

	public void setScoreCard(ScoreCard scoreCard) {
		this.scoreCard = scoreCard;
	}

	public String getZipCodes() {
		return zipCodes;
	}

	public void setZipCodes(String zipCodes) {
		this.zipCodes = zipCodes;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Long[] getSourceArray() {
		return sourceArray;
	}

	public void setSourceArray(Long[] sourceArray) {
		this.sourceArray = sourceArray;
	}

	public Integer getRandomCount() {
		return randomCount;
	}

	public void setRandomCount(Integer randomCount) {
		this.randomCount = randomCount;
	}

	private void fromTodateValidation(Date from, Date to) {
		if (from != null && to != null) {
			if (Validate.compareDate(to, from) == -1) {
				addActionError(getText("error.toBeforeFrom_2"));
			} else {
				Date today = new Date();
				if (Validate.compareDate(from, today) == 1)
					addActionError(getText("error.beforeToday", Util.getStringArray(getText("report.fromDate"), sdf.format(today))));					
				if (Validate.compareDate(to, today) == 1)
					addActionError(getText("error.beforeToday", Util.getStringArray(getText("report.toDate"), sdf.format(today))));
			}
								
		} else {
			addActionError(getText("error.required_2", Util.getStringArray(getText("report.fromDate"), getText("report.toDate"))));
		}
	}
	
	/**
	 * Compiles Jasper reports. This is a workaround for subreport problem with iReport (struts2-jasperreports-plugin-2.2.1.jar
	 * but struts2-jasperreports-plugin-2.0.11.1.jar works fine) compilation error:
	 * java.lang.ClassNotFoundException: org.apache.struts2.views.jasperreports.ValueStackDataSource.
	 * @throws Exception
	 */
	private void compileJasperReports() throws Exception {
		
		
		String realPath = servletContext.getRealPath("/WEB-INF/jasper");
		JasperCompileManager.compileReportToFile(
				realPath + "/pdfReport.jrxml",
				realPath + "/pdfReport.jasper");
		JasperCompileManager.compileReportToFile(
				realPath + "/noteHistorySub.jrxml",
				realPath + "/noteHistorySub.jasper");		
		JasperCompileManager.compileReportToFile(
				realPath + "/servicePeriodSub.jrxml",
				realPath + "/servicePeriodSub.jasper");				
		JasperCompileManager.compileReportToFile(
				realPath + "/decorationMedalSub.jrxml",
				realPath + "/decorationMedalSub.jasper");	
		JasperCompileManager.compileReportToFile(
				realPath + "/benefitTypeSub.jrxml",
				realPath + "/benefitTypeSub.jasper");	
		JasperCompileManager.compileReportToFile(
				realPath + "/benefitRecipientSub.jrxml",
				realPath + "/benefitRecipientSub.jasper");	
		JasperCompileManager.compileReportToFile(
				realPath + "/pdfReportImage.jrxml",
				realPath + "/pdfReportImage.jasper");
		JasperCompileManager.compileReportToFile(
				realPath + "/attachmentsSub.jrxml",
				realPath + "/attachmentsSub.jasper");
	}

	/**
	 * Output csv format with specified string contents.
	 * @param aContents
	 * @throws Exception
	 */
	private void csvOut(String aContents) throws Exception {

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=VeteransReport.csv");
		ServletOutputStream out = response.getOutputStream();
		
		out.write(aContents.getBytes());
		
		out.flush();
		out.close();
	}
	
	private List<VeteranAttachment> removeNonImages(List<VeteranAttachment> oldList) throws Exception {
		
		List<VeteranAttachment> newList = new ArrayList<VeteranAttachment>();
		
		for (VeteranAttachment va : oldList) {
			if (va.getAttachmentContentType().contains("pdf")) {	// convert pdf to jpg images - Jasper reports (v 3.7.5 or 5.1.0) doesn't support imbeded pdf yet.	
				ArrayList<byte[]> arrayBytes = convertPdfToImages(va.getAttachment());
				for (int i = 0; i < arrayBytes.size(); i++) {
					VeteranAttachment pdfPageImage = new VeteranAttachment();
					pdfPageImage.setAttachment(arrayBytes.get(i));
					newList.add(pdfPageImage);
				}
			} else if (va.getAttachmentContentType().contains("image")) {
				newList.add(va);
			}
		}
		
		return newList;
	}
	
	private ArrayList<byte[]> convertPdfToImages(byte[] pdf) throws Exception {
		// set initial values
        int startPage = 1;
        int endPage = Integer.MAX_VALUE;
        String imageFormat = "jpg";
        int resolution;
		
        try {
            resolution = Toolkit.getDefaultToolkit().getScreenResolution();
        } catch( HeadlessException e ) {
            resolution = 96;
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PDDocument document = null;
        ArrayList<byte[]> results = new ArrayList<byte[]>();
        try {
            document = PDDocument.load(new ByteArrayInputStream(pdf));
            int imageType = BufferedImage.TYPE_INT_RGB;
        	
            @SuppressWarnings("unchecked")
			List<PDPage> pages = document.getDocumentCatalog().getAllPages();
            int pagesSize = pages.size();            
            for (int i = startPage - 1; i < endPage && i < pagesSize; i++) {
            	PDPage page = pages.get(i);
            	BufferedImage image = page.convertToImage(imageType, resolution);
            	ImageIOUtil.writeImage(image, imageFormat, baos);
            	results.add(baos.toByteArray());
            	baos.reset();
            }
            
    		return results;
        } finally {
        	baos.close();
       		document.close();
        }        
	}	
	
	private String doGeneratePdf() throws Exception {
		// find/set benefit recipients
		for (Veteran v : veterans) {
			for (BenefitType b : v.getBenefitTypeList()) {
				VeteranBenefit veteranBenefit = service.findVeteranBenefitType(v.getId(), b.getId());
				b.setBenefitRecipients(service.findVeteranBenefitRecipients(veteranBenefit.getId()));
			}
		}
		
		// compile Jasper reports if needed.
		if (!new File(servletContext.getRealPath("/WEB-INF/jasper/pdfReport.jasper")).exists() ||
				!new File(servletContext.getRealPath("/WEB-INF/jasper/pdfReportImage.jasper")).exists()) {
			compileJasperReports();
		}
		
		String target = "PDF_REPORT";
		if (request.getParameter("image") != null) {	// generate image attachments
			target = "PDF_REPORT_IMAGE";
			for (Veteran v : veterans) {
				if (v.getAttachmentsList() != null && !v.getAttachmentsList().isEmpty()) {
					v.setAttachmentsList(removeNonImages(v.getAttachmentsList()));
					for (VeteranAttachment va : v.getAttachmentsList()) {
						va.setAttachmentIs(new ByteArrayInputStream(va.getAttachment()));
						v.setIsAttachmentList(new Boolean(true));
					}
				} else {
					v.setIsAttachmentList(new Boolean(false));
				}
			}
		}
		
		return getText(target);
	}
	
	private void setStandardSearch(ReportProperty reportProperty) {
		if (reportProperty.getSource() != null) {
			String[] sourceStringArray = reportProperty.getSource().split(",");
			Long[] sourceArray = new Long[sourceStringArray.length];
			for (int i = 0; i < sourceStringArray.length; i++) sourceArray[i] = Long.parseLong(sourceStringArray[i]);
			reportProperty.setSourceArray(sourceArray);
		} else {
			reportProperty.setSourceArray(new Long[0]);
		}
		
		if (reportProperty.getDecorationMedals() != null) {
			String[] decorationMedalStringArray = reportProperty.getDecorationMedals().split(",");
			Long[] decorationMedalArray = new Long[decorationMedalStringArray.length];
			for (int i = 0; i < decorationMedalStringArray.length; i++) decorationMedalArray[i] = Long.parseLong(decorationMedalStringArray[i]);
			reportProperty.setDecorationMedalArray(decorationMedalArray);
		} else {
			reportProperty.setDecorationMedalArray(new Long[0]);
		}
		
		if (reportProperty.getVerified() != null) {
			String[] verifiedStringArray = reportProperty.getVerified().split(",");
			Integer[] verifiedArray = new Integer[verifiedStringArray.length];
			for (int i = 0; i < verifiedStringArray.length; i++) verifiedArray[i] = Integer.parseInt(verifiedStringArray[i]);
			reportProperty.setVerifiedArray(verifiedArray);
		} else {
			reportProperty.setVerifiedArray(new Integer[0]);
		}
		
		if (reportProperty.getVaCurrent() != null) {
			String[] vaCurrentStringArray = reportProperty.getVaCurrent().split(",");
			Integer[] vaCurrentArray = new Integer[vaCurrentStringArray.length];
			for (int i = 0; i < vaCurrentStringArray.length; i++) vaCurrentArray[i] = Integer.parseInt(vaCurrentStringArray[i]);
			reportProperty.setVaCurrentArray(vaCurrentArray);
		} else {
			reportProperty.setVaCurrentArray(new Integer[0]);
		}
		
		if (reportProperty.getReviewed() != null) {
			String[] reviewedStringArray = reportProperty.getReviewed().split(",");
			Integer[] reviewedArray = new Integer[reviewedStringArray.length];
			for (int i = 0; i < reviewedStringArray.length; i++) reviewedArray[i] = Integer.parseInt(reviewedStringArray[i]);
			reportProperty.setReviewedArray(reviewedArray);
		} else {
			reportProperty.setReviewedArray(new Integer[0]);
		}
		
		if (reportProperty.getDob() != null) {
			String[] dob = sdf.format(reportProperty.getDob()).split("/");
			reportProperty.setMonth(new Integer(dob[0]));
			reportProperty.setDay(new Integer(dob[1]));
			reportProperty.setYear(new Integer(dob[2]));			
		}
		
		if (reportProperty.getServicePeriodFrom() != null) {
			String[] startDate = sdf.format(reportProperty.getServicePeriodFrom()).split("/");
			reportProperty.setMonthFrom(new Integer(startDate[0]));
			reportProperty.setDayFrom(new Integer(startDate[1]));
			reportProperty.setYearFrom(new Integer(startDate[2]));
		}
		
		if (reportProperty.getServicePeriodTo() != null) {
			String[] endDate = sdf.format(reportProperty.getServicePeriodTo()).split("/");
			reportProperty.setMonthTo(new Integer(endDate[0]));
			reportProperty.setDayTo(new Integer(endDate[1]));
			reportProperty.setYearTo(new Integer(endDate[2]));
		}
		
		if (reportProperty.getPrimaryPhone() != null && reportProperty.getPrimaryPhone().length() == 12) {
			String[] primaryPhone = reportProperty.getPrimaryPhone().split("-");
			reportProperty.setPrimaryPhone1(primaryPhone[0]);
			reportProperty.setPrimaryPhone2(primaryPhone[1]);
			reportProperty.setPrimaryPhone3(primaryPhone[2]);
		} else {
			reportProperty.setPrimaryPhone("");
			reportProperty.setPrimaryPhone1("");
			reportProperty.setPrimaryPhone2("");
			reportProperty.setPrimaryPhone3("");
		}
		
		if (reportProperty.getAltPhone() != null && reportProperty.getAltPhone().length() == 12) {
			String[] altPhone = reportProperty.getAltPhone().split("-");
			reportProperty.setAltPhone1(altPhone[0]);
			reportProperty.setAltPhone2(altPhone[1]);
			reportProperty.setAltPhone3(altPhone[2]);
		} else {
			reportProperty.setAltPhone("");
			reportProperty.setAltPhone1("");
			reportProperty.setAltPhone2("");
			reportProperty.setAltPhone3("");
		}
		
		if (reportProperty.getSsn() != null && reportProperty.getSsn().length() > 1) {
			String[] ssn = reportProperty.getSsn().split("-");
			if (ssn.length == 1) reportProperty.setSsn1(ssn[0]);
			else reportProperty.setSsn1("");
			if (ssn.length == 2) reportProperty.setSsn2(ssn[1]);
			else reportProperty.setSsn2("");
			if (ssn.length == 3) reportProperty.setSsn3(ssn[2]);
			else reportProperty.setSsn3("");
		} else {
			reportProperty.setSsn("");
			reportProperty.setSsn1("");
			reportProperty.setSsn2("");
			reportProperty.setSsn3("");
		}
		
		reportProperty.setAddress1(Util.convertNullToBlank(reportProperty.getAddress1()));
		reportProperty.setState(Util.convertNullToBlank(reportProperty.getState()));
		reportProperty.setLastName(Util.convertNullToBlank(reportProperty.getLastName()));
		reportProperty.setFirstName(Util.convertNullToBlank(reportProperty.getFirstName()));
		reportProperty.setCity(Util.convertNullToBlank(reportProperty.getCity()));
		reportProperty.setZip(Util.convertNullToBlank(reportProperty.getZip()));
		reportProperty.setEmail(Util.convertNullToBlank(reportProperty.getEmail()));
		reportProperty.setGender(Util.convertNullToBlank(reportProperty.getGender()));
		reportProperty.setRural(Util.convertNullToBlank(reportProperty.getRural()));
	}
	
	private void doCsv() throws Exception {
		StringBuffer sb = new StringBuffer("SSN,Name,Gender,Ethinicity,Street Address,Mailing Address,Phone,Email,DOB,Alt Contact Name,Alt Contact Address,Alt Contact Phone,Relationship to Veteran,Decease,Disability Percentage,Source," +
				"Medal 1,Medal 2,Medal 3,Medal 4," +
				"Branch Name 1,Begin 1,End 1,Zone 1,Discharge Type 1," + 
				"Branch Name 2,Begin 2,End 2,Zone 2,Discharge Type 2," +
				"Branch Name 3,Begin 3,End 3,Zone 3,Discharge Type 3," +
				"Branch Name 4,Begin 4,End 4,Zone 4,Discharge Type 4," +
				"Branch Name 5,Begin 5,End 5,Zone 5,Discharge Type 5," +
				"Branch Name 6,Begin 6,End 6,Zone 6,Discharge Type 6," +
				"Branch Name 7,Begin 7,End 7,Zone 7,Discharge Type 7," +
				"Branch Name 8,Begin 8,End 8,Zone 8,Discharge Type 8," +
				"Branch Name 9,Begin 9,End 9,Zone 9,Discharge Type 9," +
				"Branch Name 10,Begin 10,End 10,Zone 10,Discharge Type 10," +
				"Benefit Type 1,Beneficiary name 1,Beneficiary Address 1,Beneficiary Phone 1,Beneficiary Relation 1," +
				"Benefit Type 2,Beneficiary name 2,Beneficiary Address 2,Beneficiary Phone 2,Beneficiary Relation 2," +
				"Benefit Type 3,Beneficiary name 3,Beneficiary Address 3,Beneficiary Phone 3,Beneficiary Relation 3," +
				"Benefit Type 4,Beneficiary name 4,Beneficiary Address 4,Beneficiary Phone 4,Beneficiary Relation 4," +
				"Benefit Type 5,Beneficiary name 5,Beneficiary Address 5,Beneficiary Phone 5,Beneficiary Relation 5," +
				"Benefit Type 6,Beneficiary name 6,Beneficiary Address 6,Beneficiary Phone 6,Beneficiary Relation 6," +
				"Benefit Type 7,Beneficiary name 7,Beneficiary Address 7,Beneficiary Phone 7,Beneficiary Relation 7," +
				"Benefit Type 8,Beneficiary name 8,Beneficiary Address 8,Beneficiary Phone 8,Beneficiary Relation 8," +
				"Benefit Type 9,Beneficiary name 9,Beneficiary Address 9,Beneficiary Phone 9,Beneficiary Relation 9," +
				"Benefit Type 10,Beneficiary name 10,Beneficiary Address 10,Beneficiary Phone 10,Beneficiary Relation 10" +
				"\n");
		for (Veteran v : veterans) {
			// find/set benefit recipients
			for (BenefitType b : v.getBenefitTypeList()) {
				VeteranBenefit veteranBenefit = service.findVeteranBenefitType(v.getId(), b.getId());
				b.setBenefitRecipients(service.findVeteranBenefitRecipients(veteranBenefit.getId()));
			}
			
			sb.append(v.getSsn() + ",")
			.append(ReportUtil.concat2String(v.getFirstName(), v.getLastName()) + ",")
			.append(ReportUtil.convertNullToBlank(v.getGender()) + ",")
			.append((v.getEthnicity() == null) ? "," : v.getEthnicity().getName() + ",")
			.append((v.getAddress1() != null || v.getAddress2() != null) ? 
				"\"" + ReportUtil.concat2String(v.getAddress1(), v.getAddress2()) + ", " + v.getCity() + ", " + v.getState() + " " + v.getZip() + "\",":
				",")
			.append((v.getMailingAddr1() != null || v.getMailingAddr2() != null) ? 
				"\"" + ReportUtil.concat2String(v.getMailingAddr1(), v.getMailingAddr2()) + ", " + v.getMailingCity() + ", " + v.getMailingState() + " " + v.getMailingZip() + "\",":
				",")
			.append(ReportUtil.convertNullToBlank(v.getPrimaryPhone()) + ",")
			.append(ReportUtil.convertNullToBlank(v.getEmail()) + ",")
			.append(ReportUtil.formatDate(v.getDateOfBirth()) + ",")
			.append(ReportUtil.concat2String(v.getAltContactFirstName(), v.getAltContactLastName()) + ",")
			.append((v.getAltContactAddr1() != null || v.getAltContactAddr2() != null) ? 
				"\"" + ReportUtil.concat2String(v.getAltContactAddr1(), v.getAltContactAddr2()) + ", " + v.getAltContactCity() + ", " + v.getAltContactState() + " " + v.getAltContactZip() + "\",":
				",")
			.append(ReportUtil.convertNullToBlank(v.getAltContactPhone()) + ",")
			.append((v.getRelation() == null) ? "," : v.getRelation().getName() + ",")
			.append(ReportUtil.formatDate(v.getDateOfDeath()) + ",")
			.append(v.getPercentDisability() + ",")
			.append(v.getRecordType().getName() + ",");
			
			// decoration/medal max of 4
			if (v.getDecorationMedalList() != null) {
				int i;
				for (i = 0; i < v.getDecorationMedalList().size() && i <= 3; i++) {
					DecorationMedal d = v.getDecorationMedalList().get(i);
					sb.append(d.getName() + ",");
				}
				if (i != 4) {
					for (int j = i; j < 4; j++) {
						sb.append(",");
					}
				}
			}
			
			// service period max of 10
			if (v.getServicePeriodList() != null && v.getServicePeriodList().size() > 0) {
				int i;
				for (i = 0; i < v.getServicePeriodList().size() && i < 11; i++) {
					VeteranServicePeriod s = v.getServicePeriodList().get(i);
					sb.append(s.getServiceBranch().getName() + ",")
					.append(ReportUtil.formatDate(s.getStartDate()) + ",")
					.append(ReportUtil.formatDate(s.getEndDate()) + ",")
					.append((s.getCombatZone() == null) ? "," : s.getCombatZone().getName() + ",")
					.append((s.getDischargeType() == null) ? "," : s.getDischargeType().getName() + ",");
				}
				if (i != 10) {
					for (int j = i; j < 10; j++) {
						sb.append(",");
					}
				}
			}
			
			// benefit type max of 10 with one recipient
			if (v.getBenefitTypeList() != null) {
				int i;
				for (i = 0; i < v.getBenefitTypeList().size() && i < 11; i++) {
					BenefitType b = v.getBenefitTypeList().get(i);
					sb.append(b.getName() + ",");
					if (b.getBenefitRecipients() != null && b.getBenefitRecipients().size() > 0) {
						BenefitRecipient r = b.getBenefitRecipients().get(0);
						sb.append(ReportUtil.concat2String(r.getFirstName(), r.getLastName()) + ",")
						.append((r.getAddress1() != null || r.getAddress2() != null) ? 
							"\"" + ReportUtil.concat2String(r.getAddress1(), r.getAddress2()) + ", " + r.getCity() + ", " + r.getState() + " " + r.getZip() + "\",":
							",")
						.append(ReportUtil.convertNullToBlank(r.getPrimaryPhone()) + ",")
						.append((r.getRelation() == null) ? "," : r.getRelation().getName() + ",");
					}
				}
				if (i != 10) {
					for (int j = i; j < 10; j++) {
						sb.append(",");
					}
				}
			}
			
			sb.append("\n");
		}
		
		csvOut(sb.toString());
	}
}
