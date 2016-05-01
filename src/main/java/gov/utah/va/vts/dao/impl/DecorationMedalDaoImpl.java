package gov.utah.va.vts.dao.impl;

import gov.utah.va.vts.dao.DecorationMedalDao;
import gov.utah.va.vts.model.DecorationMedal;

import org.springframework.stereotype.Repository;

/**
 * Implementation class for DecorationMedalDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="decorationMedalDao")
public class DecorationMedalDaoImpl extends BaseEntityDaoImpl<DecorationMedal> implements DecorationMedalDao {

	public DecorationMedalDaoImpl() {
		super(DecorationMedal.class);
	}

}
