package gov.utah.va.vts.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Bean class to hold values for score card info.
 * 
 * @author HNGUYEN
 *
 */
public class ScoreCard implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date fromDate;
	private Date toDate;
	private Long totalUniqueSsn;
	private Long totalUniqueSsnPeriod;
	private Long totalDD214;
	
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public Long getTotalUniqueSsn() {
		return totalUniqueSsn;
	}
	public void setTotalUniqueSsn(Long totalUniqueSsn) {
		this.totalUniqueSsn = totalUniqueSsn;
	}
	public Long getTotalUniqueSsnPeriod() {
		return totalUniqueSsnPeriod;
	}
	public void setTotalUniqueSsnPeriod(Long totalUniqueSsnPeriod) {
		this.totalUniqueSsnPeriod = totalUniqueSsnPeriod;
	}
	public Long getTotalDD214() {
		return totalDD214;
	}
	public void setTotalDD214(Long totalDD214) {
		this.totalDD214 = totalDD214;
	}

}
