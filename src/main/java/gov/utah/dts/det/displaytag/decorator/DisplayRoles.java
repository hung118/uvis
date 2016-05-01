package gov.utah.dts.det.displaytag.decorator;

import java.util.List;

import gov.utah.va.vts.model.Role;

import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

/**
 * The displaytag wrapper class to display list of roles as a string separated by comma.
 * 
 * @author HNGUYEN
 *
 */
public class DisplayRoles implements DisplaytagColumnDecorator {

	protected Log logger = org.apache.commons.logging.LogFactory.getLog(this.getClass());
	
	public DisplayRoles() {
		
	}
	
	@Override
	public Object decorate(Object columnValue, PageContext pageContext,
			MediaTypeEnum media) throws DecoratorException {

		logger.debug("Entering decorate ...");
		
		@SuppressWarnings("unchecked")
		List<Role> roles = (List<Role>)columnValue;
		String roleString = "";
		for (int i = 0; i < roles.size(); i++) {
			roleString += (i == roles.size() - 1) ? (roles.get(i)).getRoleName() : (roles.get(i)).getRoleName() + ","; 
		}
		
		return roleString;
	}

}
