package gov.utah.va.vts.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.utah.dts.det.util.NameUniqueException;
import gov.utah.dts.det.util.Util;
import gov.utah.dts.det.util.Validate;
import gov.utah.va.vts.model.BenefitType;

/**
 * benefitType action class.
 * 
 * @author HNGUYEN
 *
 */
public class BenefitTypeAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	private BenefitType benefitType;
	
	private List<BenefitType> benefitTypes = new ArrayList<BenefitType>();
	
	public String display() throws Exception {
		
		logger.debug("entering display() ...");
		
		benefitType = new BenefitType();
		benefitType.setActive(new Integer(1));
		
		return INPUT;
	}
	
	public String save() throws Exception {
		try {
			// persists benefitType 
			if (benefitType.getId() == null) {	// insert
				benefitType.setInsertTimestamp(new Date());
				benefitType.setUpdateTimestamp(new Date());
			} else {	// update
				benefitType.setUpdateTimestamp(new Date());
			}
			
			service.saveBenefitType(benefitType);
			
			benefitTypes = service.findAllBenefitTypes();
			
			addActionMessage(getText("message.save", Util.getStringArray(getText("benefitType"))));
			benefitType.setOp("");
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				throw new NameUniqueException("Name '" + benefitType.getName() + "' already exists.");	// rm 28194
			} else {
				throw e;
			}
		}
		logger.debug("entering save()...");
			

		
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		
		logger.debug("entering edit...");
		
		Long id = new Long(request.getParameter("id"));
		benefitType = service.findBenefitTypeById(id);
		benefitType.setOp("edit");
						
		return SUCCESS;
	}
	
	public String delete() throws Exception {
		
		logger.debug("entering delete ...");
		
		Long id = new Long(request.getParameter("id"));
		benefitType = new BenefitType();
		benefitType.setId(id);
		
		try {
			service.deleteBenefitType(benefitType);
		} catch (Exception e) {	// rm 31662
			addActionError(getText("error.cannotDelete"));
			benefitTypes = service.findAllBenefitTypes();
			return INPUT;
		}
		
		benefitTypes = service.findAllBenefitTypes();
		addActionMessage(getText("message.delete", Util.getStringArray(getText("benefitType"))));
		benefitType.setOp("");
		return SUCCESS;
	}
	
	public void validate() {
		
		if (request.getParameter("save") != null) {	// validate form only Save button is clicked
			if (Validate.isEmpty(benefitType.getName())) addActionError(getText("error.required", Util.getStringArray(getText("common.name"))));
			if (Validate.isNull(benefitType.getActive())) addActionError(getText("error.required", Util.getStringArray(getText("common.status"))));
			
			// redmine 28194 31662
			if ("insert".equals(benefitType.getOp())) {	// check for insert mode only
				BenefitType entity = service.findBenefitTypeByName(benefitType.getName());
				if (entity != null) {
					addActionError(getText("error.duplicateName", Util.getStringArray("Name")));
				}				
			}
		}
	}

	public void setBenefitType(BenefitType benefitType) {
		this.benefitType = benefitType;
	}

	public BenefitType getBenefitType() {
		return benefitType;
	}
	
	public void setBenefitTypes(List<BenefitType> benefitTypes) {
		this.benefitTypes = benefitTypes;
	}

	public List<BenefitType> getBenefitTypes() {
		benefitTypes = service.findAllBenefitTypes();
		return benefitTypes;
	}
	
}
