package gov.utah.va.vts.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * ReportProperty entity class
 * @author hnguyen
 *
 */
@Entity
@Table(name="REPORT_PROPERTY")
public class ReportProperty extends BaseEntityVTS {

	private static final long serialVersionUID = 1L;
	
	private String source;
	@Transient
	private Long[] sourceArray;
	
	private Boolean vaEnrolled;
	private Boolean vaMedEnrolled;
	private Boolean deceased;
	
	private String verified;
	@Transient
	private Integer[] verifiedArray;
	
	private String reviewed;
	@Transient
	private Integer[] reviewedArray;
	
	private String vaCurrent;
	@Transient
	private Integer[] vaCurrentArray;
	
	private String rural;
	
	private String address1;
	private String lastName;
	private String firstName;
	private String city;
	private String state;
	private String zip;
	
	private String primaryPhone;
	@Transient
	private String primaryPhone1;
	@Transient
	private String primaryPhone2;
	@Transient
	private String primaryPhone3;
	
	private String altPhone;
	@Transient
	private String altPhone1;
	@Transient
	private String altPhone2;
	@Transient
	private String altPhone3;
	
	private String ssn;
	@Transient
	private String ssn1;
	@Transient
	private String ssn2;
	@Transient
	private String ssn3;
	
	private String email;
	
	private Date dob;
	@Transient
	private Integer day;
	@Transient
	private Integer month;
	@Transient
	private Integer year;
	
	
	private Long serviceEraId;
	private Long serviceBranchId;
	private Long dischargeTypeId;
	private Long combatZoneId;
	private Long benefitTypeId;
	private String note;
	private String decorationMedals;
	@Transient
	private Long[] decorationMedalArray;
	
	private Date servicePeriodFrom;
	@Transient
	private Integer monthFrom;
	@Transient
	private Integer dayFrom;
	@Transient
	private Integer yearFrom;
	
	private Date servicePeriodTo;
	@Transient
	private Integer monthTo;
	@Transient
	private Integer dayTo;
	@Transient
	private Integer yearTo;
	
	private String gender;
	private String ageRange;
	
	private Boolean adhoc;
	private String sortedBy;
	@Transient
	private String[] sortedByArray;
	private String sortedType;
	@Transient
	private String[] sortedTypeArray;
	private String operators;
	@Transient
	private String[] operatorsArray;
	private String objValues;
	@Transient
	private String[] objectValues;
	private String objNames;
	@Transient
	private String[] objectNames;
	@Transient
	private String sql;
	
	// ---- setters/getters 
	
	public String getSource() {
		if (source == null && sourceArray != null) {
			source = "";
			for (int i = 0; i < sourceArray.length; i++) {
				source += sourceArray[i];
				if (i < sourceArray.length - 1) {
					source += ",";
				}
			}
		}
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Boolean getVaEnrolled() {
		return vaEnrolled;
	}
	public void setVaEnrolled(Boolean vaEnrolled) {
		this.vaEnrolled = vaEnrolled;
	}
	public Boolean getVaMedEnrolled() {
		return vaMedEnrolled;
	}
	public void setVaMedEnrolled(Boolean vaMedEnrolled) {
		this.vaMedEnrolled = vaMedEnrolled;
	}
	public Boolean getDeceased() {
		return deceased;
	}
	public void setDeceased(Boolean deceased) {
		this.deceased = deceased;
	}
	public String getVerified() {
		return verified;
	}
	public void setVerified(String verified) {
		this.verified = verified;
	}
	public String getReviewed() {
		return reviewed;
	}
	public void setReviewed(String reviewed) {
		this.reviewed = reviewed;
	}
	public String getRural() {
		return rural;
	}
	public void setRural(String rural) {
		this.rural = rural;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getPrimaryPhone() {
		return primaryPhone;
	}
	public void setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = primaryPhone;
	}
	public String getPrimaryPhone1() {
		return primaryPhone1;
	}
	public void setPrimaryPhone1(String primaryPhone1) {
		this.primaryPhone1 = primaryPhone1;
	}
	public String getPrimaryPhone2() {
		return primaryPhone2;
	}
	public void setPrimaryPhone2(String primaryPhone2) {
		this.primaryPhone2 = primaryPhone2;
	}
	public String getPrimaryPhone3() {
		return primaryPhone3;
	}
	public void setPrimaryPhone3(String primaryPhone3) {
		this.primaryPhone3 = primaryPhone3;
	}
	public String getAltPhone() {
		return altPhone;
	}
	public void setAltPhone(String altPhone) {
		this.altPhone = altPhone;
	}
	public String getAltPhone1() {
		return altPhone1;
	}
	public void setAltPhone1(String altPhone1) {
		this.altPhone1 = altPhone1;
	}
	public String getAltPhone2() {
		return altPhone2;
	}
	public void setAltPhone2(String altPhone2) {
		this.altPhone2 = altPhone2;
	}
	public String getAltPhone3() {
		return altPhone3;
	}
	public void setAltPhone3(String altPhone3) {
		this.altPhone3 = altPhone3;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getSsn1() {
		return ssn1;
	}
	public void setSsn1(String ssn1) {
		this.ssn1 = ssn1;
	}
	public String getSsn2() {
		return ssn2;
	}
	public void setSsn2(String ssn2) {
		this.ssn2 = ssn2;
	}
	public String getSsn3() {
		return ssn3;
	}
	public void setSsn3(String ssn3) {
		this.ssn3 = ssn3;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getDob() {
		return dob;
	}
	public void setDob(Date dob) {
		this.dob = dob;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	public Long getServiceEraId() {
		return serviceEraId;
	}
	public void setServiceEraId(Long serviceEraId) {
		this.serviceEraId = serviceEraId;
	}
	public Long getServiceBranchId() {
		return serviceBranchId;
	}
	public void setServiceBranchId(Long serviceBranchId) {
		this.serviceBranchId = serviceBranchId;
	}
	public Long getDischargeTypeId() {
		return dischargeTypeId;
	}
	public void setDischargeTypeId(Long dischargeTypeId) {
		this.dischargeTypeId = dischargeTypeId;
	}
	public Long getBenefitTypeId() {
		return benefitTypeId;
	}
	public void setBenefitTypeId(Long benefitTypeId) {
		this.benefitTypeId = benefitTypeId;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getServicePeriodFrom() {
		return servicePeriodFrom;
	}
	public void setServicePeriodFrom(Date servicePeriodFrom) {
		this.servicePeriodFrom = servicePeriodFrom;
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
	public Date getServicePeriodTo() {
		return servicePeriodTo;
	}
	public void setServicePeriodTo(Date servicePeriodTo) {
		this.servicePeriodTo = servicePeriodTo;
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
	public Long[] getSourceArray() {
		if (sourceArray == null && source != null) {
			String[] temp = source.split(",");
			sourceArray = new Long[temp.length];
			for (int i = 0; i < sourceArray.length; i++) {
				sourceArray[i] = new Long(temp[i]);
			}
		}
		return sourceArray;
	}
	public void setSourceArray(Long[] sourceArray) {
		this.sourceArray = sourceArray;
	}
	public Integer[] getVerifiedArray() {
		return verifiedArray;
	}
	public void setVerifiedArray(Integer[] verifiedArray) {
		this.verifiedArray = verifiedArray;
	}
	public Integer[] getReviewedArray() {
		return reviewedArray;
	}
	public void setReviewedArray(Integer[] reviewedArray) {
		this.reviewedArray = reviewedArray;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getVaCurrent() {
		return vaCurrent;
	}
	public void setVaCurrent(String vaCurrent) {
		this.vaCurrent = vaCurrent;
	}
	public Integer[] getVaCurrentArray() {
		return vaCurrentArray;
	}
	public void setVaCurrentArray(Integer[] vaCurrentArray) {
		this.vaCurrentArray = vaCurrentArray;
	}
	public String getDecorationMedals() {
		return decorationMedals;
	}
	public void setDecorationMedals(String decorationMedals) {
		this.decorationMedals = decorationMedals;
	}
	public Long[] getDecorationMedalArray() {
		return decorationMedalArray;
	}
	public void setDecorationMedalArray(Long[] decorationMedalArray) {
		this.decorationMedalArray = decorationMedalArray;
	}
	public Long getCombatZoneId() {
		return combatZoneId;
	}
	public void setCombatZoneId(Long combatZoneId) {
		this.combatZoneId = combatZoneId;
	}
	public String getAgeRange() {
		return ageRange;
	}
	public void setAgeRange(String ageRange) {
		this.ageRange = ageRange;
	}
	public Boolean getAdhoc() {
		return adhoc;
	}
	public void setAdhoc(Boolean adhoc) {
		this.adhoc = adhoc;
	}
	
	public String getOperators() {
		if (operators == null && operatorsArray != null) {
			operators = "";
			for (int i = 0; i < operatorsArray.length; i++) {
				operators += operatorsArray[i];
				if (i < operatorsArray.length - 1) {
					operators += ",";
				}
			}
		}
		return operators;
	}
	public void setOperators(String operators) {
		this.operators = operators;
	}
	
	public String[] getOperatorsArray() {
		if (operatorsArray == null && operators != null) {
			operatorsArray = operators.split(",");
		}
		return operatorsArray;
	}
	public void setOperatorsArray(String[] operatorsArray) {
		this.operatorsArray = operatorsArray;
	}
	
	public String getSortedBy() {
		if (sortedBy == null && sortedByArray != null) {
			sortedBy = "";
			for (int i = 0; i < sortedByArray.length; i++) {
				sortedBy += sortedByArray[i];
				if (i < sortedByArray.length - 1) {
					sortedBy += ",";
				}
			}
		}
		return sortedBy;
	}
	public void setSortedBy(String sortedBy) {
		this.sortedBy = sortedBy;
	}
	
	public String[] getSortedByArray() {
		if (sortedByArray == null && sortedBy != null) {
			sortedByArray = sortedBy.split(",");
		}
		return sortedByArray;
	}
	public void setSortedByArray(String[] sortedByArray) {
		this.sortedByArray = sortedByArray;
	}
	
	public String getSortedType() {
		if (sortedType == null && sortedTypeArray != null) {
			sortedType = "";
			for (int i = 0; i < sortedTypeArray.length; i++) {
				sortedType += sortedTypeArray[i];
				if (i < sortedTypeArray.length - 1) {
					sortedType += ",";
				}
			}
		}
		return sortedType;
	}
	public void setSortedType(String sortedType) {
		this.sortedType = sortedType;
	}
	
	public String[] getSortedTypeArray() {
		if (sortedTypeArray == null && sortedType != null) {
			sortedTypeArray = sortedType.split(",");
		}
		return sortedTypeArray;
	}
	public void setSortedTypeArray(String[] sortedTypeArray) {
		this.sortedTypeArray = sortedTypeArray;
	}
	
	public String getObjNames() {
		if (objNames == null && objectNames != null) {
			objNames = "";
			for (int i = 0; i < objectNames.length; i++) {
				objNames += objectNames[i];
				if (i < objectNames.length - 1) {
					objNames += ",";
				}
			}
		}
		return objNames;
	}
	public void setObjNames(String objNames) {
		this.objNames = objNames;
	}
	
	public String[] getObjectNames() {
		if (objectNames == null && objNames != null) {
			objectNames = objNames.split(",");
		}
		return objectNames;
	}
	public void setObjectNames(String[] objectNames) {
		this.objectNames = objectNames;
	}
	
	public String getObjValues() {
		if (objValues == null && objectValues != null) {
			objValues = "";
			for (int i = 0; i < objectValues.length; i++) {
				objValues += objectValues[i];
				if (i < objectValues.length - 1) {
					objValues += ",";
				}
			}
		}
		return objValues;
	}
	public void setObjValues(String objValues) {
		this.objValues = objValues;
	}
	
	public String[] getObjectValues() {
		if (objectValues == null && objValues != null) {
			objectValues = objValues.split(",");
		}
		return objectValues;
	}
	public void setObjectValues(String[] objectValues) {
		this.objectValues = objectValues;
	}
	
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}	
}
