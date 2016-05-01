package gov.utah.va.vts.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Veteran entity class.
 * 
 * @author HNGUYEN
 *
 */
@Entity
@Table(name="VETERAN_INFO")
public class Veteran extends BaseEntityAbstract {

	private static final long serialVersionUID = 1L;
	
	static final Comparator<Note> NOTE_SORT = new Comparator<Note>() {
		public int compare(Note o1, Note o2) {
			return o1.getInsertTimestamp().compareTo(o2.getInsertTimestamp());
			// or for descending order 
			//return o2.getInsertTimestamp().compareTo(o1.getInsertTimestamp());
		}		
	};
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PERSON_SEQ")
	@SequenceGenerator(name="PERSON_SEQ", sequenceName="PERSON_SEQ", allocationSize=1)
	private Long id;
	
	private String lastName;
	
	private String firstName;
	
	private String middleName;
	
	private String suffix;
	
	private String email;
	
	private Integer emailOption = new Integer(2);
	
	@ManyToOne
	@JoinColumn(name="CREATED_BY")
	private User createdBy;
	
	@ManyToOne
	@JoinColumn(name="UPDATED_BY")
	private User updatedBy;
		
	private Integer active;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date insertTimestamp;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTimestamp;
	
	private String ssn;
	@Transient
	private String ssn1;
	@Transient
	private String ssn2;
	@Transient
	private String ssn3;
	
	private Date dateOfBirth;
	@Transient
	private String dateOfBirthStr;
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
	
	@Transient
	private String mailingAddr;
	private String mailingAddr1;
	private String mailingAddr2;
	private String mailingCity;
	private String mailingState;
	private String mailingZip;
	
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
	
	private String altContactFirstName;
	private String altContactLastName;
	private String altContactAddr1;
	private String altContactAddr2;
	private String altContactCity;
	private String altContactState;
	private String altContactZip;
	
	private String altContactPhone;
	@Transient
	private String altContactPhone1;
	@Transient
	private String altContactPhone2;
	@Transient
	private String altContactPhone3;
	
	@ManyToOne
	@JoinColumn(name="ALT_CONTACT_RELATION_ID")
	private Relation relation;
	
	private Date dateOfDeath;
	@Transient
	private Integer dayD;
	@Transient
	private Integer monthD;
	@Transient
	private Integer yearD;
	
	private Integer percentDisability;
	private String gender;

	@ManyToOne
	@JoinColumn(name="ETHNICITY_ID")
	private Ethnicity ethnicity;
	
	@ManyToOne
	@JoinColumn(name="SOURCE")
	private RecordType recordType;
	
	private Integer contactable;
	private Boolean shareFederalVa;
	private Integer verified;
	private Boolean vaEnrolled;	// receiving compensation and pension
	private Boolean vaMedEnrolled;
	private Integer vaCurrent;
	private Date sourceDate;
	private Integer reviewed;
	private String rural;
	
	@ManyToMany
	@JoinTable(
			name="VETERAN_NOTE",
			joinColumns=@JoinColumn(name="VETERAN_ID"),
			inverseJoinColumns=@JoinColumn(name="NOTE_ID")
	)
	private List<Note> noteList;
	@Transient
	private String noteText;
	
	@OneToMany(mappedBy="veteran")
	private List<VeteranServicePeriod> servicePeriodList;
	
	@ManyToMany
	@JoinTable(
			name="VETERAN_BENEFIT",
			joinColumns=@JoinColumn(name="VETERAN_ID"),
			inverseJoinColumns=@JoinColumn(name="BENEFIT_TYPE_ID")
	)
	private List<BenefitType> benefitTypeList;

	@ManyToMany
	@JoinTable(
			name="VETERAN_DECORATION",
			joinColumns=@JoinColumn(name="VETERAN_ID"),
			inverseJoinColumns=@JoinColumn(name="DECORATION_MEDAL_ID")
	)
	private List<DecorationMedal> decorationMedalList;
	@Transient
	private Long[] decorationMedals;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="veteran")
	private List<VeteranAttachment> attachmentsList;
	
	@Transient
	private Boolean deceased;
	@Transient
	private String vetPageTitle;
	@Transient
	private boolean fromDashboard = false;
	@Transient
	private Boolean isAttachmentList;
	@Transient
	private Integer noteListSize;
	@Transient
	private Integer servicePeriodListSize;
	@Transient
	private Integer decorationMedalListSize;
	@Transient
	private Integer benefitTypeListSize;
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getEmailOption() {
		return emailOption;
	}
	public void setEmailOption(Integer emailOption) {
		this.emailOption = emailOption;
	}
	public User getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}
	public User getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Integer getActive() {
		return active;
	}
	public void setActive(Integer active) {
		this.active = active;
	}
	public Date getInsertTimestamp() {
		return insertTimestamp;
	}
	public void setInsertTimestamp(Date insertTimestamp) {
		this.insertTimestamp = insertTimestamp;
	}
	public Date getUpdateTimestamp() {
		return updateTimestamp;
	}
	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
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
	public String getMailingAddr() {
		return mailingAddr;
	}
	public void setMailingAddr(String mailingAddr) {
		this.mailingAddr = mailingAddr;
	}
	public String getMailingAddr1() {
		return mailingAddr1;
	}
	public void setMailingAddr1(String mailingAddr1) {
		this.mailingAddr1 = mailingAddr1;
	}
	public String getMailingAddr2() {
		return mailingAddr2;
	}
	public void setMailingAddr2(String mailingAddr2) {
		this.mailingAddr2 = mailingAddr2;
	}
	public String getMailingCity() {
		return mailingCity;
	}
	public void setMailingCity(String mailingCity) {
		this.mailingCity = mailingCity;
	}
	public String getMailingState() {
		return mailingState;
	}
	public void setMailingState(String mailingState) {
		this.mailingState = mailingState;
	}
	public String getMailingZip() {
		return mailingZip;
	}
	public void setMailingZip(String mailingZip) {
		this.mailingZip = mailingZip;
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
	public String getAltContactFirstName() {
		return altContactFirstName;
	}
	public void setAltContactFirstName(String altContactFirstName) {
		this.altContactFirstName = altContactFirstName;
	}
	public String getAltContactLastName() {
		return altContactLastName;
	}
	public void setAltContactLastName(String altContactLastName) {
		this.altContactLastName = altContactLastName;
	}
	public String getAltContactAddr1() {
		return altContactAddr1;
	}
	public void setAltContactAddr1(String altContactAddr1) {
		this.altContactAddr1 = altContactAddr1;
	}
	public String getAltContactAddr2() {
		return altContactAddr2;
	}
	public void setAltContactAddr2(String altContactAddr2) {
		this.altContactAddr2 = altContactAddr2;
	}
	public String getAltContactCity() {
		return altContactCity;
	}
	public void setAltContactCity(String altContactCity) {
		this.altContactCity = altContactCity;
	}
	public String getAltContactState() {
		return altContactState;
	}
	public void setAltContactState(String altContactState) {
		this.altContactState = altContactState;
	}
	public String getAltContactZip() {
		return altContactZip;
	}
	public void setAltContactZip(String altContactZip) {
		this.altContactZip = altContactZip;
	}
	public String getAltContactPhone() {
		return altContactPhone;
	}
	public void setAltContactPhone(String altContactPhone) {
		this.altContactPhone = altContactPhone;
	}
	public String getAltContactPhone1() {
		return altContactPhone1;
	}
	public void setAltContactPhone1(String altContactPhone1) {
		this.altContactPhone1 = altContactPhone1;
	}
	public String getAltContactPhone2() {
		return altContactPhone2;
	}
	public void setAltContactPhone2(String altContactPhone2) {
		this.altContactPhone2 = altContactPhone2;
	}
	public String getAltContactPhone3() {
		return altContactPhone3;
	}
	public void setAltContactPhone3(String altContactPhone3) {
		this.altContactPhone3 = altContactPhone3;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getDateOfBirthStr() {
		return dateOfBirthStr;
	}
	public void setDateOfBirthStr(String dateOfBirthStr) {
		this.dateOfBirthStr = dateOfBirthStr;
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
	public Date getDateOfDeath() {
		return dateOfDeath;
	}
	public void setDateOfDeath(Date dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}
	public Integer getDayD() {
		return dayD;
	}
	public void setDayD(Integer dayD) {
		this.dayD = dayD;
	}
	public Integer getMonthD() {
		return monthD;
	}
	public void setMonthD(Integer monthD) {
		this.monthD = monthD;
	}
	public Integer getYearD() {
		return yearD;
	}
	public void setYearD(Integer yearD) {
		this.yearD = yearD;
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
	public Ethnicity getEthnicity() {
		return ethnicity;
	}
	public void setEthnicity(Ethnicity ethnicity) {
		this.ethnicity = ethnicity;
	}
	public Integer getContactable() {
		return contactable;
	}
	public void setContactable(Integer contactable) {
		this.contactable = contactable;
	}
	public Boolean getShareFederalVa() {
		return shareFederalVa;
	}
	public void setShareFederalVa(Boolean shareFederalVa) {
		this.shareFederalVa = shareFederalVa;
	}
	public Integer getVerified() {
		return verified;
	}
	public void setVerified(Integer verified) {
		this.verified = verified;
	}

	public Integer getVaCurrent() {
		return vaCurrent;
	}
	public void setVaCurrent(Integer vaCurrent) {
		this.vaCurrent = vaCurrent;
	}
	public Date getSourceDate() {
		return sourceDate;
	}
	public void setSourceDate(Date sourceDate) {
		this.sourceDate = sourceDate;
	}
	public Integer getReviewed() {
		return reviewed;
	}
	public void setReviewed(Integer reviewed) {
		this.reviewed = reviewed;
	}
	public String getRural() {
		return rural;
	}
	public void setRural(String rural) {
		this.rural = rural;
	}
	public List<Note> getNoteList() {
		
		// sort note list by insertTimestamp desc
		if (noteList != null) Collections.sort(noteList, NOTE_SORT);
		
		return noteList;
	}
	public void setNoteList(List<Note> noteList) {
		this.noteList = noteList;
	}
	public String getNoteText() {
		return noteText;
	}
	public void setNoteText(String noteText) {
		this.noteText = noteText;
	}
	public RecordType getRecordType() {
		return recordType;
	}
	public void setRecordType(RecordType recordType) {
		this.recordType = recordType;
	}
	public String getVetPageTitle() {
		return vetPageTitle;
	}
	public void setVetPageTitle(String vetPageTitle) {
		this.vetPageTitle = vetPageTitle;
	}
	public Relation getRelation() {
		return relation;
	}
	public void setRelation(Relation relation) {
		this.relation = relation;
	}
	public List<VeteranServicePeriod> getServicePeriodList() {
		return servicePeriodList;
	}
	public void setServicePeriodList(List<VeteranServicePeriod> servicePeriodList) {
		this.servicePeriodList = servicePeriodList;
	}

	public Boolean getVaEnrolled() {
		return vaEnrolled;
	}
	public void setVaEnrolled(Boolean vaEnrolled) {
		this.vaEnrolled = vaEnrolled;
	}
	public Boolean getDeceased() {
		return deceased;
	}
	public void setDeceased(Boolean deceased) {
		this.deceased = deceased;
	}
	public Boolean getVaMedEnrolled() {
		return vaMedEnrolled;
	}
	public void setVaMedEnrolled(Boolean vaMedEnrolled) {
		this.vaMedEnrolled = vaMedEnrolled;
	}
	public List<BenefitType> getBenefitTypeList() {
		return benefitTypeList;
	}
	public void setBenefitTypeList(List<BenefitType> benefitTypeList) {
		this.benefitTypeList = benefitTypeList;
	}
	public List<DecorationMedal> getDecorationMedalList() {
		return decorationMedalList;
	}
	public void setDecorationMedalList(List<DecorationMedal> decorationMedalList) {
		this.decorationMedalList = decorationMedalList;
	}
	public Long[] getDecorationMedals() {
		return decorationMedals;
	}
	public void setDecorationMedals(Long[] decorationMedals) {
		this.decorationMedals = decorationMedals;
	}
	public boolean isFromDashboard() {
		return fromDashboard;
	}
	public void setFromDashboard(boolean fromDashboard) {
		this.fromDashboard = fromDashboard;
	}
	public List<VeteranAttachment> getAttachmentsList() {
		return attachmentsList;
	}
	public void setAttachmentsList(List<VeteranAttachment> attachmentsList) {
		this.attachmentsList = attachmentsList;
	}
	public Boolean getIsAttachmentList() {
		return isAttachmentList;
	}
	public void setIsAttachmentList(Boolean isAttachmentList) {
		this.isAttachmentList = isAttachmentList;
	}
	public Integer getNoteListSize() {
		if (noteList != null)
			return noteList.size();
		else 
			return new Integer(0);
	}
	public Integer getServicePeriodListSize() {
		if (servicePeriodList != null)
			return servicePeriodList.size();
		else 
			return new Integer(0);
	}
	public Integer getDecorationMedalListSize() {
		if (decorationMedalList != null)
			return decorationMedalList.size();
		else 
			return new Integer(0);
	}
	public Integer getBenefitTypeListSize() {
		if (benefitTypeList != null)
			return benefitTypeList.size();
		else 
			return new Integer(0);
	}
}
