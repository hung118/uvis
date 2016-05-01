package gov.utah.va.vts.dao.impl;

import java.util.List;

import javax.persistence.Query;

import gov.utah.va.vts.dao.BenefitRecipientDao;
import gov.utah.va.vts.model.BenefitRecipient;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
/**
 * Implementation class for BenefitRecipientDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="benefitRecipientDao")
@Transactional
public class BenefitRecipientDaoImpl extends BaseEntityDaoImpl<BenefitRecipient> implements BenefitRecipientDao {

	public BenefitRecipientDaoImpl() {
		super(BenefitRecipient.class);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.BenefitRecipientDao#findVeteranBenefitRecipients(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<BenefitRecipient> findVeteranBenefitRecipients(Long veteranBenefitId) {
		Query q = entityManager.createQuery("from BenefitRecipient where veteran_benefit_id = :veteranBenefitId");
		q.setParameter("veteranBenefitId", veteranBenefitId);

		return q.getResultList();	
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.BenefitRecipientDao#deleteBenefitRecipientByVeteranBenefitId(java.lang.Long)
	 */
	@Override
	public void deleteBenefitRecipientsByVeteranBenefitId(Long veteranBenefitId) {
		Query q = entityManager.createNativeQuery("delete from benefit_recipient where veteran_benefit_id = " + veteranBenefitId);
		q.executeUpdate();
	}
}
