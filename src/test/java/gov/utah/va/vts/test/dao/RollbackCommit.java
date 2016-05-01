package gov.utah.va.vts.test.dao;

//import java.util.List;

//import gov.utah.va.vts.dao.BenefitRecipientDao;
import gov.utah.va.vts.dao.VeteranBenefitDao;
import gov.utah.va.vts.dao.VeteranDao;
import gov.utah.va.vts.dao.VeteranServicePeriodDao;
import gov.utah.va.vts.model.Veteran;
//import gov.utah.va.vts.model.VeteranBenefit;
import gov.utah.va.vts.model.VeteranServicePeriod;
import gov.utah.va.vts.test.BaseTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@ContextConfiguration(locations = { "/spring_conf/applicationContext-test.xml" })
public class RollbackCommit extends BaseTest {
	
	@Autowired
	private VeteranDao veteranDao;
	@Autowired
	private VeteranServicePeriodDao veteranServicePeriodDao;
	@Autowired
	private VeteranBenefitDao veteranBenefitDao;
	//@Autowired
	//private BenefitRecipientDao benefitRecipientDao;
	
	@Test
	public void rollbackCommitTx() throws Exception {
		
		logger.debug("entering rollbackCommitTx...");
		
		try {
			Long veteranId = new Long(267L);
			
			Veteran veteran = veteranDao.findById(veteranId);
			
			for (VeteranServicePeriod veteranServicePeriod : veteran.getServicePeriodList()) {
				veteranServicePeriodDao.delete(veteranServicePeriod);
			}
			veteran.setServicePeriodList(null);
			
			/*
			List<VeteranBenefit> veteranBenefits = veteranBenefitDao.findVeteranBenefits(veteranId);
			for (VeteranBenefit veteranBenefit : veteranBenefits) {
				benefitRecipientDao.deleteBenefitRecipientsByVeteranBenefitId(veteranBenefit.getId());
			}*/
			veteranBenefitDao.deleteVeteranBenefitByVeteranId(veteranId);
			
			veteranDao.delete(veteran);
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}
	
}
