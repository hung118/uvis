package gov.utah.va.vts.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.utah.dts.det.util.NameUniqueException;
import gov.utah.dts.det.util.Util;
import gov.utah.dts.det.util.Validate;
import gov.utah.va.vts.model.DecorationMedal;

/**
 * DecorationMedal action class.
 * 
 * @author HNGUYEN
 *
 */
public class DecorationMedalAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	private DecorationMedal decorationMedal;
	
	private List<DecorationMedal> decorationMedals = new ArrayList<DecorationMedal>();
	
	public String display() throws Exception {
		
		logger.debug("entering display() ...");
		
		decorationMedal = new DecorationMedal();
		decorationMedal.setActive(new Integer(1));
		
		return INPUT;
	}
	
	public String save() throws Exception {
		try {
			// persists decorationMedal 
			if (decorationMedal.getId() == null) {	// insert
				decorationMedal.setInsertTimestamp(new Date());
				decorationMedal.setUpdateTimestamp(new Date());
			} else {	// update
				decorationMedal.setUpdateTimestamp(new Date());
			}
			
			service.saveDecorationMedal(decorationMedal);
			
			decorationMedals = service.findAllDecorationMedals();
			
			addActionMessage(getText("message.save", Util.getStringArray(getText("decorationMedal"))));
			decorationMedal.setOp("");
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				throw new NameUniqueException("Name '" + decorationMedal.getName() + "' already exists.");	// rm 28194
			} else {
				throw e;
			}
		}
		
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		
		logger.debug("entering edit...");
		
		Long id = new Long(request.getParameter("id"));
		decorationMedal = service.findDecorationMedalById(id);
		decorationMedal.setOp("edit");
						
		return SUCCESS;
	}
	
	public String delete() throws Exception {
		
		logger.debug("entering delete ...");
		
		Long id = new Long(request.getParameter("id"));
		decorationMedal = new DecorationMedal();
		decorationMedal.setId(id);
		
		try {
			service.deleteDecorationMedal(decorationMedal);
		} catch (Exception e) {	// rm 31662
			addActionError(getText("error.cannotDelete"));
			decorationMedals = service.findAllDecorationMedals();
			return INPUT;
		}
		
		decorationMedals = service.findAllDecorationMedals();
		addActionMessage(getText("message.delete", Util.getStringArray(getText("decorationMedal"))));
		decorationMedal.setOp("");
		return SUCCESS;
	}
	
	public void validate() {
		
		if (request.getParameter("save") != null) {	// validate form only Save button is clicked
			if (Validate.isEmpty(decorationMedal.getName())) addActionError(getText("error.required", Util.getStringArray(getText("common.name"))));
			if (Validate.isNull(decorationMedal.getActive())) addActionError(getText("error.required", Util.getStringArray(getText("common.status"))));
			
			// redmine 28194 31662
			if ("insert".equals(decorationMedal.getOp())) {	// check for insert mode only
				DecorationMedal entity = service.findDecorationMedalByName(decorationMedal.getName());
				if (entity != null) {
					addActionError(getText("error.duplicateName", Util.getStringArray("Name")));
				}				
			}
		}
	}

	public void setDecorationMedal(DecorationMedal decorationMedal) {
		this.decorationMedal = decorationMedal;
	}

	public DecorationMedal getDecorationMedal() {
		return decorationMedal;
	}
	
	public void setDecorationMedals(List<DecorationMedal> decorationMedals) {
		this.decorationMedals = decorationMedals;
	}

	public List<DecorationMedal> getDecorationMedals() {
		decorationMedals = service.findAllDecorationMedals();
		return decorationMedals;
	}
	
}
