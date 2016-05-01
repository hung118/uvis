package gov.utah.va.vts.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CombatZone entity class.
 * 
 * @author HNGUYEN
 *
 */
@Entity
@Table(name="COMBAT_ZONE")
public class CombatZone extends BaseEntityVTS {

	private static final long serialVersionUID = 1L;

	private String name;
	
	private String description;
	
	public CombatZone() {
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

}
