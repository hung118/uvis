package gov.utah.va.vts.model;

import java.io.InputStream;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * VeteranAttachments entity class.
 * 
 * @author HNGUYEN
 *
 */
@Entity
@Table(name="VETERAN_ATTACHMENT")
public class VeteranAttachment extends BaseEntityAbstract {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="VETERAN_ATTACHMENT_SEQ")
	@SequenceGenerator(name="VETERAN_ATTACHMENT_SEQ", sequenceName="VTS_ATTACHMENT_SEQ", allocationSize=1)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="VETERAN_ID")
	private Veteran veteran;
	
	private byte[] attachment;
	private String attachmentFileName;
	private String attachmentContentType;
	private String attachmentPointer;
	
	@Transient
	private InputStream attachmentIs;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="DOC_TYPE_ID")
	private DocType docType;
		
	@ManyToOne
	@JoinColumn(name="CREATED_BY")
	private User createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date insertTimestamp;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTimestamp;

	@Transient
	private String pageTitle;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Veteran getVeteran() {
		return veteran;
	}

	public void setVeteran(Veteran veteran) {
		this.veteran = veteran;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	public String getAttachmentFileName() {
		return attachmentFileName;
	}

	public void setAttachmentFileName(String attachmentFileName) {
		this.attachmentFileName = attachmentFileName;
	}

	public String getAttachmentContentType() {
		return attachmentContentType;
	}

	public void setAttachmentContentType(String attachmentContentType) {
		this.attachmentContentType = attachmentContentType;
	}

	public DocType getDocType() {
		return docType;
	}

	public void setDocType(DocType docType) {
		this.docType = docType;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
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

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public InputStream getAttachmentIs() {
		return attachmentIs;
	}

	public void setAttachmentIs(InputStream attachmentIs) {
		this.attachmentIs = attachmentIs;
	}

	public String getAttachmentPointer() {
		return attachmentPointer;
	}

	public void setAttachmentPointer(String attachmentPointer) {
		this.attachmentPointer = attachmentPointer;
	}
			
}
