package gov.utah.va.vts.dao.impl;

import java.util.List;

import javax.persistence.Query;

import gov.utah.va.vts.dao.VeteranServicePeriodDao;
import gov.utah.va.vts.model.VeteranServicePeriod;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation class for VeteranServicePeriodDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="veteranServicePeriodDao")
@Transactional
public class VeteranServicePeriodDaoImpl extends BaseEntityDaoImpl<VeteranServicePeriod> implements VeteranServicePeriodDao {

	public VeteranServicePeriodDaoImpl() {
		super(VeteranServicePeriod.class);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranServicePeriodDao#findVeteranServicePeriod(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	public List<VeteranServicePeriod> findVeteranServicePeriods(Long veteranId) {
		
		Query q = entityManager.createQuery("from VeteranServicePeriod where veteran_id = :veteranId order by start_date");
		q.setParameter("veteranId", veteranId);

		return q.getResultList();
	}
	
	public void deleteVeteranServicePeriodsByVeteranId(Long veteranId) {
		
		String sql = "delete from veteran_service_period where veteran_id = " + veteranId.intValue();
		Query q = entityManager.createNativeQuery(sql);
		
		q.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.VeteranServicePeriodDao#deleteVspDm(java.lang.Long)
	 */
	@Override
	public void deleteVspDm(Long veteranServicePeriodId) {
		String sql = "delete from vsp_dm where veteran_service_period_id = " + veteranServicePeriodId.intValue();
		Query q = entityManager.createNativeQuery(sql);
		
		q.executeUpdate();
	}
	
}
