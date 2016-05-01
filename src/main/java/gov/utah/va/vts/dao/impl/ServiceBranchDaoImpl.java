package gov.utah.va.vts.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import gov.utah.dts.det.util.Util;
import gov.utah.va.vts.dao.ServiceBranchDao;
import gov.utah.va.vts.model.ServiceBranch;

import org.springframework.stereotype.Repository;

/**
 * Implementation class for ServiceBranchDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="serviceBranchDao")
public class ServiceBranchDaoImpl extends BaseEntityDaoImpl<ServiceBranch> implements ServiceBranchDao {

	public ServiceBranchDaoImpl() {
		super(ServiceBranch.class);
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.ServiceBranchDao#findLkupBranches()
	 */
	public Map<String, String> findLkupBranches() {
		
		String sql = "select name, value from doctek_lkup order by name";
		Query q = entityManager.createNativeQuery(sql);
		
		@SuppressWarnings("unchecked")
		List<Object[]> rows = q.getResultList();
		Map<String, String> result = new HashMap<String, String>();
		for (Object[] row : rows) {
			result.put(Util.removeSpaces((String) row[0]), (String) row[1]);
		}
		
		return result;
	}
}
