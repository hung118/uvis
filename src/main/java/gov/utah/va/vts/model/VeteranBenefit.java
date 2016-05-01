package gov.utah.va.vts.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Veteran benefit entity class.
 * 
 * @author hnguyen
 *
 */
@Entity
@Table(name="VETERAN_BENEFIT")
public class VeteranBenefit extends BaseEntityVTS {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="VETERAN_ID")
	private Veteran veteran;

	@ManyToOne
	@JoinColumn(name="BENEFIT_TYPE_ID")
	private BenefitType benefitType;
	@Transient
	private String benefitTypeName;
	@Transient
	private Long benefitTypeId;

	@Transient
	private Long idBenefitType;
	@Transient
	private Integer activeBenefitType;
	@Transient
	private String insertTimestampBenefitType;
	@Transient
	private List<BenefitRecipient> benefitRecipientList;
	
	public VeteranBenefit() {
		super();
	}

	public Veteran getVeteran() {
		return veteran;
	}

	public void setVeteran(Veteran veteran) {
		this.veteran = veteran;
	}

	public BenefitType getBenefitType() {
		return benefitType;
	}

	public void setBenefitType(BenefitType benefitType) {
		this.benefitType = benefitType;
	}

	public String getBenefitTypeName() {
		return benefitTypeName;
	}

	public void setBenefitTypeName(String benefitTypeName) {
		this.benefitTypeName = benefitTypeName;
	}

	public Long getIdBenefitType() {
		return idBenefitType;
	}

	public void setIdBenefitType(Long idBenefitType) {
		this.idBenefitType = idBenefitType;
	}

	public Integer getActiveBenefitType() {
		return activeBenefitType;
	}

	public void setActiveBenefitType(Integer activeBenefitType) {
		this.activeBenefitType = activeBenefitType;
	}

	public String getInsertTimestampBenefitType() {
		return insertTimestampBenefitType;
	}

	public void setInsertTimestampBenefitType(String insertTimestampBenefitType) {
		this.insertTimestampBenefitType = insertTimestampBenefitType;
	}

	public Long getBenefitTypeId() {
		return benefitTypeId;
	}

	public void setBenefitTypeId(Long benefitTypeId) {
		this.benefitTypeId = benefitTypeId;
	}

	public List<BenefitRecipient> getBenefitRecipientList() {
		return benefitRecipientList;
	}

	public void setBenefitRecipientList(List<BenefitRecipient> benefitRecipientList) {
		this.benefitRecipientList = benefitRecipientList;
	}

}
