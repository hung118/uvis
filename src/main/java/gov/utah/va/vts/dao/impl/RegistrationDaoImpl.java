package gov.utah.va.vts.dao.impl;

import java.util.List;

import javax.persistence.Query;

import gov.utah.va.vts.dao.RegistrationDao;
import gov.utah.va.vts.model.Registration;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation class from RegistrationDao interface.
 * @author HNGUYEN
 *
 */
@Repository (value="registrationDao")
@Transactional
public class RegistrationDaoImpl extends BaseEntityDaoImpl<Registration> implements RegistrationDao {

	public RegistrationDaoImpl() {
		super(Registration.class);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.RegistrationDao#findRegisteredRecords()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Registration> findRegisteredRecords() {
		String hql = "from Registration where transfer_date is null";
		Query q = entityManager.createQuery(hql);
		
		return q.getResultList();
	}

}
