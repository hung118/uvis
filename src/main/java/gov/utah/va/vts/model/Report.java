package gov.utah.va.vts.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Report entity class
 * @author hnguyen
 *
 */
@Entity
@Table(name="REPORT")
public class Report extends BaseEntityVTS {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="USER_ID")
	private User user;
	
	@OneToOne
	@JoinColumn(name="REPORT_PROPERTY_ID")
	private ReportProperty reportProperty;
	
	private String reportName;
	
	private Boolean sharable;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public ReportProperty getReportProperty() {
		return reportProperty;
	}

	public void setReportProperty(ReportProperty reportProperty) {
		this.reportProperty = reportProperty;
	}

	public Boolean getSharable() {
		return sharable;
	}

	public void setSharable(Boolean sharable) {
		this.sharable = sharable;
	}

}
