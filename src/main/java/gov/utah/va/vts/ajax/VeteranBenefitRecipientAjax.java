package gov.utah.va.vts.ajax;

import gov.utah.va.vts.model.BenefitRecipient;
import gov.utah.va.vts.model.Relation;
import gov.utah.va.vts.model.VeteranBenefit;
import gov.utah.va.vts.service.VtsService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Ajax class for calls from veteran.jsp, veteran benefit type section.
 * 
 * @author HNGUYEN
 *
 */
public class VeteranBenefitRecipientAjax {
	protected Log logger = org.apache.commons.logging.LogFactory.getLog(this.getClass());
	
	@Autowired
	VtsService service;
	
	public List<BenefitRecipient> getVeteranBenefitRecipients(Long veteranBenefitId) throws Exception {
		
		logger.debug("entering getVeteranBenefitRecipients ...");		
		
		List<BenefitRecipient> veteranBenefitRecipients = service.findVeteranBenefitRecipients(veteranBenefitId); 
		for (BenefitRecipient veteranBenefitRecipient : veteranBenefitRecipients) {	
			if (veteranBenefitRecipient.getPrimaryPhone() != null) {
				String[] primaryPhone = veteranBenefitRecipient.getPrimaryPhone().split("-");
				veteranBenefitRecipient.setPhone1(primaryPhone[0]);
				veteranBenefitRecipient.setPhone2(primaryPhone[1]);
				veteranBenefitRecipient.setPhone3(primaryPhone[2]);				
			}
			
			if (veteranBenefitRecipient.getRelation() != null) {
				veteranBenefitRecipient.setRelationId(veteranBenefitRecipient.getRelation().getId());
			}
			veteranBenefitRecipient.setIdBenefitRecipient(veteranBenefitRecipient.getId());
			veteranBenefitRecipient.setActiveBenefitRecipient(veteranBenefitRecipient.getActive());			
		}
		
		return veteranBenefitRecipients;
	}
	
	public void saveVeteranBenefitRecipient(BenefitRecipient veteranBenefitRecipient) throws Exception {
		
		logger.debug("entering saveVeteranBenefitRecipient ...");
		
		veteranBenefitRecipient.setId(veteranBenefitRecipient.getIdBenefitRecipient());
		
		VeteranBenefit v = new VeteranBenefit();
		v.setId(veteranBenefitRecipient.getVeteranBenefitId());
		veteranBenefitRecipient.setVeteranBenefit(v);
		
		if (veteranBenefitRecipient.getRelationId() != null) {
			Relation r = new Relation();
			r.setId(veteranBenefitRecipient.getRelationId());
			veteranBenefitRecipient.setRelation(r);			
		} else {
			veteranBenefitRecipient.setRelation(null);
		}
		
		veteranBenefitRecipient.setPrimaryPhone(veteranBenefitRecipient.getPhone1() + "-" + 
				veteranBenefitRecipient.getPhone2() + "-" + veteranBenefitRecipient.getPhone3());
		if (veteranBenefitRecipient.getPrimaryPhone().length() != 12) veteranBenefitRecipient.setPrimaryPhone(null);

		if (veteranBenefitRecipient.getId() == null) {	// insert
			veteranBenefitRecipient.setActive(new Integer(1));
			veteranBenefitRecipient.setInsertTimestamp(new Date());
			veteranBenefitRecipient.setUpdateTimestamp(new Date());
		} else {	// update
			veteranBenefitRecipient.setActive(veteranBenefitRecipient.getActiveBenefitRecipient());
			veteranBenefitRecipient.setUpdateTimestamp(new Date());
			
			// insert time stamp fix
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			String insertTimestampBenefitRecipient = veteranBenefitRecipient.getInsertTimestampBenefitRecipient();
			veteranBenefitRecipient.setInsertTimestamp(sdf.parse(insertTimestampBenefitRecipient));
		}
		
		service.saveVeteranBenefitRecipient(veteranBenefitRecipient);
	}
	
	public void deleteVeteranBenefitRecipient(Long id) throws Exception {
		
		logger.debug("entering. deleteVeteranBenefitRecipient ...");
		BenefitRecipient b = new BenefitRecipient();
		b.setId(id);
		service.deleteVeteranBenefitRecipient(b);
	}	
	
}
