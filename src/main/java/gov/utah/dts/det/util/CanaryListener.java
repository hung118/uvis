package gov.utah.dts.det.util;

import java.util.ArrayList;
import java.util.List;

import gov.utah.dts.canary.property.CanaryProperties;
import gov.utah.dts.canary.property.DatabaseProperties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;

/**
 * Application monitor listener - CanaryListener.
 * 
 * @author Hnguyen
 *
 */
public class CanaryListener implements ServletContextListener {

	private Log logger = org.apache.commons.logging.LogFactory.getLog(this.getClass());
	
	private ServletContext context;
	private String ADDITIONAL_OBJECT = "additionalCanaryServlet";
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("CanaryListener start ...");
		
		context = sce.getServletContext();
		CanaryProperties cp = new CanaryProperties();
		
		try {
			buildProperties(cp);
		} catch (Exception e) {
			logger.warn("Problem of creating lists in CanaryListener: ", e);
		} finally {
			context.setAttribute(ADDITIONAL_OBJECT, cp);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		logger.info("CanaryListener destroyed.");
	}

	private void buildProperties(CanaryProperties cp) {
		cp.setDatabase(getDatabaseProperties());
		cp.setEndpoint(getEndPointProperties());
	}
	
	private List<DatabaseProperties> getDatabaseProperties() {
		List<DatabaseProperties>dbList = new ArrayList<DatabaseProperties>();
		
		DatabaseProperties props = new DatabaseProperties();
		props.setJndiName("jdbc/vts");
		props.setSql("select count(*) from va_vts.ethnicity");
		dbList.add(props);

//		Add additional jndi		
//		DatabaseProperties props2 = new DatabaseProperties();
//		props2.setJndiName("realJndi-JK-itsFake");
//		props2.setSql("select * from users");
//		dbList.add(props2);
		
		return dbList;
	}
	
	private List<String> getEndPointProperties() {
		List<String> urlList = new ArrayList<String>();
		
		urlList.add("https://login2.utah.gov");
		
		
		return urlList;
	}
	
}
