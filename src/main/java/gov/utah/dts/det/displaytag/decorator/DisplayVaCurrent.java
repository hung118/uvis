package gov.utah.dts.det.displaytag.decorator;

import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

/**
 * The displaytag wrapper class to display vaCurrent field.
 * 
 * @author HNGUYEN
 *
 */
public class DisplayVaCurrent implements DisplaytagColumnDecorator {

	protected Log logger = org.apache.commons.logging.LogFactory.getLog(this.getClass());
	
	public DisplayVaCurrent() {
		
	}
	
	@Override
	public Object decorate(Object columnValue, PageContext pageContext,
			MediaTypeEnum media) throws DecoratorException {

		logger.debug("Entering decorate ...");
		
		Integer vaCurrent = (Integer)columnValue;
		
		return vaCurrent.intValue() == 1 ? "Y" : "N";
	}

}
