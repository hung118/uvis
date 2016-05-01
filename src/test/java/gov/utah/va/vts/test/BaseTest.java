package gov.utah.va.vts.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;

public class BaseTest extends AbstractTransactionalTestNGSpringContextTests {
	
	/**
	 * base logging capability for all subclasses.
	 */
	protected Log logger = LogFactory.getLog(this.getClass());

}
