package gov.utah.va.vts.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.utah.dts.det.util.NameUniqueException;
import gov.utah.dts.det.util.Util;
import gov.utah.dts.det.util.Validate;
import gov.utah.va.vts.model.ServiceBranch;

/**
 * ServiceBranch action class.
 * 
 * @author HNGUYEN
 *
 */
public class ServiceBranchAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	private ServiceBranch serviceBranch;
	
	private List<ServiceBranch> serviceBranches = new ArrayList<ServiceBranch>();
	
	public String display() throws Exception {
		
		logger.debug("entering display() ...");
		
		serviceBranch = new ServiceBranch();
		serviceBranch.setActive(new Integer(1));
		
		return INPUT;
	}
	
	public String save() throws Exception {
		try {
			// persists serviceBranch 
			if (serviceBranch.getId() == null) {	// insert
				serviceBranch.setInsertTimestamp(new Date());
				serviceBranch.setUpdateTimestamp(new Date());
			} else {	// update
				serviceBranch.setUpdateTimestamp(new Date());
			}
			
			service.saveServiceBranch(serviceBranch);
			
			serviceBranches = service.findAllServiceBranches();
			
			addActionMessage(getText("message.save", Util.getStringArray(getText("serviceBranch"))));
			serviceBranch.setOp("");
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				throw new NameUniqueException("Name '" + serviceBranch.getName() + "' already exists.");	// rm 28194
			} else {
				throw e;
			}
		}
		
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		
		logger.debug("entering edit...");
		
		Long id = new Long(request.getParameter("id"));
		serviceBranch = service.findServiceBranchById(id);
		serviceBranch.setOp("edit");
						
		return SUCCESS;
	}
	
	public String delete() throws Exception {
		
		logger.debug("entering delete ...");
		
		Long id = new Long(request.getParameter("id"));
		serviceBranch = new ServiceBranch();
		serviceBranch.setId(id);
		
		try {
			service.deleteServiceBranch(serviceBranch);
		} catch (Exception e) {	// rm 31662
			addActionError(getText("error.cannotDelete"));
			serviceBranches = service.findAllServiceBranches();
			return INPUT;
		}
		
		serviceBranches = service.findAllServiceBranches();
		addActionMessage(getText("message.delete", Util.getStringArray(getText("serviceBranch"))));
		serviceBranch.setOp("");
		return SUCCESS;
	}
	
	public void validate() {
		
		if (request.getParameter("save") != null) {	// validate form only Save button is clicked
			if (Validate.isEmpty(serviceBranch.getName())) addActionError(getText("error.required", Util.getStringArray(getText("common.name"))));
			if (Validate.isNull(serviceBranch.getActive())) addActionError(getText("error.required", Util.getStringArray(getText("common.status"))));
			
			// redmine 28194 31662
			if ("insert".equals(serviceBranch.getOp())) {	// check for insert mode only
				ServiceBranch entity = service.findServiceBranchByName(serviceBranch.getName());
				if (entity != null) {
					addActionError(getText("error.duplicateName", Util.getStringArray("Name")));
				}	
			}
		}
	}

	public void setServiceBranch(ServiceBranch serviceBranch) {
		this.serviceBranch = serviceBranch;
	}

	public ServiceBranch getServiceBranch() {
		return serviceBranch;
	}
	
	public void setServiceBranches(List<ServiceBranch> serviceBranches) {
		this.serviceBranches = serviceBranches;
	}

	public List<ServiceBranch> getServiceBranches() {
		serviceBranches = service.findAllServiceBranches();
		return serviceBranches;
	}
	
}
