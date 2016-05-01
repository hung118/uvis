package gov.utah.va.vts.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.utah.dts.det.util.NameUniqueException;
import gov.utah.dts.det.util.Util;
import gov.utah.dts.det.util.Validate;
import gov.utah.va.vts.model.CombatZone;

/**
 * CombatZone action class.
 * 
 * @author HNGUYEN
 *
 */
public class CombatZoneAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	private CombatZone combatZone;
	
	private List<CombatZone> combatZones = new ArrayList<CombatZone>();
	
	public String display() throws Exception {
		
		logger.debug("entering display() ...");
		
		combatZone = new CombatZone();
		combatZone.setActive(new Integer(1));
		
		return INPUT;
	}
	
	public String save() throws Exception {
		try {
			// persists CombatZone 
			if (combatZone.getId() == null) {	// insert
				combatZone.setInsertTimestamp(new Date());
				combatZone.setUpdateTimestamp(new Date());
			} else {	// update
				combatZone.setUpdateTimestamp(new Date());
			}
			
			service.saveCombatZone(combatZone);
			
			combatZones = service.findAllCombatZones();
			
			addActionMessage(getText("message.save", Util.getStringArray(getText("combatZone"))));
			combatZone.setOp("");
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				throw new NameUniqueException("Name '" + combatZone.getName() + "' already exists.");	// rm 28194
			} else {
				throw e;
			}
		}
		
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		
		logger.debug("entering edit...");
		
		Long id = new Long(request.getParameter("id"));
		combatZone = service.findCombatZoneById(id);
		combatZone.setOp("edit");
						
		return SUCCESS;
	}
	
	public String delete() throws Exception {
		
		logger.debug("entering delete ...");
		
		Long id = new Long(request.getParameter("id"));
		combatZone = new CombatZone();
		combatZone.setId(id);
		
		try {
			service.deleteCombatZone(combatZone);
		} catch (Exception e) {	// rm 31662
			addActionError(getText("error.cannotDelete"));
			combatZones = service.findAllCombatZones();
			return INPUT;
		}
		
		combatZones = service.findAllCombatZones();		
		addActionMessage(getText("message.delete", Util.getStringArray(getText("combatZone"))));
		combatZone.setOp("");
		return SUCCESS;
	}
	
	public void validate() {
		
		if (request.getParameter("save") != null) {	// validate form only Save button is clicked
			if (Validate.isEmpty(combatZone.getName())) addActionError(getText("error.required", Util.getStringArray(getText("common.name"))));
			if (Validate.isNull(combatZone.getActive())) addActionError(getText("error.required", Util.getStringArray(getText("common.status"))));
			
			// redmine 28194 31662
			if ("insert".equals(combatZone.getOp())) {	// check for insert mode only
				CombatZone entity = service.findCombatZoneByName(combatZone.getName());
				if (entity != null) {
					addActionError(getText("error.duplicateName", Util.getStringArray("Name")));
				}	
			}
		}
	}

	public void setCombatZone(CombatZone combatZone) {
		this.combatZone = combatZone;
	}

	public CombatZone getCombatZone() {
		return combatZone;
	}
	
	public void setCombatZones(List<CombatZone> combatZones) {
		this.combatZones = combatZones;
	}

	public List<CombatZone> getCombatZones() {
		combatZones = service.findAllCombatZones();
		return combatZones;
	}
	
}
