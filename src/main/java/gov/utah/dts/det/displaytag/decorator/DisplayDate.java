package gov.utah.dts.det.displaytag.decorator;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

/**
 * The displaytag wrapper class to display date for a date object.
 * 
 * @author HNGUYEN
 *
 */
public class DisplayDate implements DisplaytagColumnDecorator {

	protected Log logger = org.apache.commons.logging.LogFactory.getLog(this.getClass());
	
	public DisplayDate() {
		
	}
	
	@Override
	public Object decorate(Object columnValue, PageContext pageContext,
			MediaTypeEnum media) throws DecoratorException {

		logger.debug("Entering decorate ...");
		
		if (columnValue == null) {
			return "";
		} else {
			Date date = (Date)columnValue;
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			
			return sdf.format(date);			
		}
	}

}
