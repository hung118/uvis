package gov.utah.va.vts.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Benefit type entity class.
 * 
 * @author HNGUYEN
 *
 */
@Entity
@Table(name="BENEFIT_TYPE")
public class BenefitType extends BaseEntityVTS {

	private static final long serialVersionUID = 1L;

	private String name;
	
	private String description;
	
	@Transient
	private List<BenefitRecipient> benefitRecipients;
	
	public BenefitType() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<BenefitRecipient> getBenefitRecipients() {
		return benefitRecipients;
	}

	public void setBenefitRecipients(List<BenefitRecipient> benefitRecipients) {
		this.benefitRecipients = benefitRecipients;
	}

}
