package gov.utah.va.vts.dao.impl;

import gov.utah.va.vts.dao.NoteDao;
import gov.utah.va.vts.model.Note;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation class for NotesDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="noteDao")
@Transactional
public class NoteDaoImpl extends BaseEntityDaoImpl<Note> implements NoteDao {

	public NoteDaoImpl() {
		super(Note.class);
	}
}
