package gov.utah.va.vts.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Bean class to hold values for WEB_REGISTERED_CLND table.
 * 
 * @author HNGUYEN
 *
 */
public class Online implements Serializable {

	private static final long serialVersionUID = 1L;

	private String ssn;
	private String firstName;
	private String lastName;
	private Date servedFrom;
	private Date servedTo;
	private String branch;
	private String branchClean;
	private String medals;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String primaryPhone;
	private String altPhone;
	private String email;
	private String injured;
	private String comp;
	private String purpleHeart;
	private String combat;
	
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
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getServedFrom() {
		return servedFrom;
	}
	public void setServedFrom(Date servedFrom) {
		this.servedFrom = servedFrom;
	}
	public Date getServedTo() {
		return servedTo;
	}
	public void setServedTo(Date servedTo) {
		this.servedTo = servedTo;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getBranchClean() {
		return branchClean;
	}
	public void setBranchClean(String branchClean) {
		this.branchClean = branchClean;
	}
	public String getMedals() {
		return medals;
	}
	public void setMedals(String medals) {
		this.medals = medals;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getAltPhone() {
		return altPhone;
	}
	public void setAltPhone(String altPhone) {
		this.altPhone = altPhone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getInjured() {
		return injured;
	}
	public void setInjured(String injured) {
		this.injured = injured;
	}
	public String getComp() {
		return comp;
	}
	public void setComp(String comp) {
		this.comp = comp;
	}
	public String getPurpleHeart() {
		return purpleHeart;
	}
	public void setPurpleHeart(String purpleHeart) {
		this.purpleHeart = purpleHeart;
	}
	public String getCombat() {
		return combat;
	}
	public void setCombat(String combat) {
		this.combat = combat;
	}
	
}
