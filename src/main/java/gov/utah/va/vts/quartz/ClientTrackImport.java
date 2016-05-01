package gov.utah.va.vts.quartz;

import gov.utah.dts.det.util.Util;
import gov.utah.va.vts.model.ClientTrack;
import gov.utah.va.vts.model.DataImport;
import gov.utah.va.vts.model.RecordType;
import gov.utah.va.vts.model.User;
import gov.utah.va.vts.model.Veteran;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.BeanUtils;

/**
 * DocTek records import one-time scheduler task.
 * 
 * @author hnguyen
 *
 */
public class ClientTrackImport extends BaseSchedulerTask {

	private ResourceBundle prop = null;
	
	private int badRec = 0;
	
	public ClientTrackImport(EntityManager em) {
		super.em = em;
	}
	
	public void run() throws Exception {
		
		logger.info("Entering Quartz scheduler task run() ...");
		
		prop = ResourceBundle.getBundle("global-messages");
		em.getTransaction().begin();
		List<ClientTrack> records = null;
		String retMsg = "Success";
		
		try {
			records = findAllClientTrack();
			
			// insert records to db
			try {
				clientTrackDbInserts(records);
			} catch (Exception e) {
	        	retMsg = "Data import error: " + e.getMessage().substring(0, 75);
			}			
		} catch (Exception e) {
			// log the error and continue
			logger.error(e.getMessage());
		} finally {
			try {
				// insert recorded record for this import operation.
				DataImport dataImport = new DataImport();
				
				dataImport.setFileName("ClientTrack_1_Time");	
				if (records != null) {
					dataImport.setRecordCount(new Integer(records.size()));
				} else {
					dataImport.setRecordCount(new Integer(0));
				}
				dataImport.setBadRec(new Integer(badRec));
				RecordType recordType = new RecordType();
				recordType.setId(new Long(prop.getString("SOURCE_CLIENT_TRACK")));	// ClientTrack record type (source)
				dataImport.setRecordType(recordType);
				
				dataImport.setStatus(retMsg);
				
				User defaultUser = new User();
				defaultUser.setId(new Long(prop.getString("SYSTEM_USER")));
				dataImport.setCreatedBy(defaultUser);
				
				dataImport.setActive(new Integer(1));
				dataImport.setInsertTimestamp(new Date());
				dataImport.setUpdateTimestamp(new Date());
				
				em.persist(dataImport);
				
			} catch (Exception e) {
				// log the error and continue 
				logger.error(e.getMessage());
			}

			// commit transaction to save whatever being done.
			em.getTransaction().commit();
		}
		
	}
			
	@SuppressWarnings("unchecked")
	private List<ClientTrack> findAllClientTrack() {

		//String sql = "select socsecno, lastname, firstname, middleinitial, birthdate, address, address2, city, state, zipcode, zipcodeid, homephone, workphone, email, gender from clientrack_fullssn where rownum < 500 order by lastname, firstname";
		String sql = "select socsecno, lastname, firstname, middleinitial, birthdate, address, address2, city, state, zipcode, zipcodeid, homephone, workphone, email, gender from clientrack_fullssn order by lastname, firstname";
		Query q = em.createNativeQuery(sql);
		
		List<Object[]> rows = q.getResultList();
		List<ClientTrack> result = new ArrayList<ClientTrack>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (Object[] row : rows) {
			ClientTrack clientTrack = new ClientTrack();
			clientTrack.setSsn((String)row[0]);
			clientTrack.setLastName(Util.toUpperCaseNull((String)row[1]));
			clientTrack.setFirstName(Util.toUpperCaseNull((String)row[2]));
			clientTrack.setMiddleInitial(Util.toUpperCaseNull((String)row[3]));
			try {
				if (row[4] != null) {
					clientTrack.setDateOfBirth(sdf.parse(((String)row[4]).substring(0, 10)));
				} else {
					clientTrack.setDateOfBirth(null);
				}
				
			} catch (Exception e) {
				clientTrack.setDateOfBirth(null);
			}
			clientTrack.setAddress((String)row[5]);
			clientTrack.setAddress2((String)row[6]);
			clientTrack.setCity((String)row[7]);
			clientTrack.setState((String)row[8]);
			if ((String)row[9] != null) {
				clientTrack.setZipCode((String)row[9]);
			} else {
				if (row[10] != null) clientTrack.setZipCode(((String)row[10]).substring(0, 5));
			}
			clientTrack.setHomePhone((String)row[11]);
			clientTrack.setWorkPhone((String)row[12]);
			clientTrack.setEmail((String)row[13]);
			clientTrack.setGender((String)row[14] == "1" ? "Female" : "Male");
			
			result.add(clientTrack);
		}
		
		return result;
	}
	
	private void clientTrackDbInserts(List<ClientTrack> records) {
		
		for (ClientTrack record : records) {
			try {
				clientTrackDbInsert(record);
			} catch (Exception e) {
				// log the error, increase badRec, and continue to next record
				badRec++;
				logger.error(e.getMessage());
			}
		}
	}
	
	private void clientTrackDbInsert(ClientTrack record) throws Exception {
		
		Veteran veteran = new Veteran();
		veteran.setLastName(record.getLastName());
		veteran.setFirstName(record.getFirstName());
		veteran.setMiddleName(record.getMiddleInitial());
		veteran.setDateOfBirth(record.getDateOfBirth());
		
		if (record.getSsn() != null && record.getSsn().length() == 11) {
			veteran.setSsn(record.getSsn());
		} else {	// default to WW2-##-#### - Redmine 12421
			veteran.setSsn("WW2-" + getRandomNumber(2) + "-" + getRandomNumber(4));
		}
		
		veteran.setAddress1(record.getAddress());
		veteran.setAddress2(record.getAddress2());
		veteran.setCity(record.getCity());
		veteran.setState(record.getState());
		veteran.setZip(record.getZipCode());
		veteran.setPrimaryPhone(record.getHomePhone());
		veteran.setAltPhone(record.getWorkPhone());
		veteran.setEmail(record.getEmail());
		veteran.setGender(record.getGender());
		
		RecordType recordType = new RecordType();
		recordType.setId(new Long(prop.getString("SOURCE_CLIENT_TRACK")));
		veteran.setRecordType(recordType);
		veteran.setSourceDate(new Date());
		
		veteran.setVaCurrent(new Integer(0));	// non-current
		veteran.setVerified(new Integer(0));	// un-verified
		veteran.setReviewed(new Integer(0));	// mark record as un-reviewed
		veteran.setContactable(new Integer(1));
		veteran.setShareFederalVa(new Boolean(false));
		veteran.setActive(new Integer(1));
		
		User defaultUser = new User();
		defaultUser.setId(new Long(prop.getString("SYSTEM_USER")));
		veteran.setCreatedBy(defaultUser);
		veteran.setUpdatedBy(defaultUser);			
		
		veteran.setInsertTimestamp(new Date());
		veteran.setUpdateTimestamp(new Date());
		
		// check if there is no other records for this ssn, mark it review and create current record
		List<Veteran> veterans = findVeteransBySsn(veteran.getSsn());
		if (veterans.size() == 0) {	// not found
			veteran.setReviewed(new Integer(1));
			Veteran currentVeteran = new Veteran();
			BeanUtils.copyProperties(currentVeteran, veteran);
							
			currentVeteran.setVaCurrent(new Integer(1));	// make it current veteran
			currentVeteran.setId(null);		// make sure it's null, not 0.
			em.persist(currentVeteran);		
		} else {	// found more than 1 records with same SSN
			for (Veteran v : veterans) {				
				// mark as reviewed when all data match current record
				if (v.getVaCurrent().intValue() == 1 && isMatched(veteran, v)) {
					veteran.setReviewed(new Integer(1));
				}
				
				// add any new data to current record - Redmine 14035 #3
				if (v.getVaCurrent().intValue() == 1) {
					try {
						addNewData(veteran, v);
					} catch (Exception e) {}
				}
			}
		}
		
		em.persist(veteran);
	}
	
	@SuppressWarnings("unchecked")
	private List<Veteran> findVeteransBySsn(String ssn) {
		String hql = "from Veteran where ssn = :ssn order by source ";
		
		Query q = em.createQuery(hql);
		q.setParameter("ssn", ssn);
			
		return q.getResultList();	
	}

	private void addNewData(Veteran vet, Veteran currentVet) throws Exception {
		
		if (currentVet.getFirstName() == null) currentVet.setFirstName(vet.getFirstName());
		if (currentVet.getLastName() == null) currentVet.setLastName(vet.getLastName());
		if (currentVet.getDateOfBirth() == null) currentVet.setDateOfBirth(vet.getDateOfBirth());
		if (currentVet.getGender() == null) currentVet.setGender(vet.getGender());
		if (currentVet.getAddress1() == null) currentVet.setAddress1(vet.getAddress1());
		if (currentVet.getCity() == null) currentVet.setCity(vet.getCity());
		if (currentVet.getState() == null) currentVet.setState(vet.getState());
		if (currentVet.getZip() == null) currentVet.setZip(vet.getZip());
		if (currentVet.getPrimaryPhone() == null) currentVet.setPrimaryPhone(vet.getPrimaryPhone());
		if (currentVet.getAltPhone() == null) currentVet.setAltPhone(vet.getAltPhone());
		if (currentVet.getEmail() == null) currentVet.setEmail(vet.getEmail());
		
		em.persist(currentVet);
	}
}
