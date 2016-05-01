package gov.utah.va.vts.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.utah.dts.det.util.NameUniqueException;
import gov.utah.dts.det.util.Util;
import gov.utah.dts.det.util.Validate;
import gov.utah.va.vts.model.RecordType;

/**
 * RecordType action class.
 * 
 * @author HNGUYEN
 *
 */
public class RecordTypeAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	private RecordType recordType;
	
	private List<RecordType> recordTypes = new ArrayList<RecordType>();
	
	public String display() throws Exception {
		
		logger.debug("entering display() ...");
		
		recordType = new RecordType();
		recordType.setActive(new Integer(1));
		
		return INPUT;
	}
	
	public String save() throws Exception {
		try {
			// persists recordType 
			if (recordType.getId() == null) {	// insert
				recordType.setInsertTimestamp(new Date());
				recordType.setUpdateTimestamp(new Date());
			} else {	// update
				recordType.setUpdateTimestamp(new Date());
			}
			
			service.saveRecordType(recordType);
			
			recordTypes = service.findAllRecordTypes();
			
			addActionMessage(getText("message.save", Util.getStringArray(getText("recordType"))));
			recordType.setOp("");
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				throw new NameUniqueException("Name '" + recordType.getName() + "' already exists.");	// rm 28194
			} else {
				throw e;
			}
		}
		
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		
		logger.debug("entering edit...");
		
		Long id = new Long(request.getParameter("id"));
		recordType = service.findRecordTypeById(id);
		recordType.setOp("edit");
						
		return SUCCESS;
	}
	
	public String delete() throws Exception {
		
		logger.debug("entering delete ...");
		
		Long id = new Long(request.getParameter("id"));
		recordType = new RecordType();
		recordType.setId(id);
		
		try {
			service.deleteRecordType(recordType);
		} catch (Exception e) {	// rm 31662
			addActionError(getText("error.cannotDelete"));
			recordTypes = service.findAllRecordTypes();
			return INPUT;
		}
		
		recordTypes = service.findAllRecordTypes();		
		addActionMessage(getText("message.delete", Util.getStringArray(getText("recordType"))));
		recordType.setOp("");
		return SUCCESS;
	}
	
	public void validate() {
		
		if (request.getParameter("save") != null) {	// validate form only Save button is clicked
			if (Validate.isEmpty(recordType.getName())) addActionError(getText("error.required", Util.getStringArray(getText("common.name"))));
			if (Validate.isNull(recordType.getActive())) addActionError(getText("error.required", Util.getStringArray(getText("common.status"))));
			
			// redmine 28194 31662
			if ("insert".equals(recordType.getOp())) {	// check for insert mode only
				RecordType entity = service.findRecordTypeByName(recordType.getName());
				if (entity != null) {
					addActionError(getText("error.duplicateName", Util.getStringArray("Name")));
				}	
			}
		}
	}

	public void setRecordType(RecordType recordType) {
		this.recordType = recordType;
	}

	public RecordType getRecordType() {
		return recordType;
	}
	
	public void setRecordTypes(List<RecordType> recordTypes) {
		this.recordTypes = recordTypes;
	}

	public List<RecordType> getRecordTypes() {
		recordTypes = service.findAllRecordTypes();
		return recordTypes;
	}
	
}
