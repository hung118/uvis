package gov.utah.va.vts.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.utah.dts.det.util.Util;
import gov.utah.dts.det.util.Validate;
import gov.utah.va.vts.model.DecorationMedal;
import gov.utah.va.vts.model.DischargeType;
import gov.utah.va.vts.model.NameValue;
import gov.utah.va.vts.model.RecordType;
import gov.utah.va.vts.model.ServiceBranch;
import gov.utah.va.vts.model.ServiceEra;
import gov.utah.va.vts.model.User;
import gov.utah.va.vts.model.Veteran;
import gov.utah.va.vts.model.VeteranServicePeriod;
import gov.utah.va.vts.service.VtsService;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * On-line veteran registration action class.
 * Moved to vtsreg app.
 * @author hnguyen
 *
 */
public class RegisterAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;

	@Autowired
	VtsService service;
	
	private Veteran veteran;
	private Long serviceBranchId;
	private Integer monthFrom;
	private Integer dayFrom;
	private Integer yearFrom;
	private Date dateFrom;
	private Integer monthTo;
	private Integer dayTo;
	private Integer yearTo;
	private Date dateTo;
	private String comatZone;
	private Long dischargeTypeId;
	
	public String execute() throws Exception {
		logger.debug("Entering execute ...");
		
		// for user friendly error report when database is disconnected or not sync.
		super.getEthnicityList();
		
		return INPUT;
	}
	
	public String save() throws Exception {
		
		logger.debug("Entering execute ...");

		// redmine 15899
		veteran.setFirstName(Util.toUpperCaseNull(veteran.getFirstName()));
		veteran.setLastName(Util.toUpperCaseNull(veteran.getLastName()));
		veteran.setMiddleName(Util.toUpperCaseNull(veteran.getMiddleName()));
		
		RecordType recordType = new RecordType();
		recordType.setId(7L);	// Online registration record type (source)
		veteran.setRecordType(recordType);
		veteran.setContactable(new Integer(1));
		veteran.setVaMedEnrolled(new Boolean(false));
		//veteran.setShareFederalVa(new Boolean(false));
		veteran.setSourceDate(new Date());
		veteran.setVaCurrent(new Integer(0));	// non-current
		veteran.setVerified(new Integer(0));	// non-verified
		veteran.setActive(new Integer(1));
		User user = new User();
		user.setId(new Long(getText("SYSTEM_USER")));
		veteran.setCreatedBy(user);
		veteran.setUpdatedBy(user);
		veteran.setInsertTimestamp(new Date());
		veteran.setUpdateTimestamp(new Date());

		veteran.setSsn(veteran.getSsn1()+ "-" + veteran.getSsn2() + "-" + veteran.getSsn3());
		
		if (veteran.getEthnicity().getId() == null) veteran.setEthnicity(null);
		
		veteran.setPrimaryPhone(veteran.getPrimaryPhone1() + "-" + veteran.getPrimaryPhone2() + "-" + veteran.getPrimaryPhone3());
		if (veteran.getPrimaryPhone().length() != 12) veteran.setPrimaryPhone(null);
		
		try {
			veteran.setDateOfBirth(Util.convertToDate(veteran.getMonth().toString(), veteran.getDay().toString(), veteran.getYear().toString()));
		} catch(NullPointerException e) {
			veteran.setDateOfBirth(null);
		}
		
		// decoration and medal
		List<DecorationMedal> decorationMedals = new ArrayList<DecorationMedal>();
		for (Long decorationMedalLong : veteran.getDecorationMedals()) {
			DecorationMedal dm = new DecorationMedal();
			dm.setId(decorationMedalLong);
			decorationMedals.add(dm);
		}
		veteran.setDecorationMedalList(decorationMedals);
		
		// check if ssn does not already exists, create current veteran record.
		if (service.findVeteransBySsn(veteran.getSsn()).size() == 0) {
			veteran.setReviewed(new Integer(1));	// reviewed
			service.saveVeteran(veteran);
			
			if (getDateFrom() != null && getDateTo() != null) {
				insertServicePeriod(veteran.getId());
			}
			
			// create current veteran
			Veteran currentVeteran = new Veteran();
			BeanUtils.copyProperties(currentVeteran, veteran);
							
			currentVeteran.setId(null);
			currentVeteran.setVaCurrent(new Integer(1));	// make it current veteran
			
			veteran.setDecorationMedalList(decorationMedals);	// need to reset; otherwise got an error: Found shared references to a collection: gov.utah.va.vts.model.Veteran.decorationMedalList 
			service.saveVeteran(currentVeteran);
			
			if (getDateFrom() != null && getDateTo() != null) {
				insertServicePeriod(currentVeteran.getId());
			}
		} else {
			veteran.setReviewed(new Integer(0));	// un-reviewed
			service.saveVeteran(veteran);
			
			if (getDateFrom() != null && getDateTo() != null) {
				insertServicePeriod(veteran.getId());
			}
		}
		
		return getText("CONFIRM_REGISTER");
	}

	public void validate() {
		
		if (veteran != null) {
			if (Validate.isEmpty(veteran.getSsn1()) && Validate.isEmpty(veteran.getSsn2()) && Validate.isEmpty(veteran.getSsn3())) {
				addActionError(getText("error.required", Util.getStringArray(getText("veteran.ssn"))) + " " + getText("error.ssn"));
			} else {
				if (!Validate.isSSNValid(veteran.getSsn1() + veteran.getSsn2() + veteran.getSsn3()))
					addActionError(getText("error.notValid", Util.getStringArray(getText("veteran.ssn"))) + " " + getText("error.ssn"));
			}
			
			if (veteran.getMonth() == null && veteran.getDay() == null && veteran.getYear() == null) {
				// pass
			} else {
				if (veteran.getMonth() == null || veteran.getDay() == null || veteran.getYear() == null) {
					addActionError(getText("error.notValidDate", Util.getStringArray(getText("veteran.dob"))));
				} else {
					try {
						veteran.setDateOfBirth(Util.convertToDate(veteran.getMonth().toString(), veteran.getDay().toString(), veteran.getYear().toString()));
					} catch (Exception e) {}
				}		
			}
			
			if (!Validate.isEmpty(veteran.getZip()) && !Validate.isZipValid(veteran.getZip()))
				addActionError(getText("error.notValid", Util.getStringArray(getText("veteran.streetZip"))));
			
			if (!Validate.isPhoneNumberValid(veteran.getPrimaryPhone1() + veteran.getPrimaryPhone2() + veteran.getPrimaryPhone3()))
				addActionError(getText("error.notValidPhone", Util.getStringArray(getText("veteran.primaryPhone"))));
			
			if (Validate.isEmpty(veteran.getEmail())) {
				// pass
			} else {
				if (!Validate.isEmailValid(veteran.getEmail())) addActionError(getText("error.notValid", Util.getStringArray(getText("veteran.email"))));
			}
			
			if (getMonthFrom() == null && getDayFrom() == null && getYearFrom() == null) {
				// pass
			} else {
				if (getMonthFrom() == null || getDayFrom() == null || getYearFrom() == null) {
					addActionError(getText("error.notValidDate", Util.getStringArray(getText("veteran.serviceDateFrom"))));
				} else {
					try {
						setDateFrom(Util.convertToDate(getMonthFrom().toString(), getDayFrom().toString(), getYearFrom().toString()));
					} catch (Exception e) {}
				}				
			}
			
			if (getMonthTo() == null && getDayTo() == null && getYearTo() == null) {
				// pass
			} else {
				if (getMonthTo() == null || getDayTo() == null || getYearTo() == null) {
					addActionError(getText("error.notValidDate", Util.getStringArray(getText("veteran.serviceDateTo"))));
				} else {
					try {
						setDateTo(Util.convertToDate(getMonthTo().toString(), getDayTo().toString(), getYearTo().toString()));
					} catch (Exception e) {}
				}				
			}
						
			if (getDateFrom() != null && getDateTo() != null) {
				if (Validate.compareDate(getDateTo(), getDateFrom()) == -1) 
					addActionError(getText("error.toBeforeFrom"));
			}
		}
	}
	
	public Veteran getVeteran() {
		return veteran;
	}

	public void setVeteran(Veteran veteran) {
		this.veteran = veteran;
	}

	public VtsService getService() {
		return service;
	}

	public void setService(VtsService service) {
		this.service = service;
	}

	public Long getServiceBranchId() {
		return serviceBranchId;
	}

	public void setServiceBranchId(Long serviceBranchId) {
		this.serviceBranchId = serviceBranchId;
	}

	public Integer getMonthFrom() {
		return monthFrom;
	}

	public void setMonthFrom(Integer monthFrom) {
		this.monthFrom = monthFrom;
	}

	public Integer getDayFrom() {
		return dayFrom;
	}

	public void setDayFrom(Integer dayFrom) {
		this.dayFrom = dayFrom;
	}

	public Integer getYearFrom() {
		return yearFrom;
	}

	public void setYearFrom(Integer yearFrom) {
		this.yearFrom = yearFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public Integer getMonthTo() {
		return monthTo;
	}

	public void setMonthTo(Integer monthTo) {
		this.monthTo = monthTo;
	}

	public Integer getDayTo() {
		return dayTo;
	}

	public void setDayTo(Integer dayTo) {
		this.dayTo = dayTo;
	}

	public Integer getYearTo() {
		return yearTo;
	}

	public void setYearTo(Integer yearTo) {
		this.yearTo = yearTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public String getComatZone() {
		return comatZone;
	}

	public void setComatZone(String comatZone) {
		this.comatZone = comatZone;
	}

	public Long getDischargeTypeId() {
		return dischargeTypeId;
	}

	public void setDischargeTypeId(Long dischargeTypeId) {
		this.dischargeTypeId = dischargeTypeId;
	}
	
	/**
	 * Override super method to show Purple Heart only. Redmine 14336
	 */
    public List<NameValue> getDecorationMedalList() throws Exception {
    	
    	logger.debug("getDecorationMedalList 2 ...");
    	ArrayList<NameValue> decorationMedalList = new ArrayList<NameValue>();
    	List<DecorationMedal> decorationMedals = service.findAllDecorationMedalsActive();
    	for (DecorationMedal decorationMedal : decorationMedals) {
    		if ("Purple Heart".equals(decorationMedal.getName())) {
        		NameValue nv = new NameValue();
        		nv.setId(decorationMedal.getId());
        		nv.setValue(decorationMedal.getName());
        		decorationMedalList.add(nv);    			
    		}
		}
    	
    	return decorationMedalList;
    }
	
	private void insertServicePeriod(Long veteranId) throws Exception {
		
		VeteranServicePeriod veteranServicePeriod = new VeteranServicePeriod();
		veteranServicePeriod.setStartDate(getDateFrom());
		veteranServicePeriod.setEndDate(getDateTo());
		
		if (getServiceBranchId() != null) {
			ServiceBranch sb = new ServiceBranch();
			sb.setId(getServiceBranchId());
			veteranServicePeriod.setServiceBranch(sb);			
		}
		
		if (getDischargeTypeId() != null) {
			DischargeType dc = new DischargeType();
			dc.setId(getDischargeTypeId());
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
	
	/**
	 * Gets list of service eras specified by service start date and end date.
	 * @param startDate
	 * @param endDate
	 * @return
	 */
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
