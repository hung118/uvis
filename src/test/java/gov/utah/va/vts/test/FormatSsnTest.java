package gov.utah.va.vts.test;

import gov.utah.dts.det.util.Validate;

import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.Test;

@ContextConfiguration(locations = { "/spring_conf/applicationContext-test.xml" })
public class FormatSsnTest extends BaseTest {
		
	@Test
	public void testFormatSsn() {
		
		assert(Validate.isSSNValid("123454545"));
	}

}
