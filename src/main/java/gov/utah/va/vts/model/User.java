package gov.utah.va.vts.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * Security_User entity class.
 * 
 * @author HNGUYEN
 *
 */
@Entity
@Table(name="SECURITY_USER")
public class User extends BaseEntityAbstract {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SECURITY_USER_SEQ")
	@SequenceGenerator(name="SECURITY_USER_SEQ", sequenceName="PERSON_SEQ", allocationSize=1)
	private Long id;
	
	private String lastName;
	
	private String firstName;
	
	private String middleName;
	
	private String email;
	
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
	
	@ManyToMany
	@JoinTable(
			name="SECURITY_USER_ROLE",
			joinColumns=@JoinColumn(name="SECURITY_USER_ID"),
			inverseJoinColumns=@JoinColumn(name="ROLE_ID")
	)
	private List<Role> roles;
	
	@Transient
	private Long[] roleIds;
	
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoleIds(Long[] roleIds) {
		this.roleIds = roleIds;
	}

	public Long[] getRoleIds() {
		return roleIds;
	}

}
