<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VIS <s:text name="user" /></title>	

<link rel="stylesheet" type="text/css" href="<s:url value='/css/displaytagLib.css'/>" />

<script type="text/javascript">

function onCancel() {
	window.location.href = '<s:url namespace="/" action="cancelUser" />';
}

function onNew() {
	window.location.href = '<s:url namespace="/" action="displayUser" />';
}

function onSave() {
	YAHOO.vts.container.wait.show();
	
	document.getElementById("userForm").action = "saveUser.action";
	return true;	
}

function onSearch() {
	YAHOO.vts.container.wait.show();
	
	document.getElementById("userForm").action = "searchUser.action";
	return true;
}

function editUser(id) {
	
	var link = '<s:url namespace="/" action="editUser" />';
	link = link + "?id=" + id;
	window.location.href = link;
}

function deleteUser(id) {
	
	if (confirm("Are you sure you want to delete this user?")) {
		var link = '<s:url namespace="/" action="deleteUser" />';
		link = link + "?id=" + id;
		window.location.href = link;		
	} 
}

</script>

</head>
<body>
	
	<h1><s:text name="user.title" /></h1>

	<s:form action="saveUser" id="userForm">
		<s:actionerror/>
		<s:actionmessage/>
		<table width="450" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td><font color=red>*</font><s:text name="person.firstName"/>:</td>
				<td><s:textfield name="user.firstName" cssClass="fieldset135" maxlength="64" /></td>
			</tr>
			<tr>
				<td><s:text name="person.middleName"/>:</td>
				<td><s:textfield name="user.middleName" cssClass="fieldset135" maxlength="64" /></td>
			</tr>
			<tr>
				<td><font color=red>*</font><s:text name="person.lastName"/>:</td>
				<td><s:textfield name="user.lastName" cssClass="fieldset135" maxlength="64" /></td>
			</tr>
			<tr>
				<td><font color=red>*</font><s:text name="person.email"/>:</td>
				<td><s:textfield name="user.email" cssClass="fieldset175" maxlength="64" /></td>
			</tr>
			<tr>
				<td><font color=red>*</font><s:text name="person.role"/>:</td>
				<td>
					<div id="checkBoxList">
						<s:checkboxlist theme="custom" name="user.roleIds" list="roleList" listKey="id" listValue="value" />
					</div>
				</td>
			</tr>
			<tr>
				<td><font color=red>*</font><s:text name="common.status"/>:</td>
				<td>
					<div id="radioButtonList">
						<s:radio name="user.active" list="activeList" listKey="active" listValue="value" />
					</div>
				</td>
			</tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr><td colspan="2">
				<s:hidden name="user.id" />
				<s:hidden name="user.insertTimestamp" />
				<s:hidden name="user.updateTimestamp" />
				<s:hidden name="user.createdBy.Id" />
				<s:hidden name="user.op" />
				<input type="button" name="cancel" value="<s:text name="button.cancel"/>" class="button" onclick="return onCancel();" >
				<s:reset name="reset" value="%{getText('button.reset')}" cssClass="button"/>
				<s:submit name="search" value="%{getText('button.search')}" cssClass="button" onclick="return onSearch();" />				
				<input type="button" name="new" value="<s:text name="button.new"/>" class="button" onclick="return onNew();" />
				<s:if test="user.op == 'insert'">
					<s:submit name="save" value="%{getText('button.save')}" cssClass="button" onclick="return onSave();" />
				</s:if>
				<s:elseif test="user.op == 'edit'">
					<s:submit name="save" value="%{getText('button.update')}" cssClass="button" onclick="return onSave();" />
				</s:elseif>
				
			</td></tr>	
		</table>
	</s:form>
	
	<div class="displaytag">
		<display:table name="users" defaultsort="1" defaultorder="ascending" export="false" 
			id="row" requestURI="pageUser.action" pagesize="10">
			<display:column sortable="false" title="Full Name">${row.firstName} ${row.lastName}</display:column>
			<display:column sortable="true" title="EMail" property="email" />
			<display:column sortable="true" title="Roles" property="roles" decorator="gov.utah.dts.det.displaytag.decorator.DisplayRoles" />
			<display:column title="Status" >
				<s:if test="%{#attr.row.active}">Active</s:if>
				<s:else>Inactive</s:else>
			</display:column>
			<display:column title="Action" style="text-align: center;">
				<s:a href="javascript:editUser(%{#attr.row.id});" title="Edit"><img src="<s:url value='/images/edit-icon.png'/>"/></s:a>
				<s:a href="javascript:deleteUser(%{#attr.row.id});" title="Delete"><img src="<s:url value='/images/delete-icon.png'/>"/></s:a>
			</display:column>			
		</display:table>
	</div>		
		  
</body>
</html>