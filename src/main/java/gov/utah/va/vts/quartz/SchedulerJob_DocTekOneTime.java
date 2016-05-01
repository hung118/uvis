package gov.utah.va.vts.quartz;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Scheduler job class defined in applicationContext-scheduler.xml. This job will do one time
 * import for DocTek records on new database schema; after that, two lines in Scheduler section
 * in applicationContext-scheduler.xml must be commented out:
 * <ref bean="schedulerJob_DocTekOneTime" />
 * <ref bean="cronTrigger_DocTekOneTime" />
 * 
 * This job should be scheduled to run on the server not on localhost due to large data 64k records.
 * 
 * @author hnguyen
 * 
 */
public class SchedulerJob_DocTekOneTime extends QuartzJobBean {

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
			schedulerTask.runDocTeckImport(em);	
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("clean up resources ...");
			TransactionSynchronizationManager.unbindResource(emf);
			if (em != null) {
				em.close();
			}
		}
	}
}