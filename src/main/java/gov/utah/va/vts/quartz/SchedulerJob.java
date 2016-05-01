package gov.utah.va.vts.quartz;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.apache.commons.logging.Log;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Scheduler job class defined in applicationContext-quartz.xml.
 * 
 * @author hnguyen
 * 
 */
public class SchedulerJob extends QuartzJobBean {

	protected Log logger = org.apache.commons.logging.LogFactory.getLog(this.getClass());
	
	// injected
	private SchedulerTask schedulerTask;

	public void setSchedulerTask(SchedulerTask schedulerTask) {
		this.schedulerTask = schedulerTask;
	}

	protected void executeInternal(JobExecutionContext jobExecutioncontext)
			throws JobExecutionException {

		EntityManagerFactory emf = null;
		EntityManager em = null;
		try {
			ApplicationContext appContext = (ApplicationContext) jobExecutioncontext
				.getScheduler().getContext().get("applicationContext");

			emf = (EntityManagerFactory) appContext
					.getBean("entityManagerFactory", EntityManagerFactory.class);
			em = emf.createEntityManager();
			TransactionSynchronizationManager.bindResource(emf, new EntityManagerHolder(em));

			// run tasks
			schedulerTask.runDriverLicenseDownload(em);
			
			schedulerTask.runDWSDownload(em);
			
			schedulerTask.runDeleteZeroSSN(em);
		
		} catch (Exception e) {
			logger.error("Failed to get/bind entity manager resource: ", e);
		} finally {
			logger.info("Clean up/unbind resources ...");
			TransactionSynchronizationManager.unbindResource(emf);
			if (em != null) {
				em.close();
			}
		}
	}
}