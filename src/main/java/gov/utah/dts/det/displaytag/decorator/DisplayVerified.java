package gov.utah.dts.det.displaytag.decorator;

import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

/**
 * The displaytag wrapper class to display verified field.
 * 
 * @author HNGUYEN
 *
 */
public class DisplayVerified implements DisplaytagColumnDecorator {

	protected Log logger = org.apache.commons.logging.LogFactory.getLog(this.getClass());
	
	public DisplayVerified() {
		
	}
	
	@Override
	public Object decorate(Object columnValue, PageContext pageContext,
			MediaTypeEnum media) throws DecoratorException {

		logger.debug("Entering decorate ...");
		
		Integer verified = (Integer)columnValue;
		
		return verified.intValue() == 1 ? "Y" : "N";
	}

}
