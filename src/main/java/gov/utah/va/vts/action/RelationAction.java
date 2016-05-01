package gov.utah.va.vts.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.utah.dts.det.util.NameUniqueException;
import gov.utah.dts.det.util.Util;
import gov.utah.dts.det.util.Validate;
import gov.utah.va.vts.model.Relation;

/**
 * relation action class.
 * 
 * @author HNGUYEN
 *
 */
public class RelationAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	private Relation relation;
	
	private List<Relation> relations = new ArrayList<Relation>();
	
	public String display() throws Exception {
		
		logger.debug("entering display() ...");
		
		relation = new Relation();
		relation.setActive(new Integer(1));
		
		return INPUT;
	}
	
	public String save() throws Exception {
		try {
			// persists relation 
			if (relation.getId() == null) {	// insert
				relation.setInsertTimestamp(new Date());
				relation.setUpdateTimestamp(new Date());
			} else {	// update
				relation.setUpdateTimestamp(new Date());
			}
			
			service.saveRelation(relation);
			
			relations = service.findAllRelations();
			
			addActionMessage(getText("message.save", Util.getStringArray(getText("relation"))));
			relation.setOp("");
		} catch (Exception e) {
			if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
				throw new NameUniqueException("Name '" + relation.getName() + "' already exists.");	// rm 28194
			} else {
				throw e;
			}
		}
		
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		
		logger.debug("entering edit...");
		
		Long id = new Long(request.getParameter("id"));
		relation = service.findRelationById(id);
		relation.setOp("edit");
						
		return SUCCESS;
	}
	
	public String delete() throws Exception {
		
		logger.debug("entering delete ...");
		
		Long id = new Long(request.getParameter("id"));
		relation = new Relation();
		relation.setId(id);
		
		try {
			service.deleteRelation(relation);
		} catch (Exception e) {	// rm 31662
			addActionError(getText("error.cannotDelete"));
			relations = service.findAllRelations();
			return INPUT;
		}
		
		relations = service.findAllRelations();		
		addActionMessage(getText("message.delete", Util.getStringArray(getText("relation"))));
		relation.setOp("");
		return SUCCESS;
	}
	
	public void validate() {
		
		if (request.getParameter("save") != null) {	// validate form only Save button is clicked
			if (Validate.isEmpty(relation.getName())) addActionError(getText("error.required", Util.getStringArray(getText("common.name"))));
			if (Validate.isNull(relation.getActive())) addActionError(getText("error.required", Util.getStringArray(getText("common.status"))));
			
			// redmine 28194 31662
			if ("insert".equals(relation.getOp())) {	// check for insert mode only
				Relation entity = service.findRelationByName(relation.getName());
				if (entity != null) {
					addActionError(getText("error.duplicateName", Util.getStringArray("Name")));
				}				
			}
		}
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public Relation getRelation() {
		return relation;
	}
	
	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}

	public List<Relation> getRelations() {
		relations = service.findAllRelations();
		return relations;
	}
	
}
