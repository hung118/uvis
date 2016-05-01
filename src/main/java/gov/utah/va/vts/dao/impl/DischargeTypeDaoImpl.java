package gov.utah.va.vts.dao.impl;

import gov.utah.va.vts.dao.DischargeTypeDao;
import gov.utah.va.vts.model.DischargeType;

import org.springframework.stereotype.Repository;

/**
 * Implementation class for DischargeTypeDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="dischargeTypeDao")
public class DischargeTypeDaoImpl extends BaseEntityDaoImpl<DischargeType> implements DischargeTypeDao {

	public DischargeTypeDaoImpl() {
		super(DischargeType.class);
	}

}
