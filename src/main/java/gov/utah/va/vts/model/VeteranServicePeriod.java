package gov.utah.va.vts.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * VeteranServicePeriod entity class
 * 
 * @author HNGUYEN
 *
 */
@Entity
@Table(name="VETERAN_SERVICE_PERIOD")
public class VeteranServicePeriod extends BaseEntityVTS {

	private static final long serialVersionUID = 1L;

	private Date startDate;
	@Transient
	private Integer monthFrom;
	@Transient
	private Integer dayFrom;
	@Transient
	private Integer yearFrom;
	@Transient
	private String startDateStr;
	
	private Date endDate;
	@Transient
	private Integer monthTo;
	@Transient
	private Integer dayTo;
	@Transient
	private Integer yearTo;
	@Transient
	private String endDateStr;
	
	@ManyToOne
	@JoinColumn(name="VETERAN_ID")
	private Veteran veteran;
	
	@ManyToOne
	@JoinColumn(name="SERVICE_BRANCH_ID")
	private ServiceBranch serviceBranch;
	@Transient
	private Long serviceBranchId;
	@Transient
	private String serviceBranchName;
	
	@ManyToOne
	@JoinColumn(name="SERVICE_ERA_ID")
	private ServiceEra serviceEra;
	@Transient
	private Long serviceEraId;
	@Transient
	private String serviceEraName;
	
	@ManyToOne
	@JoinColumn(name="DISCHARGE_TYPE_ID")
	private DischargeType dischargeType;
	@Transient
	private Long dischargeTypeId;
	@Transient
	private String dischargeTypeName;
	
	@ManyToOne
	@JoinColumn(name="COMBAT_ZONE_ID")
	private CombatZone combatZone;
	@Transient
	private Long combatZoneId;
	@Transient
	private String combatZoneName;
		
	@Transient
	private Long idService;
	@Transient
	private Integer activeService;
	@Transient
	private String insertTimestampService;
	
	/* --- getters/setters --- */
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Veteran getVeteran() {
		return veteran;
	}
	public void setVeteran(Veteran veteran) {
		this.veteran = veteran;
	}
	public ServiceBranch getServiceBranch() {
		return serviceBranch;
	}
	public void setServiceBranch(ServiceBranch serviceBranch) {
		this.serviceBranch = serviceBranch;
	}
	public ServiceEra getServiceEra() {
		return serviceEra;
	}
	public void setServiceEra(ServiceEra serviceEra) {
		this.serviceEra = serviceEra;
	}
	public DischargeType getDischargeType() {
		return dischargeType;
	}
	public void setDischargeType(DischargeType dischargeType) {
		this.dischargeType = dischargeType;
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
	public Long getServiceEraId() {
		return serviceEraId;
	}
	public void setServiceEraId(Long serviceEraId) {
		this.serviceEraId = serviceEraId;
	}
	public Long getDischargeTypeId() {
		return dischargeTypeId;
	}
	public void setDischargeTypeId(Long dischargeTypeId) {
		this.dischargeTypeId = dischargeTypeId;
	}

	public CombatZone getCombatZone() {
		return combatZone;
	}
	public void setCombatZone(CombatZone combatZone) {
		this.combatZone = combatZone;
	}
	public Long getCombatZoneId() {
		return combatZoneId;
	}
	public void setCombatZoneId(Long combatZoneId) {
		this.combatZoneId = combatZoneId;
	}
	public String getCombatZoneName() {
		return combatZoneName;
	}
	public void setCombatZoneName(String combatZoneName) {
		this.combatZoneName = combatZoneName;
	}
	public String getStartDateStr() {
		return startDateStr;
	}
	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}
	public String getEndDateStr() {
		return endDateStr;
	}
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}
	public Long getServiceBranchId() {
		return serviceBranchId;
	}
	public void setServiceBranchId(Long serviceBranchId) {
		this.serviceBranchId = serviceBranchId;
	}
	public String getServiceBranchName() {
		return serviceBranchName;
	}
	public void setServiceBranchName(String serviceBranchName) {
		this.serviceBranchName = serviceBranchName;
	}
	public String getServiceEraName() {
		return serviceEraName;
	}
	public void setServiceEraName(String serviceEraName) {
		this.serviceEraName = serviceEraName;
	}
	public String getDischargeTypeName() {
		return dischargeTypeName;
	}
	public void setDischargeTypeName(String dischargeTypeName) {
		this.dischargeTypeName = dischargeTypeName;
	}
	public Long getIdService() {
		return idService;
	}
	public void setIdService(Long idService) {
		this.idService = idService;
	}
	public Integer getActiveService() {
		return activeService;
	}
	public void setActiveService(Integer activeService) {
		this.activeService = activeService;
	}
	public String getInsertTimestampService() {
		return insertTimestampService;
	}
	public void setInsertTimestampService(String insertTimestampService) {
		this.insertTimestampService = insertTimestampService;
	}
	
}
