package gov.utah.va.vts.action;

import gov.utah.dts.det.util.Util;
import gov.utah.va.vts.model.DataImport;
import gov.utah.va.vts.model.Veteran;
import gov.utah.va.vts.service.VtsService;

import java.io.File;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Base class for handling all data import actions.
 * @author HNGUYEN
 *
 */
public class DataImportAction extends ActionSupport implements ServletContextAware, ServletRequestAware {

	private static final long serialVersionUID = 1L;

	@Autowired
	protected VtsService service;
	
	protected ServletContext servletContext;
	protected HttpServletRequest request;

	protected Log logger = org.apache.commons.logging.LogFactory.getLog(this.getClass());
	
	protected File csvFile;
	protected String csvFileContentType;
	protected String csvFileFileName;
	
	protected DataImport dataImport;
		
	/**
	 * Struts 2 file upload sample - un-used, for Struts 2 file upload demo only.
	 */
	public String execute() throws Exception {
		
		logger.debug("entering execute ...");
		
		if (this.csvFile == null) {
			return INPUT;
		}
		
		String filePath = servletContext.getRealPath("/");
		
		logger.debug("Server path:" + filePath);
		File fileToCreate = new File(filePath, this.csvFileFileName);
		
		FileUtils.copyFile(this.csvFile, fileToCreate);
		
		FileUtils.readLines(this.csvFile);
		
		return SUCCESS;
	}
	
	public String page() throws Exception {
		
		dataImport = null;
		return SUCCESS;
	}
	
	@Override
	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public File getCsvFile() {
		return csvFile;
	}

	public void setCsvFile(File csvFile) {
		this.csvFile = csvFile;
	}

	public String getCsvFileContentType() {
		return csvFileContentType;
	}

	public void setCsvFileContentType(String csvFileContentType) {
		this.csvFileContentType = csvFileContentType;
	}

	public String getCsvFileFileName() {
		return csvFileFileName;
	}

	public void setCsvFileFileName(String csvFileFileName) {
		this.csvFileFileName = csvFileFileName;
	}

	@SuppressWarnings("unchecked")
	public List<DataImport> getDataImports() {
		return (List<DataImport>)request.getSession().getAttribute(getText("DATA_LIST_SES"));
	}

	public void setDataImport(DataImport dataImport) {
		this.dataImport = dataImport;
	}
	
	public DataImport getDataImport() {
		return dataImport;
	}

	protected void viewRecord() throws Exception {
		dataImport = service.findDataImportById(Long.parseLong(request.getParameter("id")));
	}
	
	protected void deleteRecord(Long source) throws Exception {
		dataImport = new DataImport();
		dataImport.setId(Long.parseLong(request.getParameter("id")));
		service.deleteDataImport(dataImport);
		
		request.getSession().setAttribute(getText("DATA_LIST_SES"), service.findDataImportsBySource(source));
		dataImport = null;
	}
	
	protected boolean isMatched(Veteran v1, Veteran v2) {
				
		if (!Util.isStringEqual(v1.getLastName(), v2.getLastName())) {
			return false;
		} else if (!Util.isStringEqual(v1.getFirstName(), v2.getFirstName())) {
			return false;
		} else if (!(v1.getDateOfBirth() != null && v1.getDateOfBirth().equals(v2.getDateOfBirth()))) {
			return false;
		} else if (!Util.isStringEqual(v1.getSsn(), v2.getSsn())) {
			return false;
		} else if (!Util.isStringEqual(v1.getAddress1(), v2.getAddress1())) {
			return false;
		} else if (!Util.isStringEqual(v1.getCity(), v2.getCity())) {
			return false;
		} else if (!Util.isStringEqual(v1.getState(), v2.getState())) {
			return false;
		} else if (!Util.isStringEqual(v1.getZip(), v2.getZip())) {
			return false;
		} else if (!Util.isStringEqual(v1.getMailingAddr1(), v2.getMailingAddr1())) {
			return false;
		} else if (!Util.isStringEqual(v1.getMailingCity(), v2.getMailingCity())) {
			return false;
		} else if (!Util.isStringEqual(v1.getMailingState(), v2.getMailingState())) {
			return false;
		} else if (!Util.isStringEqual(v1.getMailingZip(), v2.getMailingZip())) {
			return false;
		} else if (!Util.isStringEqual(v1.getEmail(), v2.getEmail())) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * gets a string full of random numbers with a given digit count.
	 * @param i
	 * @return
	 */
	protected String getRandomNumber(int digitCount) {
		return RandomStringUtils.random(digitCount, "0123456789");	
	}
	
}
