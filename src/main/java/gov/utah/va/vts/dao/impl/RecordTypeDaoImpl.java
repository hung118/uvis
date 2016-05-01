package gov.utah.va.vts.dao.impl;

import gov.utah.va.vts.dao.RecordTypeDao;
import gov.utah.va.vts.model.RecordType;

import org.springframework.stereotype.Repository;

/**
 * Implementation class for RecordTypeDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="recordTypeDao")
public class RecordTypeDaoImpl extends BaseEntityDaoImpl<RecordType> implements RecordTypeDao {

	public RecordTypeDaoImpl() {
		super(RecordType.class);
	}

}
