package gov.utah.va.vts.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.utah.dts.det.util.Util;
import gov.utah.dts.det.util.Validate;
import gov.utah.va.vts.model.NameValue;
import gov.utah.va.vts.model.Role;
import gov.utah.va.vts.model.User;

/**
 * User action class.
 * 
 * @author HNGUYEN
 *
 */
public class UserAction extends BaseActionSupport {

	private static final long serialVersionUID = 1L;
	
	private User user;
	
	private List<User> users = new ArrayList<User>();
	
	public String display() throws Exception {
		
		logger.debug("entering display() ...");
		
		user = new User();
		//user.setActive(new Integer(1));
		
		return INPUT;
	}
	
	public String save() throws Exception {
		
		logger.debug("entering save()...");
		
		// set roles for persistence
		List<Role> roles = new ArrayList<Role>();
		for (int i = 0; i < user.getRoleIds().length; i++) {
			Role r = new Role();
			r.setId((user.getRoleIds())[i]);
			roles.add(r);
		}
		user.setRoles(roles);
		
		// persists user 
		boolean update = false;
		if (user.getId() == null) {	// insert
			user.setCreatedBy((User)request.getSession().getAttribute(getText("USER")));
			user.setInsertTimestamp(new Date());
			user.setUpdatedBy((User)request.getSession().getAttribute(getText("USER")));
			user.setUpdateTimestamp(new Date());
		} else {	// update
			user.setUpdatedBy((User)request.getSession().getAttribute(getText("USER")));
			user.setUpdateTimestamp(new Date());
			
			update = true;
		}
		
		service.saveUser(user);
		
		// update users
		if (update) {
			User userSearch = (User)request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
			users = service.findUsers(userSearch);
		}
		
		addActionMessage(getText("message.save", Util.getStringArray(getText("user"))));
		user.setOp("");
		
		return SUCCESS;
	}
	
	public String search() throws Exception {
		
		logger.debug("entering search...");
		
		users = service.findUsers(getUser());
		
		// save search user on session for later use
		request.getSession().setAttribute(getText("SEARCH_OBJ_SES"), getUser());
				
		return SUCCESS;
	}
	
	public String page() throws Exception {
		
		logger.debug("entering pageNavigate...");
		
		User userSearch = (User)request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
		users = service.findUsers(userSearch);
		
		user = null;
		
		return SUCCESS;
	}
	
	public String edit() throws Exception {
		
		logger.debug("entering edit...");
		
		Long id = new Long(request.getParameter("id"));
		user = service.findUserById(id);
		
		// set roleIds
		Long[] ris = new Long[user.getRoles().size()];
		for (int i = 0; i < user.getRoles().size(); i++) {
			Role r = user.getRoles().get(i);
			ris[i] = r.getId();
		}
		user.setRoleIds(ris);
		
		user.setOp("edit");
		return SUCCESS;
	}
	
	public String delete() throws Exception {
		Long id = new Long(request.getParameter("id"));
		user = new User();
		user.setId(id);
		
		User userSearch = (User)request.getSession().getAttribute(getText("SEARCH_OBJ_SES"));
		try {
			service.deleteUser(user);
		} catch (Exception e) {	// redmine 24634 comment #5
			addActionError(getText("error.cannotDelete"));
			users = service.findUsers(userSearch);
			return INPUT;
		}
				
		users = service.findUsers(userSearch);
		addActionMessage(getText("message.delete", Util.getStringArray(getText("user"))));
		user.setOp("");
		return SUCCESS;
	}
	
	public void validateSave() {
		if (Validate.isEmpty(user.getFirstName())) addActionError(getText("error.required", Util.getStringArray(getText("person.firstName"))));
		if (Validate.isEmpty(user.getLastName())) addActionError(getText("error.required", Util.getStringArray(getText("person.lastName"))));
		if (Validate.isEmpty(user.getEmail())) {
			addActionError(getText("error.required", Util.getStringArray(getText("person.email"))));
		} else {
			if (!Validate.isEmailValid(user.getEmail())) addActionError(getText("error.notValid", Util.getStringArray(getText("person.email"))));
		}
		if (user.getRoleIds().length == 0) addActionError(getText("error.required", Util.getStringArray(getText("person.role"))));
		
		// Redmine 13061
		user.setEmail(user.getEmail().toLowerCase());
		
		// validate unique email. Redmine 13337
		User u = service.findUserByEmail(user.getEmail());
		if (user.getId() == null) { // insert
			if (u != null) {
				addActionError(getText("error.duplicateEmail", Util.getStringArray(user.getEmail())));
			}
		} else {	// update
			if (u != null && !user.getId().equals(u.getId())) {
				addActionError(getText("error.duplicateEmail", Util.getStringArray(user.getEmail())));
			}
		}
		
		if (Validate.isNull(user.getActive())) addActionError(getText("error.required", Util.getStringArray(getText("common.status"))));
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	@SuppressWarnings("unchecked")
	public List<NameValue> getRoleList() {

		List<NameValue> roleList;
		
		if (request.getSession().getAttribute(getText("ROLE_LIST_SES")) == null) {
			List<Role> roles = service.listAllRoles();
			roleList = new ArrayList<NameValue>();
			for (int i = 0; i < roles.size(); i++) {
				Role r = roles.get(i);
				NameValue nv = new NameValue();
				nv.setId(r.getId());
				nv.setValue(r.getRoleName());
				roleList.add(nv);
			}
		
			request.getSession().setAttribute(getText("ROLE_LIST_SES"), roleList);
		} else {
			roleList = (ArrayList<NameValue>)request.getSession().getAttribute(getText("ROLE_LIST_SES"));
		}
		
		return roleList;
	}
	
	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<User> getUsers() {
		return users;
	}
	
}
