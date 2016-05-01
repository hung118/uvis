package gov.utah.va.vts.action;

import gov.utah.va.vts.model.BenefitType;
import gov.utah.va.vts.model.CombatZone;
import gov.utah.va.vts.model.DecorationMedal;
import gov.utah.va.vts.model.DischargeType;
import gov.utah.va.vts.model.DocType;
import gov.utah.va.vts.model.Ethnicity;
import gov.utah.va.vts.model.NameValue;
import gov.utah.va.vts.model.RecordType;
import gov.utah.va.vts.model.Relation;
import gov.utah.va.vts.model.ServiceBranch;
import gov.utah.va.vts.model.ServiceEra;
import gov.utah.va.vts.model.Veteran;
import gov.utah.va.vts.service.VtsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;

/**
 * Base action class.
 * 
 * @author hnguyen
 *
 */
public class BaseActionSupport extends ActionSupport implements ServletRequestAware, ServletResponseAware {

	/**
	 * default serial id
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * logger for logging support 
	 */
	protected Log logger = org.apache.commons.logging.LogFactory.getLog(this.getClass());
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	@Autowired
	protected VtsService service;

	private Map<Integer, Integer> dayMap;
	private Map<Integer, Integer> monthMap;
	private Map<Integer, Integer> yearMap;
	private List<NameValue> ethnicityList;
	private List<NameValue> serviceBranchList;
	private List<NameValue> serviceEraList;
	private List<NameValue> dischargeTypeList;
	private List<NameValue> combatZoneList;
	private List<NameValue> benefitTypeList;
	private List<NameValue> docTypeList;
	private Map<String, String> stateMap;
	
	private String firstDd214 = "";	// used for attachment first checked doc type radio button.
	
	public void traceError(Exception e) {
		logger.error("*********  EXCEPTION CAUSE: " + e.getCause());
		logger.error("******** EXCEPTION MESSAGE: " + e.getMessage());
		StackTraceElement[] stackTrace = e.getStackTrace();
		for (int i = 0; i < stackTrace.length; ++i) {
			logger.error("*********** STACK TRACE[" + i + "]:"
					+ stackTrace[i].getFileName() + "."
					+ stackTrace[i].getClassName() + "."
					+ stackTrace[i].getMethodName() + " - "
					+ stackTrace[i].getLineNumber());
		}
	}
	    
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setStateMap(Map<String, String> stateMap) {
		this.stateMap = stateMap;
	}

	public Map<String, String> getStateMap() {
		if (this.stateMap == null) {
			createStateMap();
		}
		return this.stateMap;
	}
	
	private void createStateMap() {
        //A Map containing the names of states and abbreviations
        logger.debug("createStateMap ...");
        Map<String, String> abbrevMap = new TreeMap<String, String>();

        abbrevMap.put("AL", "Alabama");
        abbrevMap.put("AK", "Alaska");
        abbrevMap.put("AZ", "Arizona");
        abbrevMap.put("AR", "Arkansas");
        abbrevMap.put("CA", "California");
        abbrevMap.put("CO", "Colorado");
        abbrevMap.put("CT", "Connecticut");
        abbrevMap.put("DE", "Delaware");
        abbrevMap.put("FL", "Florida");
        abbrevMap.put("GA", "Georgia");
        abbrevMap.put("HI", "Hawaii");
        abbrevMap.put("ID", "Idaho");
        abbrevMap.put("IL", "Illinois");
        abbrevMap.put("IN", "Indiana");
        abbrevMap.put("IA", "Iowa");
        abbrevMap.put("KS", "Kansas");
        abbrevMap.put("KY", "Kentucky");
        abbrevMap.put("LA", "Louisiana");
        abbrevMap.put("ME", "Maine");
        abbrevMap.put("MD", "Maryland");
        abbrevMap.put("MA", "Massachusetts");
        abbrevMap.put("MI", "Michigan");
        abbrevMap.put("MN", "Minnesota");
        abbrevMap.put("MS", "Mississippi");
        abbrevMap.put("MO", "Missouri");
        abbrevMap.put("MT", "Montana");
        abbrevMap.put("NE", "Nebraska");
        abbrevMap.put("NV", "Nevada");
        abbrevMap.put("NH", "New Hampshire");
        abbrevMap.put("NJ", "New Jersey");
        abbrevMap.put("NM", "New Mexico");
        abbrevMap.put("NY", "New York");
        abbrevMap.put("NC", "North Carolina");
        abbrevMap.put("ND", "North Dakota");
        abbrevMap.put("OH", "Ohio");
        abbrevMap.put("OK", "Oklahoma");
        abbrevMap.put("OR", "Oregon");
        abbrevMap.put("PA", "Pennsylvania");
        abbrevMap.put("RI", "Rhode Island");
        abbrevMap.put("SC", "South Carolina");
        abbrevMap.put("SD", "South Dakota");
        abbrevMap.put("TN", "Tennessee");
        abbrevMap.put("TX", "Texas");
        abbrevMap.put("UT", "Utah");
        abbrevMap.put("VT", "Vermont");
        abbrevMap.put("VA", "Virginia");
        abbrevMap.put("WA", "Washington");
        abbrevMap.put("WV", "West Virginia");
        abbrevMap.put("WI", "Wisconsin");
        abbrevMap.put("WY", "Wyoming");
        setStateMap(abbrevMap);
    }
    
	public String cancel() {
		return getText("DASHBOARD");
	}
	
	public List<NameValue> getActiveList() {
		
		ArrayList<NameValue> activeList = new ArrayList<NameValue>();
		NameValue nv = new NameValue();
		nv.setActive(new Integer(0));
		nv.setValue("Inactive");
		activeList.add(nv);

		NameValue nv2 = new NameValue();
		nv2.setActive(new Integer(1));
		nv2.setValue("Active");
		activeList.add(nv2);

		return activeList;
	}

    public void setDayMap(Map<Integer, Integer> dayMap) {
		this.dayMap = dayMap;
	}

	public Map<Integer, Integer> getDayMap() {
		if (this.dayMap == null) {
			createDayMap();
		}
		return this.dayMap;
	}

	public void setMonthMap(Map<Integer, Integer> monthMap) {
		this.monthMap = monthMap;
	}

	public Map<Integer, Integer> getMonthMap() {
		if (this.monthMap == null) {
			createMonthMap();
		}
		return this.monthMap;
	}

	public void setYearMap(Map<Integer, Integer> yearMap) {
		this.yearMap = yearMap;
	}

	public Map<Integer, Integer> getYearMap() {
		if (this.yearMap == null) {
			createYearMap();
		}
		return this.yearMap;
	}
	
    private void createDayMap() {
    	
        logger.debug("createDayMap ...");
        Map<Integer, Integer> days = new TreeMap<Integer, Integer>();
        
        for (int i = 1; i <= 31; i++) {
        	days.put(new Integer(i), new Integer(i));
        }
        setDayMap(days);
    }
    
    private void createMonthMap() {
    	
    	logger.debug("createMonthMap ...");
    	Map<Integer, Integer> months = new TreeMap<Integer, Integer>();
    	
    	for (int i = 1; i <= 12; i++) {
    		months.put(new Integer(i), new Integer(i));
    	}
    	setMonthMap(months);
    }
    
    private void createYearMap() {
    	
    	logger.debug("createYearMap ...");
    	Map<Integer, Integer> years = new TreeMap<Integer, Integer>();
    	
    	for (int i = 1900; i <= 2040; i++) {
    		years.put(new Integer(i), new Integer(i));
    	}
    	setYearMap(years);
    }
    
    public List<NameValue> getEthnicityList() {
    	
    	logger.debug("getEthnicityList ...");
    	if (ethnicityList == null) {
    		List<Ethnicity> ethnicities = service.findAllEthnicitiesActive();
    		ethnicityList = new ArrayList<NameValue>();
    		NameValue other = new NameValue();
    		for (Ethnicity ethnicity : ethnicities) {
    			if ("Other".equals(ethnicity.getName())) {	// put Other at the end - redmin3 14336
    				other.setId(ethnicity.getId());
    				other.setValue(ethnicity.getName());    				
    			} else {
        			NameValue nv = new NameValue();
    				nv.setId(ethnicity.getId());
    				nv.setValue(ethnicity.getName());
    				ethnicityList.add(nv);    				    				
    			}
			}
    		
    		ethnicityList.add(other);
    	}
    	
    	return ethnicityList;
    }
    
    public List<NameValue> getServiceBranchList() {
    	
    	logger.debug("getServiceBranchList ...");
    	if (serviceBranchList == null) {
    		List<ServiceBranch> serviceBranches = service.findAllServiceBranchesActive();
    		serviceBranchList = new ArrayList<NameValue>();
    		for (ServiceBranch serviceBranch : serviceBranches) {
				NameValue nv = new NameValue();
				nv.setId(serviceBranch.getId());
				nv.setValue(serviceBranch.getName());
				serviceBranchList.add(nv);
			}
    	}
    	
    	return serviceBranchList;
    }
    
    public List<NameValue> getServiceEraList() {
    	
    	logger.debug("getServiceEraList ...");
    	if (serviceEraList == null) {
    		List<ServiceEra> serviceEras = service.findAllServiceErasActive();
    		serviceEraList = new ArrayList<NameValue>();
    		for (ServiceEra serviceEra : serviceEras) {
				NameValue nv = new NameValue();
				nv.setId(serviceEra.getId());
				nv.setValue(serviceEra.getName());
				serviceEraList.add(nv);
			}
    	}
    	
    	return serviceEraList;
    }
    
    public List<NameValue> getDischargeTypeList() {
    	
    	logger.debug("getDischargeTypeList ...");
    	if (dischargeTypeList == null) {
    		List<DischargeType> dischargeTypes = service.findAllDischargeTypesActive();
    		dischargeTypeList = new ArrayList<NameValue>();
    		for (DischargeType dischargeType : dischargeTypes) {
				NameValue nv = new NameValue();
				nv.setId(dischargeType.getId());
				nv.setValue(dischargeType.getName());
				dischargeTypeList.add(nv);
			}
    	}
    	
    	return dischargeTypeList;
    }
    
    public List<NameValue> getCombatZoneList() {
    	
    	logger.debug("getCombatZoneList ...");
    	if (combatZoneList == null) {
    		List<CombatZone> combatZones = service.findAllCombatZones();
    		combatZoneList = new ArrayList<NameValue>();
    		for (CombatZone combatZone : combatZones) {
    			NameValue nv = new NameValue();
    			nv.setId(combatZone.getId());
    			nv.setValue(combatZone.getName());
    			combatZoneList.add(nv);
    		}
    	}
    	
    	return combatZoneList;
    }
    
    public List<NameValue> getBenefitTypeList() {
    	
    	logger.debug("getBenefitTypeList ...");
    	if (benefitTypeList == null) {
    		List<BenefitType> benefitTypes = service.findAllBenefitTypesActive();
    		benefitTypeList = new ArrayList<NameValue>();
    		for (BenefitType benefitType : benefitTypes) {
				NameValue nv = new NameValue();
				nv.setId(benefitType.getId());
				nv.setValue(benefitType.getName());
				benefitTypeList.add(nv);
			}
    	}
    	
    	return benefitTypeList;
    }
    
    public List<NameValue> getDocTypeList() {
    	
    	logger.debug("getDocTypeList ...");
    	if (docTypeList == null) {
    		List<DocType> docTypes = service.findAllDocTypesActive();
    		docTypeList = new ArrayList<NameValue>();
    		for (DocType docType : docTypes) {
				NameValue nv = new NameValue();
				nv.setId(docType.getId());
				nv.setValue(docType.getName());
				docTypeList.add(nv);
			}
    	}
    	
    	return docTypeList;    	
    }
    
    public List<NameValue> getRecordTypeList() throws Exception {
    	
    	logger.debug("getRecordTypeList ...");
    	ArrayList<NameValue> recordTypeList = new ArrayList<NameValue>();
    	List<RecordType> recordTypes = service.findAllRecordTypesActive();
    	for (RecordType recordType : recordTypes) {
    		NameValue nv = new NameValue();
    		nv.setId(recordType.getId());
    		nv.setValue(recordType.getName());
    		recordTypeList.add(nv);
		}
    	
    	return recordTypeList;
    }
    
    public List<NameValue> getRelationList() throws Exception {
    	
    	logger.debug("getRelationList ...");
    	ArrayList<NameValue> relationList = new ArrayList<NameValue>();
    	List<Relation> relations = service.findAllRelationsActive();
    	for (Relation relation : relations) {
    		NameValue nv = new NameValue();
    		nv.setId(relation.getId());
    		nv.setValue(relation.getName());
    		relationList.add(nv);
		}
    	
    	return relationList;
    }

    public List<NameValue> getDecorationMedalList() throws Exception {
    	
    	logger.debug("getDecorationMedalList ...");
    	ArrayList<NameValue> decorationMedalList = new ArrayList<NameValue>();
    	List<DecorationMedal> decorationMedals = service.findAllDecorationMedalsActive();
    	for (DecorationMedal decorationMedal : decorationMedals) {
    		NameValue nv = new NameValue();
    		nv.setId(decorationMedal.getId());
    		nv.setValue(decorationMedal.getName());
    		decorationMedalList.add(nv);
		}
    	
    	return decorationMedalList;
    }
    
	public String getFirstDd214() {
		
		if ("".equals(firstDd214)) {
			for (NameValue nv : getDocTypeList()) {
				if (nv.getValue().contains(getText("DD214"))) {
					firstDd214 = nv.getId().toString();
					break;
				}
			}			
		}
		
		return firstDd214;
	}
	
	public void setFirstDd214(String firstDd214) {
		this.firstDd214 = firstDd214;
	}
	
	public DocType getFirstDd214DocType() {
		for (NameValue nv : getDocTypeList()) {
			if (nv.getValue().contains(getText("DD214"))) {
				DocType docType = new DocType();
				docType.setId(nv.getId());
				return docType;
			}
		}
		
		return null;
	}
	
	public Long getPurpleHeartId() {
		
		List<DecorationMedal> dms = service.findAllDecorationMedals();
		Long ret = null;
		for (DecorationMedal dm : dms) {
			if ("Purple Heart".equalsIgnoreCase(dm.getName())){
				ret = dm.getId();
				break;
			}
		}
		
		return ret;
	}
	
	public void setRuralCrosswalk(Veteran veteran) {
		List<NameValue> rurals = service.findUtahRurals();
		for (NameValue rural : rurals) {
			if (!veteran.getZip().isEmpty() && veteran.getZip().length() >= 5 && rural.getName().equals(veteran.getZip().substring(0, 5))) {
				veteran.setRural(rural.getValue());
				break;
			}
		}
	}
    
}
