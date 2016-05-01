package gov.utah.va.vts.dao.impl;

import gov.utah.va.vts.dao.ServiceEraDao;
import gov.utah.va.vts.model.ServiceEra;

import org.springframework.stereotype.Repository;

/**
 * Implementation class for ServiceEraDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="serviceEraDao")
public class ServiceEraDaoImpl extends BaseEntityDaoImpl<ServiceEra> implements ServiceEraDao {

	public ServiceEraDaoImpl() {
		super(ServiceEra.class);
	}

}
