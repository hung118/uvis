package gov.utah.va.vts.dao.impl;

import gov.utah.va.vts.dao.VeteranNoteDao;
import gov.utah.va.vts.model.VeteranNote;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation class for VeteranNoteDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="veteranNoteDao")
@Transactional
public class VeteranNoteDaoImpl extends BaseEntityDaoImpl<VeteranNote> implements VeteranNoteDao {

	public VeteranNoteDaoImpl() {
		super(VeteranNote.class);
	}
}
