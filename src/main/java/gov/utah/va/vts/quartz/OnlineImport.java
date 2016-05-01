package gov.utah.va.vts.quartz;

import gov.utah.dts.det.util.Util;
import gov.utah.va.vts.model.DataImport;
import gov.utah.va.vts.model.DecorationMedal;
import gov.utah.va.vts.model.Note;
import gov.utah.va.vts.model.Online;
import gov.utah.va.vts.model.RecordType;
import gov.utah.va.vts.model.ServiceBranch;
import gov.utah.va.vts.model.ServiceEra;
import gov.utah.va.vts.model.User;
import gov.utah.va.vts.model.Veteran;
import gov.utah.va.vts.model.VeteranServicePeriod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Online records import one-time scheduler task.
 * 
 * @author hnguyen
 *
 */
public class OnlineImport extends BaseSchedulerTask {

	private ResourceBundle prop = null;
	
	private int badRec = 0;
	
	private List<ServiceBranch> serviceBranches;
	private List<ServiceEra> allServiceEras;
	
	public OnlineImport(EntityManager em) {
		super.em = em;
	}
	
	public void run() throws Exception {
		
		logger.info("Entering Quartz scheduler task run() ...");
		
		prop = ResourceBundle.getBundle("global-messages");
		em.getTransaction().begin();
		List<Online> records = null;
		String retMsg = "Success";
		
		try {
			List<DecorationMedal> decorations = findAllDecorationMedals();
			serviceBranches = findAllServiceBranchesActive();
			allServiceEras = findAllServiceErasActive();
			records = findAllOnline();
			
			// insert records to db
			try {
				onlineDbInserts(records, decorations);
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
				
				dataImport.setFileName("Online_1_Time");	
				if (records != null) {
					dataImport.setRecordCount(new Integer(records.size()));
				} else {
					dataImport.setRecordCount(new Integer(0));
				}
				dataImport.setBadRec(new Integer(badRec));
				RecordType recordType = new RecordType();
				recordType.setId(new Long(prop.getString("SOURCE_ONLINE")));	// Online record type (source)
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
	private List<DecorationMedal> findAllDecorationMedals() {
		String hql = "from DecorationMedal order by name";
		
		Query q = em.createQuery(hql);

		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<ServiceBranch> findAllServiceBranchesActive() {
		String hql = "from ServiceBranch order by name";
		
		Query q = em.createQuery(hql);

		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<ServiceEra> findAllServiceErasActive() {
		String hql = "from ServiceEra order by startDate";
		
		Query q = em.createQuery(hql);

		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	private List<Online> findAllOnline() {

		String sql = "select ssn_clnd, lastname, firstname, servedfrom, servedto, branch, branch_clnd, medals, address, addresscity, state, zip, phone, phone2, email, combat, injured, comp, purpleheart from web_registered_clnd order by lastname, firstname";
		Query q = em.createNativeQuery(sql);
		
		List<Object[]> rows = q.getResultList();
		List<Online> result = new ArrayList<Online>();
		for (Object[] row : rows) {
			Online online = new Online();
			online.setSsn((String)row[0]);
			online.setLastName(Util.toUpperCaseNull((String)row[2]));	// first name and last name are reverse in table???
			online.setFirstName(Util.toUpperCaseNull((String)row[1]));
			online.setServedFrom((Date)row[3]);
			online.setServedTo((Date)row[4]);
			online.setBranch((String)row[5]);
			online.setBranchClean((String)row[6]);
			online.setMedals((String)row[7]);
			online.setAddress((String)row[8]);
			online.setCity((String)row[9]);
			online.setState((String)row[10]);
			online.setZip((String)row[11]);
			online.setPrimaryPhone(Util.formatPhone((String)row[12]));
			online.setAltPhone(Util.formatPhone((String)row[13]));
			online.setEmail((String)row[14]);
			online.setCombat((String)row[15]);
			online.setInjured((String)row[16]);
			online.setComp((String)row[17]);
			online.setPurpleHeart((String)row[18]);
			
			result.add(online);
		}
		
		return result;
	}
	
	private void onlineDbInserts(List<Online> records, List<DecorationMedal> decorations) {
		
		for (Online record : records) {
			try {
				onlineDbInsert(record, decorations);
			} catch (Exception e) {
				// log the error, increase badRec, and continue to next record
				badRec++;
				logger.error(e.getMessage());
			}
		}
	}
	
	private void onlineDbInsert(Online record, List<DecorationMedal> decorations) throws Exception {
		
		Veteran veteran = new Veteran();

		if (record.getSsn() != null && record.getSsn().length() == 11) {
			veteran.setSsn(record.getSsn());
		}
		veteran.setLastName(record.getLastName());
		
		// look for first name and middle name separated by space in first name field and split them into first and middle name
		if (!"".equals(record.getFirstName())) {
			String[] names = record.getFirstName().split(" ");
			if (names.length > 1) {
				veteran.setFirstName(names[0]);
				veteran.setMiddleName("");
				for (int i = 1; i < names.length; i++) {
					veteran.setMiddleName(veteran.getMiddleName() + names[i]);
				}
			} else {
				veteran.setFirstName(record.getFirstName());
			}
		}		
		
		veteran.setAddress1(record.getAddress());
		veteran.setCity(record.getCity());
		veteran.setState(record.getState());
		veteran.setZip(record.getZip());
		veteran.setPrimaryPhone(record.getPrimaryPhone());
		veteran.setAltPhone(record.getAltPhone());
		veteran.setEmail(record.getEmail());
		if ("Yes".equals(record.getComp())) {
			veteran.setVaEnrolled(true);
		}
		
		// decoration and medal
		String medals = "Yes".equals(record.getPurpleHeart()) ? "Purple Heart," + record.getMedals() : record.getMedals(); 
		Long[] decorationMedalLongs = getDecorationList(medals, decorations);
		List<DecorationMedal> decorationMedals = new ArrayList<DecorationMedal>();
		for (Long decorationMedalLong : decorationMedalLongs) {
			if (decorationMedalLong != null) {
				DecorationMedal dm = new DecorationMedal();
				dm.setId(decorationMedalLong);
				decorationMedals.add(dm);				
			}
		}
		if (decorationMedals.size() > 0) {
			veteran.setDecorationMedalList(decorationMedals);
		}
		
		RecordType recordType = new RecordType();
		recordType.setId(new Long(prop.getString("SOURCE_ONLINE")));
		veteran.setRecordType(recordType);
		veteran.setSourceDate(new Date());
		
		veteran.setVaCurrent(new Integer(0));	// non-current
		veteran.setVerified(new Integer(0));	// non-verified
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
		
		// notes
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		ArrayList<Note> noteList = new ArrayList<Note>();
		if (record.getBranch() != null && record.getBranch().contains(",")) {	// create note for multiple branch names
			Note note = new Note();
			note.setCreatedBy(defaultUser);
			note.setActive(new Integer(1));
			note.setInsertTimestamp(new Date());
			note.setUpdateTimestamp(new Date());
			note.setNoteText("* Multiple Service periods: " + sdf.format(record.getServedFrom()) + " - " + sdf.format(record.getServedTo()) + " - " + record.getBranch() + "<br>");
			em.persist(note);
			noteList.add(note);
		}
		if (record.getBranchClean() == null) {	// create note for null branch_clnd required field for service period
			Note note = new Note();
			note.setCreatedBy(defaultUser);
			note.setActive(new Integer(1));
			note.setInsertTimestamp(new Date());
			note.setUpdateTimestamp(new Date());
			note.setNoteText("* Branch service not found: " + sdf.format(record.getServedFrom()) + " - " + sdf.format(record.getServedTo()) + " - " + record.getBranch() + "<br>");
			em.persist(note);
			noteList.add(note);
		}
		if ("Yes".equals(record.getCombat())) { // create note for combat
			Note note = new Note();
			note.setCreatedBy(defaultUser);
			note.setActive(new Integer(1));
			note.setInsertTimestamp(new Date());
			note.setUpdateTimestamp(new Date());
			note.setNoteText("* Combat Zone Yes<br>");
			em.persist(note);
			noteList.add(note);			
		} else {
			Note note = new Note();
			note.setCreatedBy(defaultUser);
			note.setActive(new Integer(1));
			note.setInsertTimestamp(new Date());
			note.setUpdateTimestamp(new Date());
			note.setNoteText("* Combat Zone No<br>");
			em.persist(note);
			noteList.add(note);
		}
		if ("Yes".equals(record.getInjured())) { // create note for injured
			Note note = new Note();
			note.setCreatedBy(defaultUser);
			note.setActive(new Integer(1));
			note.setInsertTimestamp(new Date());
			note.setUpdateTimestamp(new Date());
			note.setNoteText("* Injured/Disability<br>");
			em.persist(note);
			noteList.add(note);			
		}
		veteran.setNoteList(noteList);
				
		// check if there is no other records for this ssn, mark it review and create current record
		List<Veteran> veterans = findVeteransBySsn(veteran.getSsn());
		if (veterans.size() == 0) {	// not found
			veteran.setReviewed(new Integer(1));
			Veteran currentVeteran = new Veteran();
			BeanUtils.copyProperties(currentVeteran, veteran);
							
			currentVeteran.setVaCurrent(new Integer(1));	// make it current veteran
			currentVeteran.setId(null);		// make sure it's null, not 0.

			// insert orig note to current record. This is needed or else 2 records will point to the same note id.
			if (veteran.getNoteList() != null) {
				List<Note> newNotes = new ArrayList<Note>();
				for (Note oldNote : veteran.getNoteList()) {
					Note newNote = new Note();
					BeanUtils.copyProperties(newNote, oldNote);
					newNote.setId(null);
					em.persist(newNote);
					newNotes.add(newNote);
				}
				currentVeteran.setNoteList(newNotes);
			}
			em.persist(currentVeteran);
			
			// service period/service branch for current veteran
			setServicePeriod(record, currentVeteran.getId());			
		} else {	// found more than 1 records with same SSN
			for (Veteran v : veterans) {
				// add decoration/medal, service period, and others to current record if it is from DL.
				if (v.getVaCurrent().intValue() == 1 && v.getRecordType().getId().equals(new Long(prop.getString("SOURCE_DL")))) {
					if (decorationMedals.size() > 0 && v.getDecorationMedalList().size() == 0) {
						v.setDecorationMedalList(decorationMedals);
					}
					setServicePeriod(record, v.getId());
					if (veteran.getEmail() != null && v.getEmail() == null) {
						v.setEmail(veteran.getEmail());
					}
					if (veteran.getPrimaryPhone() != null && v.getPrimaryPhone() == null) {
						v.setPrimaryPhone(veteran.getPrimaryPhone());
					}
					if (veteran.getAltPhone() != null && v.getAltPhone() == null) {
						v.setAltPhone(veteran.getAltPhone());
					}
					if (veteran.getVaEnrolled()) {
						v.setVaEnrolled(true);
					}
					
					em.persist(v);
				}
				
				// mark as reviewed when all data match current record
				if (v.getVaCurrent().intValue() == 1 && isMatched(veteran, v)) {
					veteran.setReviewed(new Integer(1));
					break;
				}				
			}
		}
		
		em.persist(veteran);
		
		// service period/service branch for veteran
		setServicePeriod(record, veteran.getId());
	}

	private Long getBranchId(String branchName) {
		
		Long branchId = null;

		for (ServiceBranch branch : serviceBranches) {
			if (branchName != null && branchName.equalsIgnoreCase(branch.getName())) {
				branchId = branch.getId();
				break;
			}
		}
		
		return branchId;
	}
	
	private Long[] getDecorationList(String decorationStr, List<DecorationMedal> decorations) {
		
		Long[] decorationList = new Long[decorations.size()];
		
		int i = 0;
		for (DecorationMedal decoration : decorations) {
			if (decorationStr != null && decorationStr.toLowerCase().contains(decoration.getName().toLowerCase())) {
				decorationList[i] = decoration.getId();
				i++;
			}
		}
		
		return decorationList;
	}
	
	private void setServicePeriod(Online rec, Long veteranId) throws Exception {
		
		if (rec.getBranchClean() == null) {
			return;
		}
		
		VeteranServicePeriod servicePeriod = new VeteranServicePeriod();
		
		servicePeriod.setId(null);	// make sure it's null, not 0.
		
		if (getBranchId(rec.getBranchClean()) == null) {
			return;
		} else {
			ServiceBranch sb = new ServiceBranch();
			sb.setId(getBranchId(rec.getBranchClean()));
			servicePeriod.setServiceBranch(sb);
		}
		
		servicePeriod.setActive(new Integer(1));
		servicePeriod.setInsertTimestamp(new Date());
		servicePeriod.setUpdateTimestamp(new Date());
		
		// service eras, it may span more than 1 era see VeteranServiceAjax.saveVeteranService()
		Veteran v = new Veteran();
		v.setId(veteranId);
		servicePeriod.setVeteran(v);
		
		Date startDate = rec.getServedFrom();
		Date endDate = rec.getServedTo();
		if (startDate != null && endDate != null) {
			List<ServiceEra> serviceEras = getServiceEras(startDate, endDate);
			if (serviceEras.size() == 0) {
				servicePeriod.setServiceEra(null);
				servicePeriod.setStartDate(startDate);
				servicePeriod.setEndDate(endDate);
				em.persist(servicePeriod);
			} else {				
				for (int i = 0; i < serviceEras.size(); i++ ) {
					ServiceEra serviceEra = serviceEras.get(i);
					ServiceEra se = new ServiceEra();
					VeteranServicePeriod newVsp = new VeteranServicePeriod();
					BeanUtils.copyProperties(newVsp, servicePeriod);

					se.setId(serviceEra.getId());
					newVsp.setServiceEra(se);
					
					newVsp.setStartDate(startDate);
					
					if (i == serviceEras.size() - 1) {
						newVsp.setEndDate(endDate);
					} else {
						newVsp.setEndDate(serviceEra.getEndDate());
						startDate = Util.addDaysToDate(1, serviceEra.getEndDate());
					}
						
					newVsp.setId(null);	// make sure it's null, not 0.
					em.persist(newVsp);					
				}
			}
		} else {
			servicePeriod.setServiceEra(null);			
			em.persist(servicePeriod);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<Veteran> findVeteransBySsn(String ssn) {
		//String hql = "from Veteran where ssn = :ssn order by source ";
		String hql = "select model from Veteran model where model.ssn = :ssn order by source ";
		
		Query q = em.createQuery(hql);
		q.setParameter("ssn", ssn);
		
		List<Veteran> list = null;
		try {
			list = q.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		
		return list;
	}

	private List<ServiceEra> getServiceEras(Date startDate, Date endDate) {
		
		ArrayList<ServiceEra> retVal = new ArrayList<ServiceEra>();
		for (ServiceEra serviceEra : allServiceEras) {
			if (Util.isDateInRange(startDate, endDate, serviceEra.getStartDate(), serviceEra.getEndDate())) {
				retVal.add(serviceEra);
			}
		}
		
		return retVal;
	}
}
