package gov.utah.va.vts.quartz;

import gov.utah.dts.det.util.Util;
import gov.utah.va.vts.model.DataImport;
import gov.utah.va.vts.model.DecorationMedal;
import gov.utah.va.vts.model.DocTek;
import gov.utah.va.vts.model.RecordType;
import gov.utah.va.vts.model.ServiceBranch;
import gov.utah.va.vts.model.User;
import gov.utah.va.vts.model.Veteran;
import gov.utah.va.vts.model.VeteranServicePeriod;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
public class DocTekImport extends BaseSchedulerTask {

	private ResourceBundle prop = null;
	
	private int badRec = 0;
	
	public DocTekImport(EntityManager em) {
		super.em = em;
	}
	
	public void run() throws Exception {
		
		logger.info("Entering Quartz scheduler task run() ...");
		
		prop = ResourceBundle.getBundle("global-messages");
		em.getTransaction().begin();
		List<DocTek> records = null;
		String retMsg = "Success";
		
		try {
			Map<String, String> branchLookup = findAllBranches();
			List<DecorationMedal> decorations = findAllDecorationMedals();
			List<ServiceBranch> serviceBranches = findAllServiceBranchesActive();
			records = findAllDocTek();
			
			// insert records to db
			try {
				docTekDbInserts(records, branchLookup, decorations, serviceBranches);
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
				
				dataImport.setFileName("DocTek_1_Time");	
				if (records != null) {
					dataImport.setRecordCount(new Integer(records.size()));
				} else {
					dataImport.setRecordCount(new Integer(0));
				}
				dataImport.setBadRec(new Integer(badRec));
				RecordType recordType = new RecordType();
				recordType.setId(new Long(prop.getString("SOURCE_DTEK")));	// DocTek record type (source)
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
	private Map<String, String> findAllBranches() {
		
		String sql = "select name, value from doctek_lkup order by name";
		Query q = em.createNativeQuery(sql);
		
		List<Object[]> rows = q.getResultList();
		Map<String, String> result = new HashMap<String, String>();
		for (Object[] row : rows) {
			result.put((String) row[0], (String) row[1]);
		}
		
		return result;
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
	private List<DocTek> findAllDocTek() {

		//String sql = "select ssn, last_name, first_name, middle_initial, branch_of_service, date_of_birth, decorations_and_medals from doctek_all where rownum < 500 order by last_name, first_name";
		String sql = "select ssn, last_name, first_name, middle_initial, branch_of_service, date_of_birth, decorations_and_medals from doctek_all order by last_name, first_name";
		Query q = em.createNativeQuery(sql);
		
		List<Object[]> rows = q.getResultList();
		List<DocTek> result = new ArrayList<DocTek>();
		for (Object[] row : rows) {
			DocTek docTek = new DocTek();
			docTek.setSsn((String)row[0]);
			docTek.setLastName(Util.toUpperCaseNull((String)row[1]));
			docTek.setFirstName(Util.toUpperCaseNull((String)row[2]));
			docTek.setMiddleInitial(Util.toUpperCaseNull((String)row[3]));
			docTek.setBranchOfService((String)row[4]);
			docTek.setDateOfBirth((Date)row[5]);
			docTek.setDecorationsAndMedals((String)row[6]);
			
			result.add(docTek);
		}
		
		return result;
	}
	
	private void docTekDbInserts(List<DocTek> records, Map<String, String> branch,
			List<DecorationMedal> decorations, List<ServiceBranch> serviceBranches) {
		
		for (DocTek record : records) {
			try {
				docTekDbInsert(record, branch, decorations, serviceBranches);
			} catch (Exception e) {
				// log the error, increase badRec, and continue to next record
				badRec++;
				logger.error(e.getMessage());
			}
		}
	}
	
	private void docTekDbInsert(DocTek record, Map<String, String> branchLookup,
			List<DecorationMedal> decorations, List<ServiceBranch> serviceBranches) throws Exception {
		
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
		
		// decoration and medal
		Long[] decorationMedalLongs = getDecorationList(record.getDecorationsAndMedals(), decorations);
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
		recordType.setId(new Long(prop.getString("SOURCE_DTEK")));
		veteran.setRecordType(recordType);
		veteran.setSourceDate(new Date());
		
		veteran.setVaCurrent(new Integer(0));	// non-current
		veteran.setVerified(new Integer(1));	// verified - this is DD214 form which should have been verified. Redmine 14035 comment 5
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
		Long branchId = getBranchId(record.getBranchOfService(), branchLookup, serviceBranches);
		List<Veteran> veterans = findVeteransBySsn(veteran.getSsn());
		if (veterans.size() == 0) {	// not found
			veteran.setReviewed(new Integer(1));
			Veteran currentVeteran = new Veteran();
			BeanUtils.copyProperties(currentVeteran, veteran);
							
			currentVeteran.setVaCurrent(new Integer(1));	// make it current veteran
			currentVeteran.setId(null);		// make sure it's null, not 0.
			em.persist(currentVeteran);
			
			// service period/service branch for current veteran
			if (branchId != null) {
				setServicePeriod(branchId, currentVeteran.getId());
			}			
		} else {	// found more than 1 records with same SSN
			for (Veteran v : veterans) {
				// add decoration/medal and branch ID if the current record is from DL. Redmine 14035 comment 2
				if (v.getVaCurrent().intValue() == 1 && v.getRecordType().getId().equals(new Long(prop.getString("SOURCE_DL")))) {
					if (decorationMedals.size() > 0) {
						v.setDecorationMedalList(decorationMedals);
					}
					if (branchId != null) {
						setServicePeriod(branchId, v.getId());
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
		if (branchId != null) {
			setServicePeriod(branchId, veteran.getId());
		}
	}

	private Long getBranchId(String branchName, Map<String, String> branchLookup, List<ServiceBranch> serviceBranches) {
		
		Long branchId = null;

		String suggestedBranchName = branchLookup.get(branchName);
		for (ServiceBranch branch : serviceBranches) {
			if (suggestedBranchName != null && suggestedBranchName.equalsIgnoreCase(branch.getName())) {
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
	
	private void setServicePeriod(Long branchId, Long veteranId) throws Exception {
		
		VeteranServicePeriod servicePeriod = new VeteranServicePeriod();
		
		ServiceBranch sb = new ServiceBranch();
		sb.setId(branchId);
		servicePeriod.setServiceBranch(sb);
		
		servicePeriod.setActive(new Integer(1));
		servicePeriod.setInsertTimestamp(new Date());
		servicePeriod.setUpdateTimestamp(new Date());
		
		Veteran v = new Veteran();
		v.setId(veteranId);
		servicePeriod.setVeteran(v);
		
		em.persist(servicePeriod);
	}
	
	@SuppressWarnings("unchecked")
	private List<Veteran> findVeteransBySsn(String ssn) {
		String hql = "from Veteran where ssn = :ssn order by source ";
		
		Query q = em.createQuery(hql);
		q.setParameter("ssn", ssn);
			
		return q.getResultList();	
	}

}
