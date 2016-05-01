package gov.utah.va.vts.dao.impl;

import gov.utah.va.vts.dao.RoleDao;
import gov.utah.va.vts.model.Role;

import org.springframework.stereotype.Repository;

/**
 * Implementation class for RoleDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="roleDao")
public class RoleDaoImpl extends BaseEntityDaoImpl<Role> implements RoleDao {

	public RoleDaoImpl() {
		super(Role.class);
	}

}
