package gov.utah.va.vts.ajax;

import gov.utah.va.vts.model.BenefitRecipient;
import gov.utah.va.vts.model.BenefitType;
import gov.utah.va.vts.model.Veteran;
import gov.utah.va.vts.model.VeteranBenefit;
import gov.utah.va.vts.service.VtsService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Ajax class for calls from veteran.jsp, veteran benefit type section.
 * 
 * @author HNGUYEN
 *
 */
public class VeteranBenefitTypeAjax {
	protected Log logger = org.apache.commons.logging.LogFactory.getLog(this.getClass());
	
	@Autowired
	VtsService service;
	
	public List<VeteranBenefit> getVeteranBenefitTypes(Long veteranId) throws Exception {
		
		logger.debug("entering getVeteranBenefitTypes ...");		
		
		List<VeteranBenefit> veteranBenefitTypes = service.findVeteranBenefitTypes(veteranId); 
		for (VeteranBenefit veteranBenefit : veteranBenefitTypes) {	
			if (veteranBenefit.getBenefitType().getName() == null) {	// Hibernate or DWR bug???
				veteranBenefit.setBenefitType(service.findBenefitTypeById(veteranBenefit.getBenefitType().getId()));
			}
			
			veteranBenefit.setBenefitTypeName(veteranBenefit.getBenefitType().getName());
			veteranBenefit.setBenefitTypeId(veteranBenefit.getBenefitType().getId());
			
			veteranBenefit.setIdBenefitType(veteranBenefit.getId());
			veteranBenefit.setActiveBenefitType(veteranBenefit.getActive());
			
			// Redmine 12083 comment #4 - get benefitRecipientList
			veteranBenefit.setBenefitRecipientList(service.findVeteranBenefitRecipients(veteranBenefit.getId()));				
		}
		
		return veteranBenefitTypes;
	}
	
	public void saveVeteranBenefitType(Long veteranId, Long benefitTypeId, VeteranBenefit veteranBenefit) throws Exception {
		
		logger.debug("entering saveVeteranBenefitType ...");
		
		// check if benefit type already entered
		if (service.findVeteranBenefitType(veteranId, benefitTypeId) != null) {
			throw new Exception("Benefit type already exists.");
		}
		
		veteranBenefit.setId(veteranBenefit.getIdBenefitType());
		veteranBenefit.setActive(veteranBenefit.getActiveBenefitType());
		
		BenefitType b = new BenefitType();
		b.setId(veteranBenefit.getBenefitTypeId());
		veteranBenefit.setBenefitType(b);
				
		Veteran v = new Veteran();
		v.setId(veteranId);
		veteranBenefit.setVeteran(v);
		
		if (veteranBenefit.getId() == null) {	// insert
			veteranBenefit.setActive(new Integer(1));
			veteranBenefit.setInsertTimestamp(new Date());
			veteranBenefit.setUpdateTimestamp(new Date());
		} else {	// update
			veteranBenefit.setUpdateTimestamp(new Date());
			
			// insert time stamp fix
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			String insertTimestampBenefitType = veteranBenefit.getInsertTimestampBenefitType();
			veteranBenefit.setInsertTimestamp(sdf.parse(insertTimestampBenefitType));
		}
		
		service.saveVeteranBenefitType(veteranBenefit);
	}
	
	public void copyBenefitTypeRecipients(Long benefitTypeId, Long currentVeteranId, Long origVeteranId) throws Exception {
		
		 logger.debug("entering copyBenefitTypeRecipients...");
		 
		 // copy benefitType if its not already exist.
		 Long currentVBId = service.findVeteranBenefitTypeId(currentVeteranId, benefitTypeId); 
		 VeteranBenefit vb = new VeteranBenefit();
		 if (currentVBId == null) { // current veteran does not have this benefit type
			// copy benefitType
			 Veteran v = new Veteran();
			 v.setId(currentVeteranId);
			 BenefitType b = new BenefitType();
			 b.setId(benefitTypeId);

			 vb.setVeteran(v);
			 vb.setBenefitType(b);
			 vb.setInsertTimestamp(new Date());
			 vb.setUpdateTimestamp(new Date());
			 vb.setActive(new Integer(1));
			 service.saveVeteranBenefitType(vb);	
		 } else {
			 vb.setId(currentVBId);
		 }

		 // copy benefit recipients
		 VeteranBenefit origVb = service.findVeteranBenefitType(origVeteranId, benefitTypeId);
		 List<BenefitRecipient> recipients = service.findVeteranBenefitRecipients(origVb.getId());
		 for (BenefitRecipient recipient : recipients) {
			 BenefitRecipient newRecipient = new BenefitRecipient();
			 BeanUtils.copyProperties(newRecipient, recipient);
			 newRecipient.setId(null);
			 newRecipient.setVeteranBenefit(vb);
			 newRecipient.setUpdateTimestamp(new Date());
			 
			 service.saveVeteranBenefitRecipient(newRecipient);
		 }
	}
	
	public void deleteVeteranBenefitType(Long id) throws Exception {
		
		logger.debug("entering.deleteVeteranBenefitType ...");
		
		// delete any BenefitRecipient(s) belong to this veteranBenefit (id)
		service.deleteVeteranBenefitRecipientsByVeteranBenefitId(id);
		
		VeteranBenefit v = new VeteranBenefit();
		v.setId(id);
		service.deleteVeteranBenefitType(v);
	}	
	
}
