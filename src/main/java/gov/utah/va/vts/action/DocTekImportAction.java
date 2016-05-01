package gov.utah.va.vts.action;

/*
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
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Handles DocTek import - one time import. Data in DOCTEK_ALL table will be imported using branch lookup
 * in DOCTEK_LKUP table. This need a schedule to run using Quartz job scheduler (see DocTekDownload); otherwise it will
 * time out (at around 7000 out of 64K records) when running on web.
 * 
 * Un-used.
 * 
 * @author HNGUYEN
 *
 */
public class DocTekImportAction extends DataImportAction {

	private static final long serialVersionUID = 1L;

	public String display() throws Exception {
		
		logger.debug("entering displayDocTek ...");
		
		request.getSession().setAttribute(getText("DATA_LIST_SES"), service.findDataImportsBySource(new Long(getText("SOURCE_DTEK"))));
		return INPUT;
	}
	
	/*
	public String importData() throws Exception {
		
		logger.debug("entering importDocTek ...");
		
		String retMsg = "Success";
		
		// 1. convert pairs list to map for branch service lookup
		Map<String, String> branchLookup = service.findAllBranches();
		
		// 3. get list of available decoration and medals, service branch from db
		List<DecorationMedal> decorations = service.findAllDecorationMedals();
		List<ServiceBranch> serviceBranches = service.findAllServiceBranchesActive();
		
		// 4. get all records in DOCTEK_ALL table
		List<DocTek> records = service.findAllDocTek();
		
		// 5. insert records to db
		try {
			docTekDbInserts(records, branchLookup, decorations, serviceBranches);
		} catch (Exception e) {
        	retMsg = "Data import error: " + e.getMessage().substring(0, 75);
		}			
        	
		// 6. insert recorded record for this import operation.
		dataImport = new DataImport();
		dataImport.setFileName("DocTek");
		if (records != null) {
			dataImport.setRecordCount(new Integer(records.size()));
		} else {
			dataImport.setRecordCount(new Integer(0));
		}
		
		RecordType recordType = new RecordType();
		recordType.setId(new Long(getText("SOURCE_DTEK")));	
		dataImport.setRecordType(recordType);
		
		dataImport.setStatus(retMsg);		
		dataImport.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
		dataImport.setActive(new Integer(1));
		dataImport.setInsertTimestamp(new Date());
		dataImport.setUpdateTimestamp(new Date());
		
		service.saveDataImport(dataImport);
        
		// 7. return page
		request.getSession().setAttribute(getText("DATA_LIST_SES"), service.findDataImportsBySource(new Long(getText("SOURCE_DTEK"))));
		dataImport = null;
        if ("Success".equals(retMsg)) {
        	addActionMessage(getText("data.dldImportSuccess"));
        	return SUCCESS;
        } else {
        	dataImport = null;
        	addActionError(retMsg);
        	return INPUT;
        }
	}
	
	public String view() throws Exception {
		super.viewRecord();
		return SUCCESS;
	}
	
	public String delete() throws Exception {
		super.deleteRecord(new Long(getText("SOURCE_DTEK")));
		return SUCCESS;
	}
	
	private void docTekDbInserts(List<DocTek> records, Map<String, String> branch,
			List<DecorationMedal> decorations, List<ServiceBranch> serviceBranches) throws Exception {
		
		for (DocTek record : records) {
			docTekDbInsert(record, branch, decorations, serviceBranches);
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
		recordType.setId(new Long(getText("SOURCE_DTEK")));
		veteran.setRecordType(recordType);
		veteran.setSourceDate(new Date());
		
		veteran.setVaCurrent(new Integer(0));	// non-current
		veteran.setVerified(new Integer(0));	// non-verified
		veteran.setReviewed(new Integer(0));	// mark record as un-reviewed
		veteran.setContactable(new Integer(1));
		veteran.setShareFederalVa(new Boolean(true));
		veteran.setActive(new Integer(1));
		
		veteran.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
		veteran.setUpdatedBy((User)request.getSession().getAttribute(getText("USER")));			
		
		veteran.setInsertTimestamp(new Date());
		veteran.setUpdateTimestamp(new Date());
		
		// check if there is no other records for this ssn, mark it review and create current record
		Long branchId = getBranchId(record.getBranchOfService(), branchLookup, serviceBranches);
		List<Veteran> veterans = service.findVeteransBySsn(veteran.getSsn());
		if (veterans.size() == 0) {
			veteran.setReviewed(new Integer(1));
			Veteran currentVeteran = new Veteran();
			BeanUtils.copyProperties(currentVeteran, veteran);
							
			currentVeteran.setVaCurrent(new Integer(1));	// make it current veteran			
			currentVeteran = service.saveVeteran(currentVeteran);
			
			// service period/service branch for current veteran
			if (branchId != null) {
				setServicePeriod(branchId, currentVeteran.getId());
			}			
		} else {	// mark as reviewed when all data match current record
			for (Veteran v : veterans) {
				if (v.getVaCurrent().intValue() == 1 && isMatched(veteran, v)) {
					veteran.setReviewed(new Integer(1));
					break;
				}
			}
		}
		
		veteran = service.saveVeteran(veteran);
		
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
			if (decorationStr != null && decorationStr.contains(decoration.getName())) {
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
		
		service.saveVeteranServicePeriod(servicePeriod);
	}*/
}
