  package gov.utah.va.vts.ajax;

import gov.utah.dts.det.util.Util;
import gov.utah.va.vts.model.CombatZone;
import gov.utah.va.vts.model.DischargeType;
import gov.utah.va.vts.model.ServiceBranch;
import gov.utah.va.vts.model.ServiceEra;
import gov.utah.va.vts.model.Veteran;
import gov.utah.va.vts.model.VeteranServicePeriod;
import gov.utah.va.vts.service.VtsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Ajax class for calls from veteran.jsp, veteran service periods section.
 * 
 * @author HNGUYEN
 *
 */
public class VeteranServiceAjax {
	protected Log logger = org.apache.commons.logging.LogFactory.getLog(this.getClass());
	
	@Autowired
	VtsService service;
	
	public List<VeteranServicePeriod> getVeteranServices(Long veteranId) throws Exception {
		
		logger.debug("entering getVeteranService ...");		
		
		List<VeteranServicePeriod> veteranServices = service.findVeteranServicePeriods(veteranId); 
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		for (VeteranServicePeriod veteranService : veteranServices) {	
			if (veteranService.getServiceBranch().getName() == null) {	// Hibernate or DWR bug???
				veteranService.setServiceBranch(service.findServiceBranchById(veteranService.getServiceBranchId()));
				if (veteranService.getServiceEra() != null) veteranService.setServiceEra(service.findServiceEraById(veteranService.getServiceEra().getId()));
				if (veteranService.getDischargeTypeId() != null) veteranService.setDischargeType(service.findDischargeTypeById(veteranService.getDischargeTypeId()));
				if (veteranService.getCombatZoneId() != null) veteranService.setCombatZone(service.findCombatZoneById(veteranService.getCombatZoneId()));
			}
			
			veteranService.setServiceBranchId(veteranService.getServiceBranch().getId());
			veteranService.setServiceBranchName(veteranService.getServiceBranch().getName());
			if (veteranService.getServiceEra() != null) veteranService.setServiceEraName(veteranService.getServiceEra().getName());
			
			if (veteranService.getDischargeType() != null) veteranService.setDischargeTypeId(veteranService.getDischargeType().getId());
			if (veteranService.getDischargeType() != null) veteranService.setDischargeTypeName(veteranService.getDischargeType().getName());

			if (veteranService.getCombatZone() != null) veteranService.setCombatZoneId(veteranService.getCombatZone().getId());
			if (veteranService.getCombatZone() != null) veteranService.setCombatZoneName(veteranService.getCombatZone().getName());
			
			if (veteranService.getStartDate() != null) {
				veteranService.setStartDateStr(sdf.format(veteranService.getStartDate()));
				String[] startDate = veteranService.getStartDateStr().split("/");
				veteranService.setMonthFrom(new Integer(startDate[0]));
				veteranService.setDayFrom(new Integer(startDate[1]));
				veteranService.setYearFrom(new Integer(startDate[2]));
			}
			
			if (veteranService.getEndDate() != null) {
				veteranService.setEndDateStr(sdf.format(veteranService.getEndDate()));
				String[] endDate = veteranService.getEndDateStr().split("/");
				veteranService.setMonthTo(new Integer(endDate[0]));
				veteranService.setDayTo(new Integer(endDate[1]));
				veteranService.setYearTo(new Integer(endDate[2]));				
			}
						
			veteranService.setIdService(veteranService.getId());
			veteranService.setActiveService(veteranService.getActive());
		}
		
		return veteranServices;
	}
	
	public void saveVeteranService(Long veteranId, VeteranServicePeriod veteranServicePeriod) throws Exception {
		
		logger.debug("entering saveVeteranService ...");
		
		veteranServicePeriod.setId(veteranServicePeriod.getIdService());
		veteranServicePeriod.setActive(veteranServicePeriod.getActiveService());

		Date startDate = null;
		try {
			startDate = Util.convertToDate(veteranServicePeriod.getMonthFrom().toString(), veteranServicePeriod.getDayFrom().toString(), veteranServicePeriod.getYearFrom().toString()); 
			veteranServicePeriod.setStartDate(startDate);
		} catch (Exception e) {
			veteranServicePeriod.setStartDate(null);
		}
		
		Date endDate = null;
		try {
			endDate = Util.convertToDate(veteranServicePeriod.getMonthTo().toString(), veteranServicePeriod.getDayTo().toString(), veteranServicePeriod.getYearTo().toString());
			veteranServicePeriod.setEndDate(endDate);
		} catch (Exception e) {
			veteranServicePeriod.setEndDate(null);
		}
		
		ServiceBranch sb = new ServiceBranch();
		sb.setId(veteranServicePeriod.getServiceBranchId());
		veteranServicePeriod.setServiceBranch(sb);			
				
		if (veteranServicePeriod.getDischargeTypeId() != null) {
			DischargeType dc = new DischargeType();
			dc.setId(veteranServicePeriod.getDischargeTypeId());
			veteranServicePeriod.setDischargeType(dc);
		} else {
			veteranServicePeriod.setDischargeType(null);
		}
		
		if (veteranServicePeriod.getCombatZoneId() != null) {
			CombatZone cz = new CombatZone();
			cz.setId(veteranServicePeriod.getCombatZoneId());
			veteranServicePeriod.setCombatZone(cz);
		} else {
			veteranServicePeriod.setCombatZone(null);
		}
				
		if (veteranServicePeriod.getId() == null) {	// insert
			veteranServicePeriod.setActive(new Integer(1));
			veteranServicePeriod.setInsertTimestamp(new Date());
			veteranServicePeriod.setUpdateTimestamp(new Date());
		} else {	// update
			veteranServicePeriod.setUpdateTimestamp(new Date());
			
			// insert time stamp fix
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			String insertTimestampService = veteranServicePeriod.getInsertTimestampService();
			veteranServicePeriod.setInsertTimestamp(sdf.parse(insertTimestampService));
		}
		
		Veteran v = new Veteran();
		v.setId(veteranId);
		veteranServicePeriod.setVeteran(v);
		
		// and finally before saving - service eras, it may span more than 1 eras
		if (startDate != null && endDate != null) {
			List<ServiceEra> serviceEras = getServiceEras(startDate, endDate); 
			if (serviceEras.size() == 0) {
				veteranServicePeriod.setServiceEra(null);
				service.saveVeteranServicePeriod(veteranServicePeriod);
			} else { // redmine 14795
				if (veteranServicePeriod.getId() != null) { // update service redmine 14795 comment #2
					deleteVeteranService(veteranServicePeriod.getId());
					veteranServicePeriod.setId(null);
				}
				
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
	
	public void copyVeteranService(Long servicePeriodId, Long currentVeteranId) throws Exception {
		
		logger.debug("entering copyVeteranService ...");
		
		VeteranServicePeriod vsp = service.findVeteranServicePeriodById(servicePeriodId);
				
		VeteranServicePeriod newVsp = new VeteranServicePeriod ();
		BeanUtils.copyProperties(newVsp, vsp);
		
		newVsp.setId(null);
		
		Veteran v = new Veteran();
		v.setId(currentVeteranId);
		newVsp.setVeteran(v);
		newVsp.setUpdateTimestamp(new Date());
		
		service.saveVeteranServicePeriod(newVsp);
	}
	
	public void deleteVeteranService(Long id) throws Exception {
		
		logger.debug("entering deleteVeteranService ...");
		VeteranServicePeriod v = new VeteranServicePeriod();
		v.setId(id);
		service.deleteVeteranServicePeriod(v);
		service.flush();
	}
	
	public String checkDuplicateSsn(String ssn) throws Exception {
		
		logger.debug("entering checkDuplicateSsn ...");
		
		List<Veteran> veterans = service.findVeteransBySsn(ssn);
		if (veterans.size() > 0) {
			return "Yes";
		} else {
			return "No";
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
