package gov.utah.va.vts.action;

import gov.utah.dts.det.util.Util;
import gov.utah.va.vts.model.DataImport;
import gov.utah.va.vts.model.DecorationMedal;
import gov.utah.va.vts.model.DischargeType;
import gov.utah.va.vts.model.Ethnicity;
import gov.utah.va.vts.model.RecordType;
import gov.utah.va.vts.model.Registration;
import gov.utah.va.vts.model.ServiceBranch;
import gov.utah.va.vts.model.ServiceEra;
import gov.utah.va.vts.model.User;
import gov.utah.va.vts.model.Veteran;
import gov.utah.va.vts.model.VeteranServicePeriod;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Handles online registration data import.
 * 
 * @author HNGUYEN
 *
 */
public class RegistrationImportAction extends DataImportAction {

	private static final long serialVersionUID = 1L;

	private int badRec = 0;
	
	public String display() throws Exception {
		
		logger.debug("entering displayRegistration ...");
		
		request.getSession().setAttribute(getText("DATA_LIST_SES"), service.findDataImportsBySource(new Long(getText("SOURCE_ONLINE"))));
		return INPUT;
	}
	
	public String importReg() throws Exception {
		
		logger.debug("entering importRegistration ...");
		
		// 1. get list of records from Registration table.
		List<Registration> records = service.findRegisteredRecords();
		String retMsg = "Success";
		
		// 2. insert records to db from uploaded file
		if ("Success".equals(retMsg)) {
			try {
				registrationDbInserts(records);
			} catch (Exception e) {
				if (e.getMessage().length() > 65) {
					retMsg = "Online registration data import error: " + e.getMessage().substring(0, 65);
				} else {
					retMsg = "Online registration data import error: " + e.getMessage();
				}
			}			
		}
        	
		// 3. insert recorded record for this import operation.
		dataImport = new DataImport();
		dataImport.setFileName("Online Registration");
		
		if (records != null) {
			dataImport.setRecordCount(new Integer(records.size()));
		} else {
			dataImport.setRecordCount(new Integer(0));
		}
		dataImport.setBadRec(new Integer(badRec));
				
		RecordType recordType = new RecordType();
		recordType.setId(new Long(getText("SOURCE_ONLINE")));	// Online record type (source)
		dataImport.setRecordType(recordType);
		
		dataImport.setStatus(retMsg);		
		dataImport.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
		dataImport.setActive(new Integer(1));
		dataImport.setInsertTimestamp(new Date());
		dataImport.setUpdateTimestamp(new Date());
		
		service.saveDataImport(dataImport);
        
		// 4. return page
		request.getSession().setAttribute(getText("DATA_LIST_SES"), service.findDataImportsBySource(new Long(getText("SOURCE_ONLINE"))));
		dataImport = null;
        if ("Success".equals(retMsg)) {
        	addActionMessage(getText("data.registrationSuccess"));
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
		super.deleteRecord(new Long(getText("SOURCE_ONLINE")));
		return SUCCESS;
	}
		
	public int getBadRec() {
		return badRec;
	}

	public void setBadRec(int badRec) {
		this.badRec = badRec;
	}
		
	private void registrationDbInserts(List<Registration> records) throws Exception {
				
		for (Registration record : records) {
			try {
				registrationDbInsert(record);
			} catch (Exception e) {
				// log the error, increase badRec, and continue to next record
				setBadRec(getBadRec() + 1);
				logger.error(e.getMessage());
			}
		}			
	}
	
	private void registrationDbInsert(Registration record) throws Exception {
		
		// 1. save information from registration to veteran_info table
		Veteran veteran = new Veteran();
		veteran.setFirstName(record.getFirstName());
		veteran.setLastName(record.getLastName());
		veteran.setMiddleName(record.getMiddleName());
		veteran.setAddress1(record.getAddress1());
		veteran.setAddress2(record.getAddress2());
		veteran.setCity(record.getCity());
		veteran.setState(record.getState());
		veteran.setZip(record.getZip());
		veteran.setGender(record.getGender());
		veteran.setEmail(record.getEmail());
		veteran.setSuffix(record.getSuffix());
		veteran.setEmailOption(record.getEmailOption());
		veteran.setShareFederalVa(record.getShareFederalVa());
		veteran.setVaEnrolled(record.getVaEnrolled());
		veteran.setPercentDisability(record.getPercentDisability());
		veteran.setSsn(record.getSsn());
		if (record.getEthnicityId() != null) {
			Ethnicity e = new Ethnicity();
			e.setId(record.getEthnicityId());
			veteran.setEthnicity(e);
		}
		veteran.setPrimaryPhone(record.getPrimaryPhone());
		veteran.setDateOfBirth(record.getDateOfBirth());
		List<DecorationMedal> decorationMedals = null;
		if (record.getPurpleHeart()) {
			decorationMedals = new ArrayList<DecorationMedal>();
			DecorationMedal dm = new DecorationMedal();
			dm.setId(getPurpleHeartId());
			decorationMedals.add(dm);
			veteran.setDecorationMedalList(decorationMedals);
		}
		veteran.setSourceDate(record.getCreatedDate());
		
		RecordType recordType = new RecordType();
		recordType.setId(new Long(getText("SOURCE_ONLINE")));
		veteran.setRecordType(recordType);
		veteran.setContactable(new Integer(1));
		veteran.setVaMedEnrolled(new Boolean(false));
		veteran.setVaCurrent(new Integer(0));	// non-current
		veteran.setVerified(new Integer(0));	// non-verified
		veteran.setActive(new Integer(1));
		User user = (User)request.getSession().getAttribute(getText("USER"));
		veteran.setCreatedBy(user);
		veteran.setUpdatedBy(user);
		veteran.setInsertTimestamp(new Date());
		veteran.setUpdateTimestamp(new Date());

		// check if ssn does not already exists, create current veteran record.
		if (service.findVeteransBySsn(veteran.getSsn()).size() == 0) {
			veteran.setReviewed(new Integer(1));	// reviewed
			service.saveVeteran(veteran);
			
			if (record.getServiceStartDate() != null && record.getServiceEndDate() != null) {
				insertServicePeriod(veteran.getId(), record.getServiceBranchId(), record.getDischargeTypeId(), record.getServiceStartDate(), record.getServiceEndDate());
			}
			
			// create current veteran
			Veteran currentVeteran = new Veteran();
			BeanUtils.copyProperties(currentVeteran, veteran);
							
			currentVeteran.setId(null);
			currentVeteran.setVaCurrent(new Integer(1));	// make it current veteran
			
			veteran.setDecorationMedalList(decorationMedals);	// need to reset; otherwise got an error: Found shared references to a collection: gov.utah.va.vts.model.Veteran.decorationMedalList 
			service.saveVeteran(currentVeteran);
			
			if (record.getServiceStartDate() != null && record.getServiceEndDate() != null) {
				insertServicePeriod(currentVeteran.getId(), record.getServiceBranchId(), record.getDischargeTypeId(), record.getServiceStartDate(), record.getServiceEndDate());
			}
		} else {
			veteran.setReviewed(new Integer(0));	// un-reviewed
			service.saveVeteran(veteran);
			
			if (record.getServiceStartDate() != null && record.getServiceEndDate() != null) {
				insertServicePeriod(veteran.getId(), record.getServiceBranchId(), record.getDischargeTypeId(), record.getServiceStartDate(), record.getServiceEndDate());
			}
		}
		
		// 2. update transfer date to today so it will not be processed again next time.
		record.setTransferDate(new Date());
		service.saveRegistration(record);
	}
	
	private Long getPurpleHeartId() {
		Long retVal = null;
		List<DecorationMedal> decorationMedals = service.findAllDecorationMedalsActive();
		for (DecorationMedal decorationMedal : decorationMedals) {
			if ("Purple Heart".equals(decorationMedal.getName())) {
				retVal = decorationMedal.getId();
				break;
			}
		}
		
		return retVal;
	}
	
	private void insertServicePeriod(Long veteranId, Long serviceBranchId, Long dischargeTypeId, Date serviceStartDate, Date serviceEndDate) throws Exception {
		
		VeteranServicePeriod veteranServicePeriod = new VeteranServicePeriod();
		veteranServicePeriod.setStartDate(serviceStartDate);
		veteranServicePeriod.setEndDate(serviceEndDate);
		
		if (serviceBranchId != null) {
			ServiceBranch sb = new ServiceBranch();
			sb.setId(serviceBranchId);
			veteranServicePeriod.setServiceBranch(sb);			
		}
		
		if (dischargeTypeId != null) {
			DischargeType dc = new DischargeType();
			dc.setId(dischargeTypeId);
			veteranServicePeriod.setDischargeType(dc);
		}
				
		veteranServicePeriod.setActive(new Integer(1));
		veteranServicePeriod.setInsertTimestamp(new Date());
		veteranServicePeriod.setUpdateTimestamp(new Date());
		
		Veteran v = new Veteran();
		v.setId(veteranId);
		veteranServicePeriod.setVeteran(v);
		
		if (veteranServicePeriod.getStartDate() != null && veteranServicePeriod.getEndDate() != null) {
			Date startDate = veteranServicePeriod.getStartDate();
			Date endDate = veteranServicePeriod.getEndDate();
			
			List<ServiceEra> serviceEras = getServiceEras(startDate, endDate); 
			if (serviceEras.size() == 0) {
				veteranServicePeriod.setServiceEra(null);
				service.saveVeteranServicePeriod(veteranServicePeriod);
			} else { // redmine 14795
				for (int i = 0; i < serviceEras.size(); i++ ) {
					ServiceEra serviceEra = serviceEras.get(i);
					ServiceEra se = new ServiceEra();
					VeteranServicePeriod newVsp = new VeteranServicePeriod();
					BeanUtils.copyProperties(newVsp, veteranServicePeriod);

					se.setId(serviceEra.getId());
					newVsp.setServiceEra(se);
					
					newVsp.setStartDate(startDate);
					
					if (i == serviceEras.size() - 1) {
						newVsp.setEndDate(endDate);
					} else {
						newVsp.setEndDate(serviceEra.getEndDate());
						startDate = Util.addDaysToDate(1, serviceEra.getEndDate());
					}
					
					service.saveVeteranServicePeriod(newVsp);					
				}
			}
		} else {
			veteranServicePeriod.setServiceEra(null);
			service.saveVeteranServicePeriod(veteranServicePeriod);
		}

	}
	
	private List<ServiceEra> getServiceEras(Date startDate, Date endDate) {
		
		List<ServiceEra> serviceEras = service.findAllServiceErasActive();
		ArrayList<ServiceEra> retVal = new ArrayList<ServiceEra>();
		for (ServiceEra serviceEra : serviceEras) {
			if (Util.isDateInRange(startDate, endDate, serviceEra.getStartDate(), serviceEra.getEndDate())) {
				retVal.add(serviceEra);
			}
		}
		
		return retVal;
	}

}
