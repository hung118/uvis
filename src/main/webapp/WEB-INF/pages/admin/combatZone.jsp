<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VIS <s:text name="combatZone" /></title>	

<link rel="stylesheet" type="text/css" href="<s:url value='/css/displaytagLib.css'/>" />

<script type="text/javascript">

function onCancel() {
	window.location.href = '<s:url namespace="/" action="cancelCombatZone" />';
}

function onNew() {
	window.location.href = '<s:url namespace="/" action="displayCombatZone" />';
}

function onSave() {
	YAHOO.vts.container.wait.show();
	
	document.getElementById("combatZoneForm").action = "saveCombatZone.action";
	return true;	
}

function editCombatZone(id) {
	
	var link = '<s:url namespace="/" action="editCombatZone" />';
	link = link + "?id=" + id;
	window.location.href = link;
}

function deleteCombatZone(id) {
	
	if (confirm("Are you sure you want to delete this record type?")) {
		var link = '<s:url namespace="/" action="deleteCombatZone" />';
		link = link + "?id=" + id;
		window.location.href = link;		
	} 
}

</script>

</head>
<body>
	
	<h1><s:text name="combatZone.title" /></h1>

	<s:form action="saveCombatZone" id="combatZoneForm">
		<s:actionerror/>
		<s:actionmessage/>
		<table width="450" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td><font color=red>*</font><s:text name="common.name"/>:</td>
				<td><s:textfield name="combatZone.name" cssClass="fieldset135" maxlength="64" /></td>
			</tr>
			<tr>
				<td><s:text name="common.description"/>:</td>
				<td><s:textfield name="combatZone.description" cssClass="fieldset200" maxlength="64" /></td>
			</tr>
			<tr>
				<td><font color=red>*</font><s:text name="common.status"/>:</td>
				<td>
					<div id="radioButtonList">
						<s:radio name="combatZone.active" list="activeList" listKey="active" listValue="value" />
					</div>
				</td>
			</tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr><td colspan="2">
				<s:hidden name="combatZone.id" />
				<s:hidden name="combatZone.insertTimestamp" />
				<s:hidden name="combatZone.updateTimestamp" />
				<s:hidden name="combatZone.op" />
				<input type="button" name="cancel" value="<s:text name="button.cancel"/>" class="button" onclick="return onCancel();" >
				<s:reset name="reset" value="%{getText('button.reset')}" cssClass="button"/>
				<input type="button" name="new" value="<s:text name="button.new"/>" class="button" onclick="return onNew();" />
				<s:if test="combatZone.op == 'insert'">
					<s:submit name="save" value="%{getText('button.save')}" cssClass="button" onclick="return onSave();" />
				</s:if>
				<s:elseif test="combatZone.op == 'edit'">
					<s:submit name="save" value="%{getText('button.update')}" cssClass="button" onclick="return onSave();" />
				</s:elseif>
				
			</td></tr>	
		</table>
	</s:form>
	
	<div class="displaytag">
		<display:table name="combatZones" export="false" 
			id="row" requestURI="pageCombatZone.action">
			<display:column title="Name" property="name" />
			<display:column title="Description" property="description" />
			<display:column title="Status" >
				<s:if test="%{#attr.row.active}">Active</s:if>
				<s:else>Inactive</s:else>
			</display:column>
			<display:column title="Action" style="text-align: center;">
				<s:a href="javascript:editCombatZone(%{#attr.row.id});" title="Edit"><img src="<s:url value='/images/edit-icon.png'/>"/></s:a>
				<s:a href="javascript:deleteCombatZone(%{#attr.row.id});" title="Delete"><img src="<s:url value='/images/delete-icon.png'/>"/></s:a>
			</display:column>			
		</display:table>
	</div>		
		  
</body>
</html>