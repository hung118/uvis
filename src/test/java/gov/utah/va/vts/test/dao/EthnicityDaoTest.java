package gov.utah.va.vts.test.dao;

import gov.utah.va.vts.dao.EthnicityDao;
import gov.utah.va.vts.model.Ethnicity;
import gov.utah.va.vts.test.BaseTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@ContextConfiguration(locations = { "/spring_conf/applicationContext-test.xml" })
public class EthnicityDaoTest extends BaseTest {
	
	@Autowired
	private EthnicityDao ethnicityDao;
	
	@Test
	public void testUserDao() {
		
		Ethnicity e = new Ethnicity();
		e.setName("testing");
		e.setDescription("this is a test.");
		assert(e.getId() == null);
		ethnicityDao.save(e);
		assert(e.getId() != null);
	}

}
