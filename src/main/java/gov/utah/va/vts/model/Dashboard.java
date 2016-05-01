package gov.utah.va.vts.model;

import java.io.Serializable;

/**
 * Bean class for dashboard.
 * @author HNGUYEN
 *
 */

public class Dashboard implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String ssn;
	private String firstName;
	private String lastName;
	private Integer numberOfSources;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Integer getNumberOfSources() {
		return numberOfSources;
	}
	public void setNumberOfSources(Integer numberOfSources) {
		this.numberOfSources = numberOfSources;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	
}
