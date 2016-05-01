package gov.utah.va.vts.dao.impl;

import gov.utah.va.vts.dao.VeteranAttachmentDao;
import gov.utah.va.vts.model.VeteranAttachment;

import org.springframework.stereotype.Repository;

/**
 * Implementation class for VeteranAttachmentDao interface.
 * 
 * @author HNGUYEN
 *
 */
@Repository (value="veteranAttachmentDao")
public class VeteranAttachmentDaoImpl extends BaseEntityDaoImpl<VeteranAttachment> implements VeteranAttachmentDao {

	public VeteranAttachmentDaoImpl() {
		super(VeteranAttachment.class);
	}

}
