package gov.utah.va.vts.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Veteran_Note entity class.
 * 
 * @author HNGUYEN
 *
 */
@Entity
@Table(name="VETERAN_NOTE")
public class VeteranNote extends BaseEntityVTS {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="VETERAN_ID")
	private Veteran veteran;	
	
	@ManyToOne
	@JoinColumn(name="NOTE_ID")
	private Note note;	

	public VeteranNote() {
		super();
	}

	public Veteran getVeteran() {
		return veteran;
	}

	public void setVeteran(Veteran veteran) {
		this.veteran = veteran;
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}

}
