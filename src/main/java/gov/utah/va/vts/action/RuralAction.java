package gov.utah.va.vts.action;

import java.util.ArrayList;
import java.util.List;

import gov.utah.dts.det.util.NameUniqueException;
import gov.utah.dts.det.util.Util;
import gov.utah.dts.det.util.Validate;
import gov.utah.va.vts.model.NameValue;

/**
 * Rural crosswalk action class.
 * 
 * @author HNGUYEN
 *
 */
public class RuralAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
		
	private NameValue rural;
	private List<NameValue> rurals = new ArrayList<NameValue>();
	
	public String display() throws Exception {
		
		logger.debug("entering display() ...");
		rural = new NameValue();
		
		return INPUT;
	}
	
	public String save() throws Exception {
		
		logger.debug("entering save()...");
		
		// persists rural	
		String op = service.saveRural(rural);
		if (!op.equals(rural.getOp())) {
			throw new NameUniqueException("Zip code '" + rural.getName() + "' already exists.");	// rm 28194
		}
				
		addActionMessage(getText("message.save", Util.getStringArray(getText("rural"))));
		rural.setOp("");
		return SUCCESS;
	}
	
	public String search() throws Exception {
		
		logger.debug("entering search...");
		
		rurals = service.findRurals(rural);
		
		// save search on session for later use
		request.getSession().setAttribute(getText("SEARCH_OBJ_SES"), rural);
				
		return SUCCESS;
	}
	
	public String page() throws Exception {
		
		logger.debug("entering pageNavigate...");
		
		NameValue ruralSearch = (NameValue)request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
		rurals = service.findRurals(ruralSearch);
		
		rural = null;
		
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		
		logger.debug("entering edit...");
		
		rural = new NameValue();
		rural.setName(request.getParameter("zipCode"));
		rural.setValue(request.getParameter("designation"));
				
		rural.setOp("edit");
		return SUCCESS;
	}
	
	public String delete() throws Exception {
		
		logger.debug("entering delete ...");
		
		NameValue nvSearch = (NameValue)request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
		try {
			service.deleteRural(request.getParameter("zipCode"));
		} catch (Exception e) {	// rm 31662
			addActionError(getText("error.cannotDelete"));
			rurals = service.findRurals(nvSearch);
			return INPUT;
		}
		
		rurals = service.findRurals(nvSearch);		
		addActionMessage(getText("message.delete", Util.getStringArray(getText("Zip Code Designation"))));
		rural = new NameValue();
		rural.setOp("");
		return SUCCESS;
	}
	
	public void validate() {
		if (request.getParameter("save") != null) {	// validate form only Save button is clicked
			if (Validate.isEmpty(rural.getName())) {
				addActionError(getText("error.required", Util.getStringArray(getText("rural.zipCode"))));
			} else {
				if (!rural.getName().matches("[0-9]{5}")) {
					addActionError(getText("error.numberOnly", Util.getStringArray(getText("rural.zipCode"))));
				}
			}
			if (Validate.isEmpty(rural.getValue())) addActionError(getText("error.required", Util.getStringArray(getText("rural.designation"))));
			
			// redmine 28194 31662
			if ("insert".equals(rural.getOp())) {	// check for insert mode only
				NameValue entity = service.findRuralByName(rural.getName());
				if (entity != null) {
					addActionError(getText("error.duplicateName", Util.getStringArray("Zip Code")));
				}				
			}
		}
	}

	public NameValue getRural() {
		return rural;
	}

	public void setRural(NameValue rural) {
		this.rural = rural;
	}

	public List<NameValue> getRurals() {
		return rurals;
	}

	public void setRurals(List<NameValue> rurals) {
		this.rurals = rurals;
	}

}
