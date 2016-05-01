package gov.utah.va.vts.quartz;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Scheduled task to delete all orphan 000-00-0000 SSNs in veteran_info table.
 * 
 * @author hnguyen
 *
 */
public class DeleteZerosSSN extends BaseSchedulerTask {

	public DeleteZerosSSN(EntityManager em) {
		super.em = em;
	}
	
	public void run() throws Exception {
		
		String sql = "delete from veteran_info where ssn = '000-00-0000'";
		Query q = em.createNativeQuery(sql);
		
		em.getTransaction().begin();
		q.executeUpdate();
		em.getTransaction().commit();
	}
}
