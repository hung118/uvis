package gov.utah.va.vts.dao.impl;

import gov.utah.va.vts.dao.BenefitTypeDao;
import gov.utah.va.vts.model.BenefitType;

import org.springframework.stereotype.Repository;

/**
 * Implementation class for BenefitTypeDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="benefitTypeDao")
public class BenefitTypeDaoImpl extends BaseEntityDaoImpl<BenefitType> implements BenefitTypeDao {

	public BenefitTypeDaoImpl() {
		super(BenefitType.class);
	}

}
