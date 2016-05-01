package gov.utah.va.vts.dao.impl;

import gov.utah.va.vts.dao.EthnicityDao;
import gov.utah.va.vts.model.Ethnicity;

import org.springframework.stereotype.Repository;

/**
 * Implementation class for EthnicityDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="ethnicityDao")
public class EthnicityDaoImpl extends BaseEntityDaoImpl<Ethnicity> implements EthnicityDao {

	public EthnicityDaoImpl() {
		super(Ethnicity.class);
	}

}
