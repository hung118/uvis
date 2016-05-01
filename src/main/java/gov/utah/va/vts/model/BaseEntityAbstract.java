package gov.utah.va.vts.model;

import java.io.Serializable;

import javax.persistence.Transient;

/**
 * The abstract base entity class to be inherited from all entity classes.
 * 
 * @author HNGUYEN
 *
 */
public abstract class BaseEntityAbstract implements Serializable {

	private static final long serialVersionUID = 1L;

	@Transient
	private String op = "insert";
	
	/**
	 * Gets the entity ID of sub-class which has overriden getId method.
	 * @return
	 */
	public abstract Long getId();

	/**
	 * Custom toString method.
	 * @return
	 */
	/*public String toString(){
		return new StringBuffer().append(
				this.getClass()).append("[").append(this.getId()).append("]").toString();
	}*/
		
	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}	
}
