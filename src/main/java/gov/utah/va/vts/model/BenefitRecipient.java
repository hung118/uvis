package gov.utah.va.vts.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Benefit Recipient entity class.
 * 
 * @author hnguyen
 *
 */
@Entity
@Table(name="BENEFIT_RECIPIENT")
public class BenefitRecipient extends BaseEntityAbstract {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BENEFIT_RECIPIENT_SEQ")
	@SequenceGenerator(name="BENEFIT_RECIPIENT_SEQ", sequenceName="PERSON_SEQ", allocationSize=1)
	private Long id;
	@Transient
	private Long idBenefitRecipient;
	
	private String lastName;
	private String firstName;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zip;
	
	private String primaryPhone;
	@Transient
	private String phone1;
	@Transient
	private String phone2;
	@Transient
	private String phone3;
		
	@ManyToOne
	@JoinColumn(name="VETERAN_BENEFIT_ID")
	private VeteranBenefit veteranBenefit;
	@Transient
	private Long veteranBenefitId;
	
	@ManyToOne
	@JoinColumn(name="RELATION_ID")
	private Relation relation;
	@Transient
	private Long relationId;
		
	private Integer active;
	@Transient
	private Integer activeBenefitRecipient;

	private Date insertTimestamp;
	@Transient
	private String insertTimestampBenefitRecipient;
	
	private Date updateTimestamp;
		
	/* --- getters/setters --- */
	@Override
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

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
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

	public VeteranBenefit getVeteranBenefit() {
		return veteranBenefit;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getPhone3() {
		return phone3;
	}

	public void setPhone3(String phone3) {
		this.phone3 = phone3;
	}

	public Long getRelationId() {
		return relationId;
	}

	public void setRelationId(Long relationId) {
		this.relationId = relationId;
	}

	public void setVeteranBenefit(VeteranBenefit veteranBenefit) {
		this.veteranBenefit = veteranBenefit;
	}

	public Integer getActiveBenefitRecipient() {
		return activeBenefitRecipient;
	}

	public void setActiveBenefitRecipient(Integer activeBenefitRecipient) {
		this.activeBenefitRecipient = activeBenefitRecipient;
	}

	public String getInsertTimestampBenefitRecipient() {
		return insertTimestampBenefitRecipient;
	}

	public void setInsertTimestampBenefitRecipient(
			String insertTimestampBenefitRecipient) {
		this.insertTimestampBenefitRecipient = insertTimestampBenefitRecipient;
	}

	public Long getVeteranBenefitId() {
		return veteranBenefitId;
	}

	public void setVeteranBenefitId(Long veteranBenefitId) {
		this.veteranBenefitId = veteranBenefitId;
	}

	public Long getIdBenefitRecipient() {
		return idBenefitRecipient;
	}

	public void setIdBenefitRecipient(Long idBenefitRecipient) {
		this.idBenefitRecipient = idBenefitRecipient;
	}
	

}
