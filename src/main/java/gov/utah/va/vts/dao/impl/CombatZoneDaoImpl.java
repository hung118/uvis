package gov.utah.va.vts.dao.impl;

import gov.utah.va.vts.dao.CombatZoneDao;
import gov.utah.va.vts.model.CombatZone;

import org.springframework.stereotype.Repository;

/**
 * Implementation class for CombatZoneDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="combatZoneDao")
public class CombatZoneDaoImpl extends BaseEntityDaoImpl<CombatZone> implements CombatZoneDao {

	public CombatZoneDaoImpl() {
		super(CombatZone.class);
	}

}
