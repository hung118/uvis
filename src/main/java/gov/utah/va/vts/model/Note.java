package gov.utah.va.vts.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Notes entity class.
 * 
 * @author HNGUYEN
 *
 */
@Entity
@Table(name="NOTE")
public class Note extends BaseEntityVTS {

	private static final long serialVersionUID = 1L;

	private String noteText;
	
	@ManyToOne
	@JoinColumn(name="CREATED_BY")
	private User createdBy;	
	
	public Note() {
		super();
	}

	public String getNoteText() {
		return noteText;
	}

	public void setNoteText(String noteText) {
		this.noteText = noteText;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}


}
