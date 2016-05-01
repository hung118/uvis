package gov.utah.va.vts.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.utah.dts.det.util.NameUniqueException;
import gov.utah.dts.det.util.Util;
import gov.utah.dts.det.util.Validate;
import gov.utah.va.vts.model.DischargeType;

/**
 * dischargeType action class.
 * 
 * @author HNGUYEN
 *
 */
public class DischargeTypeAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;

	private DischargeType dischargeType;
	
	private List<DischargeType> dischargeTypes = new ArrayList<DischargeType>();
	
	public String display() throws Exception {
		
		logger.debug("entering display() ...");
		
		dischargeType = new DischargeType();
		dischargeType.setActive(new Integer(1));
		
		return INPUT;
	}
	
	public String save() throws Exception {
		try {
			// persists dischargeType 
			if (dischargeType.getId() == null) {	// insert
				dischargeType.setInsertTimestamp(new Date());
				dischargeType.setUpdateTimestamp(new Date());
			} else {	// update
				dischargeType.setUpdateTimestamp(new Date());
			}
			
			service.saveDischargeType(dischargeType);
			
			dischargeTypes = service.findAllDischargeTypes();
			
			addActionMessage(getText("message.save", Util.getStringArray(getText("dischargeType"))));
			dischargeType.setOp("");
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				throw new NameUniqueException("Name '" + dischargeType.getName() + "' already exists.");	// rm 28194
			} else {
				throw e;
			}
		}
		
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		
		logger.debug("entering edit...");
		
		Long id = new Long(request.getParameter("id"));
		dischargeType = service.findDischargeTypeById(id);
		dischargeType.setOp("edit");
						
		return SUCCESS;
	}
	
	public String delete() throws Exception {
		
		logger.debug("entering delete ...");
		
		Long id = new Long(request.getParameter("id"));
		dischargeType = new DischargeType();
		dischargeType.setId(id);
		
		try {
			service.deleteDischargeType(dischargeType);
		} catch (Exception e) {	// rm 31662
			addActionError(getText("error.cannotDelete"));
			dischargeTypes = service.findAllDischargeTypes();	
			return INPUT;
		}
		
		dischargeTypes = service.findAllDischargeTypes();		
		addActionMessage(getText("message.delete", Util.getStringArray(getText("dischargeType"))));
		dischargeType.setOp("");
		return SUCCESS;
	}
	
	public void validate() {
		
		if (request.getParameter("save") != null) {	// validate form only Save button is clicked
			if (Validate.isEmpty(dischargeType.getName())) addActionError(getText("error.required", Util.getStringArray(getText("common.name"))));
			if (Validate.isNull(dischargeType.getActive())) addActionError(getText("error.required", Util.getStringArray(getText("common.status"))));
			
			// redmine 28194 31662
			if ("insert".equals(dischargeType.getOp())) {	// check for insert mode only
				DischargeType entity = service.findDischargeTypeByName(dischargeType.getName());
				if (entity != null) {
					addActionError(getText("error.duplicateName", Util.getStringArray("Name")));
				}	
			}
		}
	}

	public void setDischargeType(DischargeType dischargeType) {
		this.dischargeType = dischargeType;
	}

	public DischargeType getDischargeType() {
		return dischargeType;
	}
	
	public void setDischargeTypes(List<DischargeType> dischargeTypes) {
		this.dischargeTypes = dischargeTypes;
	}

	public List<DischargeType> getDischargeTypes() {
		dischargeTypes = service.findAllDischargeTypes();
		return dischargeTypes;
	}
	
}
