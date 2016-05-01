package gov.utah.va.vts.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Bean class to hold values for DOCTEK_ALL table.
 * 
 * @author HNGUYEN
 *
 */
public class DocTek implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ssn;
	private String firstName;
	private String middleInitial;
	private String lastName;
	private String branchOfService;
	private Date dateOfBirth;
	private String decorationsAndMedals;
	
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleInitial() {
		return middleInitial;
	}
	public void setMiddleInitial(String middleInitial) {
		this.middleInitial = middleInitial;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getBranchOfService() {
		return branchOfService;
	}
	public void setBranchOfService(String branchOfService) {
		this.branchOfService = branchOfService;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getDecorationsAndMedals() {
		return decorationsAndMedals;
	}
	public void setDecorationsAndMedals(String decorationsAndMedals) {
		this.decorationsAndMedals = decorationsAndMedals;
	}
	
}
