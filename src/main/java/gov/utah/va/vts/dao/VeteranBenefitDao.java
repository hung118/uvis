package gov.utah.va.vts.dao;

import java.util.List;

import gov.utah.va.vts.model.VeteranBenefit;

/**
 * VeteranBenefit Dao interface.
 * 
 * @author hnguyen
 *
 */
public interface VeteranBenefitDao extends BaseEntityDao<VeteranBenefit> {
	
	/**
	 * Finds veteran benefits by veteran id.
	 * @param veteranId
	 * @return
	 */
	public List<VeteranBenefit> findVeteranBenefits(Long veteranId);
	
	/**
	 * Finds veteran benefit by veteran id and benefit type id.
	 * @param veteranId
	 * @param benefitTypeId
	 * @return
	 */
	public VeteranBenefit findVeteranBenefit(Long veteranId, Long benefitTypeId);
	
	/**
	 * Finds veteran_benefit ID.
	 * @param veteranId
	 * @param benefitTypeId
	 * @return
	 */
	public Long findVeteranBenefitId(Long veteranId, Long benefitTypeId);
	
	/**
	 * Deletes VeteranBenefit records by veteran id.
	 * @param veteranId
	 */
	public void deleteVeteranBenefitByVeteranId(Long veteranId);
}
