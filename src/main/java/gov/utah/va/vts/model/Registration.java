package gov.utah.va.vts.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Registration entity class.
 * 
 * @author HNGUYEN
 *
 */
@Entity
@Table(name="REGISTRATION")
public class Registration extends BaseEntityAbstract {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="REGISTRATION_SEQ")
	@SequenceGenerator(name="REGISTRATION_SEQ", sequenceName="PERSON_SEQ", allocationSize=1)
	private Long id;
	
	private String lastName;
	
	private String firstName;
	
	private String middleName;
	
	private String suffix;
	
	private String email;
	
	private Integer emailOption;
	
	private String ssn;
	@Transient
	private String ssn1;
	@Transient
	private String ssn2;
	@Transient
	private String ssn3;
	
	private Date dateOfBirth;
	@Transient
	private Integer day;
	@Transient
	private Integer month;
	@Transient
	private Integer year;
	
	private String address1;
	private String address2;
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
			
	private Integer percentDisability;
	private String gender;

	private Long ethnicityId;
		
	private Boolean shareFederalVa;
	private Boolean vaEnrolled;	// receiving compensation and pension

	private Long serviceBranchId;
	private Date serviceStartDate;
	@Transient
	private Integer dayFrom;
	@Transient
	private Integer monthFrom;
	@Transient
	private Integer yearFrom;
	
	private Date serviceEndDate;
	@Transient
	private Integer dayTo;
	@Transient
	private Integer monthTo;
	@Transient
	private Integer yearTo;
	
	private Long dischargeTypeId;
	private Boolean purpleHeart;
	
	private Date createdDate;
	private Date transferDate;
	
	/* --- getters/setters --- */
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getSuffix() {
		return suffix;
	}
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	public Integer getEmailOption() {
		return emailOption;
	}
	public void setEmailOption(Integer emailOption) {
		this.emailOption = emailOption;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
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


	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
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

	public Integer getPercentDisability() {
		return percentDisability;
	}
	public void setPercentDisability(Integer percentDisability) {
		this.percentDisability = percentDisability;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}

	public Long getEthnicityId() {
		return ethnicityId;
	}
	public void setEthnicityId(Long ethnicityId) {
		this.ethnicityId = ethnicityId;
	}
	public Boolean getShareFederalVa() {
		return shareFederalVa;
	}
	public void setShareFederalVa(Boolean shareFederalVa) {
		this.shareFederalVa = shareFederalVa;
	}

	public Boolean getVaEnrolled() {
		return vaEnrolled;
	}
	public void setVaEnrolled(Boolean vaEnrolled) {
		this.vaEnrolled = vaEnrolled;
	}

	public Long getServiceBranchId() {
		return serviceBranchId;
	}
	public void setServiceBranchId(Long serviceBranchId) {
		this.serviceBranchId = serviceBranchId;
	}
	public Date getServiceStartDate() {
		return serviceStartDate;
	}
	public void setServiceStartDate(Date serviceStartDate) {
		this.serviceStartDate = serviceStartDate;
	}
	public Integer getDayFrom() {
		return dayFrom;
	}
	public void setDayFrom(Integer dayFrom) {
		this.dayFrom = dayFrom;
	}
	public Integer getMonthFrom() {
		return monthFrom;
	}
	public void setMonthFrom(Integer monthFrom) {
		this.monthFrom = monthFrom;
	}
	public Integer getYearFrom() {
		return yearFrom;
	}
	public void setYearFrom(Integer yearFrom) {
		this.yearFrom = yearFrom;
	}
	public Date getServiceEndDate() {
		return serviceEndDate;
	}
	public void setServiceEndDate(Date serviceEndDate) {
		this.serviceEndDate = serviceEndDate;
	}
	public Integer getDayTo() {
		return dayTo;
	}
	public void setDayTo(Integer dayTo) {
		this.dayTo = dayTo;
	}
	public Integer getMonthTo() {
		return monthTo;
	}
	public void setMonthTo(Integer monthTo) {
		this.monthTo = monthTo;
	}
	public Integer getYearTo() {
		return yearTo;
	}
	public void setYearTo(Integer yearTo) {
		this.yearTo = yearTo;
	}
	public Long getDischargeTypeId() {
		return dischargeTypeId;
	}
	public void setDischargeTypeId(Long dischargeTypeId) {
		this.dischargeTypeId = dischargeTypeId;
	}
	public Boolean getPurpleHeart() {
		return purpleHeart;
	}
	public void setPurpleHeart(Boolean purpleHeart) {
		this.purpleHeart = purpleHeart;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}
}
