package gov.utah.va.vts.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * ServiceEra entity class.
 * 
 * @author HNGUYEN
 *
 */
@Entity
@Table(name="SERVICE_ERA")
public class ServiceEra extends BaseEntityVTS {

	private static final long serialVersionUID = 1L;

	private String name;
	
	private Date startDate;
	
	@Transient
	private String startDateStr;
	
	private Date endDate;

	@Transient
	private String endDateStr;
	
	public ServiceEra() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStartDateStr() {
		return startDateStr;
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	public String getEndDateStr() {
		return endDateStr;
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

}
