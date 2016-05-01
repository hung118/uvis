package gov.utah.va.vts.test.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.utah.dts.det.test.util.TestUtils;
import gov.utah.va.vts.dao.UserDao;
import gov.utah.va.vts.model.Role;
import gov.utah.va.vts.model.User;
import gov.utah.va.vts.test.BaseTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@ContextConfiguration(locations = { "/spring_conf/applicationContext-test.xml" })
public class UserDaoTest extends BaseTest {
	
	@Autowired
	private UserDao userDao;
	
	@Test
	public void userUpdateDao() {
		
		logger.debug("entering userUpdateDao...");
		User u = getRandomUser();
		
		u.setId(108L);	// update user 108
		
		// set roles
		List<Role> roles = new ArrayList<Role>();
		Role role1 = new Role();
		role1.setId(4L);
		roles.add(role1);
		
		u.setRoles(roles);
		
		u.setActive(new Integer(1));
		u.setInsertTimestamp(new Date());
		u.setUpdateTimestamp(new Date());
		
		User createdBy = new User();
		createdBy.setId(100L);
		u.setCreatedBy(createdBy);
		
		User updatedBy = new User();
		updatedBy.setId(100L);
		u.setUpdatedBy(updatedBy);
		
		
		assert(u.getId() != null);
		userDao.save(u);
		assert(u.getId() != null);
	}
	
	@Test
	public void userInsertDao() {
		
		logger.debug("Entering userInsertDao ...");
		User u = getRandomUser();
		
		// set roles
		List<Role> roles = new ArrayList<Role>();
		Role role1 = new Role();
		role1.setId(2L);
		roles.add(role1);
		Role role2 = new Role();
		role2.setId(1L);
		roles.add(role2);
		
		u.setRoles(roles);
		
		u.setActive(new Integer(1));
		u.setInsertTimestamp(new Date());
		u.setUpdateTimestamp(new Date());
		
		User createdBy = new User();
		createdBy.setId(100L);
		u.setCreatedBy(createdBy);
		
		User updatedBy = new User();
		updatedBy.setId(100L);
		u.setUpdatedBy(updatedBy);
		
		
		assert(u.getId() == null);
		userDao.save(u);
		assert(u.getId() != null);
	}

	private User getRandomUser() {
		
		User u = new User();
		
		u.setFirstName(TestUtils.getRandomName());
		u.setLastName(TestUtils.getRandomName());
		u.setEmail(TestUtils.getRandomEmail());
		
		return u;
	}
}
