package gov.utah.va.vts.test;

import gov.utah.dts.det.util.Util;
import gov.utah.va.vts.dao.VeteranDao;
import gov.utah.va.vts.model.Veteran;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.Test;

@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@ContextConfiguration(locations = { "/spring_conf/applicationContext-test.xml" })
public class DataImportTest extends BaseTest {

	@Autowired
	private VeteranDao veteranDao;
	
	@Test
	public void checkMatchingRecords() {
		Veteran v1 = veteranDao.findById(264633L);		// unreviewed record, supposed to be reviewed
		Veteran v2 = veteranDao.findById(264632L);		// current record
		
		if (isMatched(v1, v2)) {
			assert(true);
		} else {
			assert(false);
		}
	}
	
	private boolean isMatched(Veteran v1, Veteran v2) {
		boolean ret = true;

		if (!Util.isStringEqual(v1.getLastName(), v2.getLastName())) {
			ret = false;
		} else if (!Util.isStringEqual(v1.getFirstName(), v2.getFirstName())) {
			ret = false;
		} else if (v1.getDateOfBirth() != null && !v1.getDateOfBirth().equals(v2.getDateOfBirth())) {
			ret = false;
		} else if (!Util.isStringEqual(v1.getSsn(), v2.getSsn())) {
			ret = false;
		} else if (!Util.isStringEqual(v1.getAddress1(), v2.getAddress1())) {
			ret = false;
		} else if (!Util.isStringEqual(v1.getCity(), v2.getCity())) {
			ret = false;
		} else if (!Util.isStringEqual(v1.getState(), v2.getState())) {
			ret = false;
		} else if (!Util.isStringEqual(v1.getZip(), v2.getZip())) {
			ret = false;
		} else if (!Util.isStringEqual(v1.getMailingAddr1(), v2.getMailingAddr1())) {
			ret = false;
		} else if (!Util.isStringEqual(v1.getMailingCity(), v2.getMailingCity())) {
			ret = false;
		} else if (!Util.isStringEqual(v1.getMailingState(), v2.getMailingState())) {
			ret = false;
		} else if (!Util.isStringEqual(v1.getMailingZip(), v2.getMailingZip())) {
			ret = false;
		} else if (!Util.isStringEqual(v1.getEmail(), v2.getEmail())) {
			ret = false;
		}
		
		return ret;
	}
}
