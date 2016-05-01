package gov.utah.va.vts.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Role entity class.
 * 
 * @author hnguyen
 *
 */
@Entity
@Table(name="ROLE")
public class Role extends BaseEntityVTS {

	private static final long serialVersionUID = 1L;
	
	private String roleName;
	
	public Role() {
		super();
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}

	
}
