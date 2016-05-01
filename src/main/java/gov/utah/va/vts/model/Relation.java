package gov.utah.va.vts.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Relation entity class.
 * 
 * @author HNGUYEN
 *
 */
@Entity
@Table(name="RELATION")
public class Relation extends BaseEntityVTS {

	private static final long serialVersionUID = 1L;

	private String name;
	
	private String description;
	
	public Relation() {
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
