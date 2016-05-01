package gov.utah.va.vts.quartz;

import gov.utah.dts.det.util.Util;
import gov.utah.va.vts.model.Veteran;

import javax.persistence.EntityManager;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;

/**
 * Base class for running task classes.
 * 
 * @author hnguyen
 *
 */
public class BaseSchedulerTask {
	
	protected Log logger = org.apache.commons.logging.LogFactory.getLog(this.getClass());
	
	protected EntityManager em;

	protected boolean isMatched(Veteran v1, Veteran v2) {
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
	
	protected String getRandomNumber(int digitCount) {
		return RandomStringUtils.random(digitCount, "0123456789");	
	}
}
