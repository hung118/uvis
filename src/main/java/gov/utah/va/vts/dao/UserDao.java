package gov.utah.va.vts.dao;

import java.util.List;
import java.util.Map;

import gov.utah.va.vts.model.NameValue;
import gov.utah.va.vts.model.User;

public interface UserDao extends BaseEntityDao<User> {
	
	/**
	 * Finds a user by email. If multiple users are found, returns the first one.
	 * 
	 * @param email
	 * @return
	 */
	public User findUserByEmail(String email);
	
	/**
	 * Finds active user by email. If multiple users are found, returns the first one.
	 * 
	 * @param email
	 * @return
	 */
	public User findActiveUserByEmail(String email);
	
	/**
	 * Search users.
	 * 
	 * @param user
	 * @return
	 */
	public List<User> findUsers(User user);
	
	/**
	 * Updates or inserts rural crosswalk.
	 * 
	 * @param nv
	 * @return U or I for update or insert.
	 */
	public String saveRural(NameValue nv);
	
	/**
	 * Finds rurals.
	 * @param nv
	 * @return list of rurals name value objects.
	 */
	public List<NameValue> findRurals(NameValue nv);
	
	/**
	 * Deletes rural crosswalk
	 * @param zipCode
	 */
	public void deleteRural(String zipCode);
	
	/**
	 * Finds all utah rural zip codes.
	 * @return list of Namevalue objects
	 */
	public List<NameValue> findUtahRurals();
	
	/**
	 * Finds directory and file counts from attachment_pointer_info table.
	 * @return map object.
	 */
	public Map<String, Long> findDirFileCount();
	
	/**
	 * Inserts first record into attachment_pointer_info table.
	 * @param dfc - a map object of values to be inserted.
	 */
	public void saveDirFileCount(Map<String, Long> dfc);
	
	/**
	 * Updates first record of attachment_pointer_info table.
	 * @param dfc - a map object of values to be updated.
	 */
	public void updateDirFileCount(Map<String, Long> dfc);
	
	/**
	 * Find rural by name
	 * @param name
	 * @return
	 */
	public NameValue findRuralByName(String name);
	
}
