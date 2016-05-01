package gov.utah.va.vts.quartz;

import javax.persistence.EntityManager;

/**
 * Scheduler task defined in applicationContext-quartz.xml.
 * 
 * @author hnguyen
 *
 */
public class SchedulerTask {
   
   public void runDriverLicenseDownload(EntityManager em) throws Exception {
	   
	   DriverLicenseDownload dl = new DriverLicenseDownload(em);
	   dl.run();
   }
   
   public void runDWSDownload(EntityManager em) throws Exception {
	   
	   DWSDownload dws = new DWSDownload(em);
	   dws.run();
   }
   
   public void runDeleteZeroSSN(EntityManager em) throws Exception {
	   
	   DeleteZerosSSN deleteZerosSSN = new DeleteZerosSSN(em);
	   deleteZerosSSN.run();
   }
   
   public void runRegistrationImport(EntityManager em) throws Exception {
	   
	   RegistrationImport registration = new RegistrationImport(em);
	   registration.run();
   }
   
   /**
    * *** Caution *** Runs this method only once for a new schema database.
    * To disable the run, comment out the following 2 lines in applicationContext-quartz.xml
    * <ref bean="schedulerJob_OnlineOneTime" />
    * <ref bean="cronTrigger_OnlineOneTime" />
    * 
    * @param em
    * @throws Exception
    */
   public void runOnlineImport(EntityManager em) throws Exception {
	   
	   OnlineImport online = new OnlineImport(em);
	   online.run();
   }
   
   /**
    * *** Caution *** Runs this method only once for a new schema database.
    * To disable the run, comment out the following 2 lines in applicationContext-quartz.xml
    * <ref bean="schedulerJob_DocTekOneTime" />
    * <ref bean="cronTrigger_DocTekOneTime" />
    * 
    * @param em
    * @throws Exception
    */
   public void runDocTeckImport(EntityManager em) throws Exception {
	   
	   DocTekImport docTek = new DocTekImport(em);
	   docTek.run();
   }
   
   /**
    * *** Caution *** Runs this method only once for a new schema database.
    * To disable the run, comment out the following 2 lines in applicationContext-quartz.xml
    * <ref bean="schedulerJob_ClientTrackOneTime" />
    * <ref bean="cronTrigger_ClientTrackOneTime" />
    * 
    * @param em
    * @throws Exception
    */
   public void runClientTrackImport(EntityManager em) throws Exception {
	   
	   ClientTrackImport clientTrack = new ClientTrackImport(em);
	   clientTrack.run();
   }
   
}