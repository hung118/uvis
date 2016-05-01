package gov.utah.va.vts.quartz;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * This servelt context listener will resolve errors: "The web application [/vts] appears to have started a thread named 
 * [org.springframework.scheduling.quartz.SchedulerFactoryBean#0_Worker-1] but has failed to stop it. This is very likely to 
 * create a memory leak." This error occurs when re-deploying takes place or stopping tomcat 6.x. 
 * 
 * The idea is just waiting 1 second after tomcat shutdown.
 * 
 * @author Hnguyen
 *
 */
public class QuartzServletContextListener implements ServletContextListener {
	
	protected Log logger = org.apache.commons.logging.LogFactory.getLog(this.getClass());
	public static final String QUARTZ_FACTORY_KEY = "org.quartz.impl.StdSchedulerFactory.KEY";
	private ServletContext ctx = null;
	private StdSchedulerFactory factory = null;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ctx = sce.getServletContext();
		
		try {
			factory = new StdSchedulerFactory();
			
			// start the scheduler now
			factory.getScheduler().start();
			
			logger.info("Storing QuartzScheduler Factory at " + QUARTZ_FACTORY_KEY);
			ctx.setAttribute(QUARTZ_FACTORY_KEY, factory);
		} catch (Exception e) {
			logger.error("Quartz failed to initialize.", e);
		}		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
            factory.getScheduler().shutdown();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error("Quartz failed to shutdown", e);
        } catch (SchedulerException e) {
            logger.error("Quartz failed to shutdown", e);
        }	
	}

}
