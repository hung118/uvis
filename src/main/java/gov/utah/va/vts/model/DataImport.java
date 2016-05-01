package gov.utah.va.vts.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Data_Import entity class
 * @author HNGUYEN
 *
 */
@Entity
@Table(name="DATA_IMPORT")
public class DataImport extends BaseEntityAbstract {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="DATA_IMPORT_SEQ")
	@SequenceGenerator(name="DATA_IMPORT_SEQ", sequenceName="VTS_SEQ", allocationSize=1)
	private Long id;
	
	private String fileName;
	
	private Integer recordCount;
	
	private Integer badRec;
	
	@ManyToOne
	@JoinColumn(name="SOURCE")
	private RecordType recordType;
	
	private String status;
	
	@ManyToOne
	@JoinColumn(name="CREATED_BY")
	private User createdBy;
		
	private Integer active;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date insertTimestamp;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTimestamp;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}

	public Integer getRecordCount() {
		return recordCount;
	}

	public Integer getBadRec() {
		return badRec;
	}

	public void setBadRec(Integer badRec) {
		this.badRec = badRec;
	}

	public RecordType getRecordType() {
		return recordType;
	}

	public void setRecordType(RecordType recordType) {
		this.recordType = recordType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
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
