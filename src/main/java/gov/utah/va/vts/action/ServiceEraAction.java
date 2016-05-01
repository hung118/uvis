package gov.utah.va.vts.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.utah.dts.det.util.NameUniqueException;
import gov.utah.dts.det.util.Util;
import gov.utah.dts.det.util.Validate;
import gov.utah.va.vts.model.ServiceEra;

/**
 * ServiceEra action class.
 * 
 * @author HNGUYEN
 *
 */
public class ServiceEraAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	private ServiceEra serviceEra;
	
	private List<ServiceEra> serviceEras = new ArrayList<ServiceEra>();
	
	public String display() throws Exception {
		
		logger.debug("entering display() ...");
		
		serviceEra = new ServiceEra();
		serviceEra.setActive(new Integer(1));
		
		return INPUT;
	}
	
	public String save() throws Exception {
		try {
			// persists serviceEra 
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			serviceEra.setStartDate(sdf.parse(serviceEra.getStartDateStr()));
			serviceEra.setEndDate(sdf.parse(serviceEra.getEndDateStr()));
			if (serviceEra.getId() == null) {	// insert
				serviceEra.setInsertTimestamp(new Date());
				serviceEra.setUpdateTimestamp(new Date());
			} else {	// update
				serviceEra.setUpdateTimestamp(new Date());
			}
			
			service.saveServiceEra(serviceEra);
			
			serviceEras = service.findAllServiceEras();
			
			addActionMessage(getText("message.save", Util.getStringArray(getText("serviceEra"))));
			serviceEra.setOp("");			
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				throw new NameUniqueException("Name '" + serviceEra.getName() + "' already exists.");	// rm 28194
			} else {
				throw e;
			}
		}
		
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		
		logger.debug("entering edit...");
		
		Long id = new Long(request.getParameter("id"));
		serviceEra = service.findServiceEraById(id);
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		serviceEra.setStartDateStr(sdf.format(serviceEra.getStartDate()));
		serviceEra.setEndDateStr(sdf.format(serviceEra.getEndDate()));
		serviceEra.setOp("edit");
						
		return SUCCESS;
	}
	
	public String delete() throws Exception {
		
		logger.debug("entering delete ...");
		
		Long id = new Long(request.getParameter("id"));
		serviceEra = new ServiceEra();
		serviceEra.setId(id);
		
		try {
			service.deleteServiceEra(serviceEra);
		} catch (Exception e) {	// rm 31662
			addActionError(getText("error.cannotDelete"));
			serviceEras = service.findAllServiceEras();
			return INPUT;
		}
		
		serviceEras = service.findAllServiceEras();
		addActionMessage(getText("message.delete", Util.getStringArray(getText("serviceEra"))));
		serviceEra.setOp("");
		return SUCCESS;
	}
	
	public void validate() {
		
		if (request.getParameter("save") != null) {	// validate form only Save button is clicked
			if (Validate.isEmpty(serviceEra.getName())) addActionError(getText("error.required", Util.getStringArray(getText("common.name"))));
			if (Validate.isEmpty(serviceEra.getStartDateStr())) addActionError(getText("error.required", Util.getStringArray(getText("serviceEra.startDate"))));
			if (!Validate.isEmpty(serviceEra.getStartDateStr())) {
				if (!Validate.isDateValid(serviceEra.getStartDateStr(), "MM/dd/yyyy")) addActionError(getText("error.notValidDate", Util.getStringArray(getText("serviceEra.startDate"))));
			}
			if (Validate.isEmpty(serviceEra.getEndDateStr())) addActionError(getText("error.required", Util.getStringArray(getText("serviceEra.endDate"))));
			if (!Validate.isEmpty(serviceEra.getEndDateStr())) {
				if (!Validate.isDateValid(serviceEra.getEndDateStr(), "MM/dd/yyyy")) addActionError(getText("error.notValidDate", Util.getStringArray(getText("serviceEra.endDate"))));
			}
			if (Validate.isNull(serviceEra.getActive())) addActionError(getText("error.required", Util.getStringArray(getText("common.status"))));
			
			// redmine 28194 31662
			if ("insert".equals(serviceEra.getOp())) {	// check for insert mode only
				ServiceEra entity = service.findServiceEraByName(serviceEra.getName());
				if (entity != null) {
					addActionError(getText("error.duplicateName", Util.getStringArray("Name")));
				}	
			}
		}
	}

	public void setServiceEra(ServiceEra serviceEra) {
		this.serviceEra = serviceEra;
	}

	public ServiceEra getServiceEra() {
		return serviceEra;
	}
	
	public void setServiceEras(List<ServiceEra> serviceEras) {
		this.serviceEras = serviceEras;
	}

	public List<ServiceEra> getServiceEras() {
		serviceEras = service.findAllServiceEras();
		return serviceEras;
	}
	
}
