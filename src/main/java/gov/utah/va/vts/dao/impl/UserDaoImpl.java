package gov.utah.va.vts.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




//import javax.persistence.NoResultException;
import javax.persistence.Query;

import gov.utah.va.vts.dao.UserDao;
import gov.utah.va.vts.model.NameValue;
import gov.utah.va.vts.model.User;

import org.springframework.stereotype.Repository;


/**
 * Implementation class for UserDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="userDao")
public class UserDaoImpl extends BaseEntityDaoImpl<User> implements UserDao {

	public UserDaoImpl() {
		super(User.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUsers(User user) {

		String hql = "from User u where " +
			"upper(u.firstName) like '%" + user.getFirstName().toUpperCase() + "%' "+
			"and upper(u.lastName) like '%" + user.getLastName().toUpperCase() + "%' " +
			"and u.email like '%" + user.getEmail().toLowerCase() + "%' " +
			(user.getActive() != null ? "and u.active = " + user.getActive() : "");
		hql += " order by u.lastName, u.firstName";
		
		Query q = entityManager.createQuery(hql);
		
		return q.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.UserDao#findUserByEmail(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public User findUserByEmail(String email) {
		Query q = entityManager.createQuery("from User where EMAIL = :email");
		
		q.setParameter("email", email.toLowerCase());
		
		List<User> users = q.getResultList();
		if (users.size() == 0) {
			return null;
		} else {
			return users.get(0);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.UserDao#findActiveUserByEmail(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public User findActiveUserByEmail(String email) {
		Query q = entityManager.createQuery("from User where active =  1 and email = :email");
		
		q.setParameter("email", email.toLowerCase());
		
		List<User> users = q.getResultList();
		if (users.size() == 0) {
			return null;
		} else {
			return users.get(0);
		}		
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.UserDao#saveRural(gov.utah.va.vts.model.NameValue)
	 */
	@Override
	public String saveRural(NameValue nv) {
		String ret = "edit";
		String sql = "update rural_crosswalk set designation = :designation where zip_code = :zipCode";
		Query q = entityManager.createNativeQuery(sql);
		q.setParameter("designation", nv.getValue());
		q.setParameter("zipCode", nv.getName());
		
		if (q.executeUpdate() != 1) {	// insert
			sql = "insert into rural_crosswalk (zip_code, designation) values(:zipCode, :designation)";
			q = entityManager.createNativeQuery(sql);
			q.setParameter("designation", nv.getValue());
			q.setParameter("zipCode", nv.getName());
			
			q.executeUpdate();
			ret = "insert";
		}
		
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.UserDao#findRurals(gov.utah.va.vts.model.NameValue)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NameValue> findRurals(NameValue nv) {
		StringBuffer sb = new StringBuffer("select zip_code, designation from rural_crosswalk where ");
		if (nv.getName().length() > 0 && nv.getValue().length() == 1) {
			sb.append("zip_code like '" + nv.getName() + "%' and designation = '" + nv.getValue() + "' and rownum < 501 ");
		} else if (nv.getName().trim().length() > 0) {
			sb.append("zip_code like '" + nv.getName() + "%' and rownum < 501 ")
			.append("order by zip_code");
		} else if ((nv.getValue().length() == 1)) {
			sb.append("designation = '" + nv.getValue() + "' and rownum < 501");
		} else {
			sb.append("zip_code like '84%' order by zip_code"); 	// default to Utah zip code
		}
		
		Query q = entityManager.createNativeQuery(sb.toString());
		List<Object[]> rows = q.getResultList();
		List<NameValue> results = new ArrayList<NameValue>();
		for (Object[] row : rows) {
			NameValue nameValue = new NameValue();
			nameValue.setName(((String)row[0]).toString());
			nameValue.setValue(((Character)row[1]).toString());
			
			results.add(nameValue);
		}
		
		return results;
	}

	@Override
	public void deleteRural(String zipCode) {
		String sql = "delete from rural_crosswalk where zip_code = :zipCode";
		Query q = entityManager.createNativeQuery(sql);
		q.setParameter("zipCode", zipCode);
		
		q.executeUpdate();
	}

	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.UserDao#findUtahRurals()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<NameValue> findUtahRurals() {
		String sql = "select zip_code, designation from rural_crosswalk where zip_code like '84%' order by zip_code";
		Query q = entityManager.createNativeQuery(sql);
		List<Object[]> rows = q.getResultList();
		List<NameValue> results = new ArrayList<NameValue>();
		for (Object[] row : rows) {
			NameValue nameValue = new NameValue();
			nameValue.setName(((String)row[0]).toString());
			nameValue.setValue(((Character)row[1]).toString());
			
			results.add(nameValue);
		}
		
		return results;	
	}
	
	/*
	 * (non-Javadoc)
	 * @see gov.utah.va.vts.dao.UserDao#findDirFileCount()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Long> findDirFileCount() {
		String sql = "select directory_count, file_count, file_number from attachment_pointer_info where id = 1";
		Query q = entityManager.createNativeQuery(sql);
		List<Object[]> rows = q.getResultList();
		if (rows.size() == 0) {
			return null;
		} else {
			Map<String, Long> retMap = new HashMap<String, Long>();
			Object[] row = rows.get(0);
			retMap.put("directoryCount", new Long(((BigDecimal)row[0]).longValue()));
			retMap.put("fileCount", new Long(((BigDecimal)row[1]).longValue()));
			retMap.put("fileNumber", new Long(((BigDecimal)row[2]).longValue()));
			return retMap;
		}
	}
	
	@Override
	public void saveDirFileCount(Map<String, Long> dfc) {
		String sql = "insert into attachment_pointer_info (id, directory_count, file_count) values(1, :directoryCount, :fileCount, :fileNumber)";
		Query q = entityManager.createNativeQuery(sql);
		q = entityManager.createNativeQuery(sql);
		q.setParameter("directoryCount", dfc.get("directoryCount"));
		q.setParameter("fileCount", dfc.get("fileCount"));
		q.setParameter("fileNumber", dfc.get("fileNumber"));
		
		q.executeUpdate();
	}

	@Override
	public void updateDirFileCount(Map<String, Long> dfc) {
		String sql = "update attachment_pointer_info set directory_count = :directoryCount, file_count = :fileCount, file_number = :fileNumber where id = 1";
		Query q = entityManager.createNativeQuery(sql);
		q.setParameter("directoryCount", dfc.get("directoryCount"));
		q.setParameter("fileCount", dfc.get("fileCount"));
		q.setParameter("fileNumber", dfc.get("fileNumber"));
		
		q.executeUpdate();
	}

	@Override
	public NameValue findRuralByName(String name) {
		String sql = "select zip_code as name, designation as value from rural_crosswalk where zip_code = :name";
		Query q = entityManager.createNativeQuery(sql);
		q.setParameter("name", name);
		
		@SuppressWarnings("unchecked")
		List<Object[]> rows = q.getResultList();
		
		if (rows.isEmpty()) {
			return null;
		} else {			
			return new NameValue();
		}
	}
	
}
