package gov.utah.va.vts.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.utah.dts.det.util.Util;
import gov.utah.dts.det.util.Validate;
import gov.utah.va.vts.model.Ethnicity;

/**
 * Ethnicity action class.
 * 
 * @author HNGUYEN
 *
 */
public class EthnicityAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;

	private Ethnicity ethnicity;
	
	private List<Ethnicity> ethnicities = new ArrayList<Ethnicity>();
	
	public String display() throws Exception {
		
		logger.debug("entering display() ...");
		
		ethnicity = new Ethnicity();
		ethnicity.setActive(new Integer(1));
		
		return INPUT;
	}
	
	public String save() throws Exception {
		
		logger.debug("entering save()...");
			
		// persists ethnicity 
		if (ethnicity.getId() == null) {	// insert
			ethnicity.setInsertTimestamp(new Date());
			ethnicity.setUpdateTimestamp(new Date());
		} else {	// update
			ethnicity.setUpdateTimestamp(new Date());
		}
		
		service.saveEthnicity(ethnicity);
		
		ethnicities = service.findAllEthnicities();
		
		addActionMessage(getText("message.save", Util.getStringArray(getText("ethnicity"))));
		ethnicity.setOp("");
		
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		
		logger.debug("entering edit...");
		
		Long id = new Long(request.getParameter("id"));
		ethnicity = service.findEthnicityById(id);
		ethnicity.setOp("edit");
		
		return SUCCESS;
	}
	
	public String delete() throws Exception {
		
		logger.debug("entering delete ...");
		
		Long id = new Long(request.getParameter("id"));
		ethnicity = new Ethnicity();
		ethnicity.setId(id);
		
		try {
			service.deleteEthnicity(ethnicity);
		} catch (Exception e) {	// rm 31662
			addActionError(getText("error.cannotDelete"));
			ethnicities = service.findAllEthnicities();
			return INPUT;
		}
		
		ethnicities = service.findAllEthnicities();
		addActionMessage(getText("message.delete", Util.getStringArray(getText("ethnicity"))));
		ethnicity.setOp("");		
		return SUCCESS;
	}
	
	public void validate() {
		
		if (request.getParameter("save") != null) {	// validate form only Save button is clicked
			if (Validate.isEmpty(ethnicity.getName())) addActionError(getText("error.required", Util.getStringArray(getText("common.name"))));
			if (Validate.isNull(ethnicity.getActive())) addActionError(getText("error.required", Util.getStringArray(getText("common.status"))));
			
			// redmine 28194 31662
			if ("insert".equals(ethnicity.getOp())) {	// check for insert mode only
				Ethnicity entity = service.findEthnicityByName(ethnicity.getName());
				if (entity != null) {
					addActionError(getText("error.duplicateName", Util.getStringArray("Name")));
				}				
			}
		}
	}

	public void setEthnicity(Ethnicity ethnicity) {
		this.ethnicity = ethnicity;
	}

	public Ethnicity getEthnicity() {
		return ethnicity;
	}
	
	public void setEthnicities(List<Ethnicity> ethnicities) {
		this.ethnicities = ethnicities;
	}

	public List<Ethnicity> getEthnicities() {
		ethnicities = service.findAllEthnicities();
		return ethnicities;
	}
	
}
