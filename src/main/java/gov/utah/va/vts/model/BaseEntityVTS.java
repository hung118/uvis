package gov.utah.va.vts.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The base entity class to be inherited from most entity classes for VTS app.
 * 
 * @author HNGUYEN
 *
 */
@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public class BaseEntityVTS extends BaseEntityAbstract {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VTS_SEQ")
	@SequenceGenerator(name="VTS_SEQ", sequenceName="VTS_SEQ", allocationSize=1)
	private Long id;
	
	private Integer active;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date insertTimestamp;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTimestamp;
			
	@Override
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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

}
