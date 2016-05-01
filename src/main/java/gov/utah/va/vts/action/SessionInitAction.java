package gov.utah.va.vts.action;

import org.springframework.beans.factory.annotation.Autowired;

import gov.utah.dts.umd.SecurityBreachException;
import gov.utah.va.vts.model.User;
import gov.utah.va.vts.service.VtsService;

/**
 * Application entry.
 * 
 * @author HNGUYEN
 *
 */
public class SessionInitAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;

	@Autowired
	VtsService service;
	
	public String execute() throws Exception {
		
		logger.debug("Entering execute ...");
		
		if (request.getSession().getAttribute(getText("USER")) == null) {
			String email = (String)request.getHeader("email");
			
			if (email == null) {
				if ("localhost".equals(getText("test_or_prod"))) {
					email = request.getUserPrincipal().getName();
				} else {
					throw new SecurityBreachException("UMD login is required!");
				}
			}
			
			User user = service.findActiveUserByEmail(email);
			if (user == null) {
				return "inactive";
			} else {
				request.getSession().setAttribute(getText("USER"), user);
			}
		}
		
		return SUCCESS;
	}
	
	public Integer getTotalUnreviewedRecords() {
		return service.findVeterans_unreviewed_total();
	}
	
}
