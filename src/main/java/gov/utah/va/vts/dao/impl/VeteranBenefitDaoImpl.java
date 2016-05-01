package gov.utah.va.vts.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import gov.utah.va.vts.dao.VeteranBenefitDao;
import gov.utah.va.vts.model.VeteranBenefit;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation class for VeteranBenefitDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="veteranBenefitDao")
@Transactional
public class VeteranBenefitDaoImpl extends BaseEntityDaoImpl<VeteranBenefit> implements VeteranBenefitDao {

	public VeteranBenefitDaoImpl() {
		super(VeteranBenefit.class);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranBenefitDao#findVeteranBenefit(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<VeteranBenefit> findVeteranBenefits(Long veteranId) {
		
		Query q = entityManager.createQuery("from VeteranBenefit where veteran_id = :veteranId");
		q.setParameter("veteranId", veteranId);

		return q.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranBenefitDao#findVeteranBenefit(java.lang.Long, java.lang.Long)
	 */
	@Override
	public VeteranBenefit findVeteranBenefit(Long veteranId, Long benefitTypeId) {

		Query q = entityManager.createQuery("from VeteranBenefit where veteran_id = :veteranId and benefit_type_id = :benefitTypeId");
		q.setParameter("veteranId", veteranId);
		q.setParameter("benefitTypeId", benefitTypeId);
		
		VeteranBenefit entity = null;
		
		try {
			entity = (VeteranBenefit)q.getSingleResult(); 
		} catch (NoResultException nre) {
			// return null when not found
		}
		
		return entity; 
	}
	
	@Override
	public Long findVeteranBenefitId(Long veteranId, Long benefitTypeId) {
		
		Long entity = null;
		
		final String SQL = "select id from veteran_benefit where veteran_id = :veteranId and benefit_type_id = :benefitTypeId";

		Query q = entityManager.createNativeQuery(SQL);
		q.setParameter("veteranId", veteranId);
		q.setParameter("benefitTypeId", benefitTypeId);
		
		try {
			entity = new Long(((java.math.BigDecimal)q.getSingleResult()).longValue());
		} catch (NoResultException nre) {
			// return null when not found
		}
		
		return entity; 
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranBenefitDao#deleteVeteranBenefitByVeteranId(java.lang.Long)
	 */
	@Override
	public void deleteVeteranBenefitByVeteranId(Long veteranId) {
		Query q = entityManager.createNativeQuery("delete from veteran_benefit where veteran_id = " + veteranId);
		q.executeUpdate();
	}

}
