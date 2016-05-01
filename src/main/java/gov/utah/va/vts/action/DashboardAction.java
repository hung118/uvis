package gov.utah.va.vts.action;

import java.util.List;

import gov.utah.va.vts.model.Dashboard;
import gov.utah.va.vts.service.VtsService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Dashboard action class.
 * @author HNGUYEN
 *
 */
public class DashboardAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;

	@Autowired
	VtsService service;
	
	private String pageSize;
	
	public String execute() throws Exception {		
		return SUCCESS;
	}
	
	public String page() throws Exception {
		return SUCCESS;
	}	

	public String setSessionPageSize() throws Exception {
		
		logger.debug("entering setSessionPageSize ...");
		
		String pageSizeStr = request.getParameter(getText("PAGESIZE_SES"));
		if ("all".equals(pageSizeStr)) {
			request.getSession().setAttribute(getText("PAGESIZE_SES"), "");
		} else {
			request.getSession().setAttribute(getText("PAGESIZE_SES"), pageSizeStr);
		}
		
		return SUCCESS;
	}
	
	public List<Dashboard> getVeterans() {
		return service.findVeterans_unreviewed();
	}
	
	public Integer getTotalUnreviewedRecords() {
		return service.findVeterans_unreviewed_total();
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
	
}
