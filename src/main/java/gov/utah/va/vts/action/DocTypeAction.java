package gov.utah.va.vts.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.utah.dts.det.util.NameUniqueException;
import gov.utah.dts.det.util.Util;
import gov.utah.dts.det.util.Validate;
import gov.utah.va.vts.model.DocType;

/**
 * docType action class.
 * 
 * @author HNGUYEN
 *
 */
public class DocTypeAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	private DocType docType;
	
	private List<DocType> docTypes = new ArrayList<DocType>();
	
	public String display() throws Exception {
		
		logger.debug("entering display() ...");
		
		docType = new DocType();
		docType.setActive(new Integer(1));
		
		return INPUT;
	}
	
	public String save() throws Exception {
		try {
			// persists docType 
			if (docType.getId() == null) {	// insert
				docType.setInsertTimestamp(new Date());
				docType.setUpdateTimestamp(new Date());
			} else {	// update
				docType.setUpdateTimestamp(new Date());
			}
			
			service.saveDocType(docType);
			
			docTypes = service.findAllDocTypes();
			
			addActionMessage(getText("message.save", Util.getStringArray(getText("docType"))));
			docType.setOp("");
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				throw new NameUniqueException("Name '" + docType.getName() + "' already exists.");	// rm 28194
			} else {
				throw e;
			}
		}

		return SUCCESS;
	}
	
	public String edit() throws Exception {
		
		logger.debug("entering edit...");
		
		Long id = new Long(request.getParameter("id"));
		docType = service.findDocTypeById(id);
		docType.setOp("edit");
						
		return SUCCESS;
	}
	
	public String delete() throws Exception {
		
		logger.debug("entering delete ...");
		
		Long id = new Long(request.getParameter("id"));
		docType = new DocType();
		docType.setId(id);
		
		try {
			service.deleteDocType(docType);
		} catch (Exception e) {	// rm 31662
			addActionError(getText("error.cannotDelete"));
			docTypes = service.findAllDocTypes();
			return INPUT;
		}
		
		docTypes = service.findAllDocTypes();		
		addActionMessage(getText("message.delete", Util.getStringArray(getText("docType"))));
		docType.setOp("");
		return SUCCESS;
	}
	
	public void validate() {
		
		if (request.getParameter("save") != null) {	// validate form only Save button is clicked
			if (Validate.isEmpty(docType.getName())) addActionError(getText("error.required", Util.getStringArray(getText("common.name"))));
			if (Validate.isNull(docType.getActive())) addActionError(getText("error.required", Util.getStringArray(getText("common.status"))));
			
			// redmine 28194 31662
			if ("insert".equals(docType.getOp())) {	// check for insert mode only
				DocType entity = service.findDocTypeByName(docType.getName());
				if (entity != null) {
					addActionError(getText("error.duplicateName", Util.getStringArray("Name")));
				}
			}
		}
	}

	public void setDocType(DocType docType) {
		this.docType = docType;
	}

	public DocType getDocType() {
		return docType;
	}
	
	public void setDocTypes(List<DocType> docTypes) {
		this.docTypes = docTypes;
	}

	public List<DocType> getDocTypes() {
		docTypes = service.findAllDocTypes();
		return docTypes;
	}
	
}
