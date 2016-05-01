package gov.utah.va.vts.dao;

import java.util.List;

import gov.utah.va.vts.model.BenefitRecipient;

/**
 * BenefitRecipient Dao interface.
 * 
 * @author hnguyen
 *
 */
public interface BenefitRecipientDao extends BaseEntityDao<BenefitRecipient> {

	/**
	 * Finds benefit recipients with specified veteranBenefitId.
	 * @param veteranBenefitId
	 * @return
	 */
	public List<BenefitRecipient> findVeteranBenefitRecipients(Long veteranBenefitId);
	
	/**
	 * Deletes BenefitRecipient entities by veteranBenefitId.
	 * @param veteranBenefitId
	 */
	public void deleteBenefitRecipientsByVeteranBenefitId(Long veteranBenefitId);
}
