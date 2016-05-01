package gov.utah.va.vts.dao.impl;

import gov.utah.va.vts.dao.RelationDao;
import gov.utah.va.vts.model.Relation;

import org.springframework.stereotype.Repository;

/**
 * Implementation class for RelationDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="relationDao")
public class RelationDaoImpl extends BaseEntityDaoImpl<Relation> implements RelationDao {

	public RelationDaoImpl() {
		super(Relation.class);
	}

}
