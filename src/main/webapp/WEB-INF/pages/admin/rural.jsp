<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VIS <s:text name="rural" /></title>	

<link rel="stylesheet" type="text/css" href="<s:url value='/css/displaytagLib.css'/>" />

<script type="text/javascript">

function onCancel() {
	window.location.href = '<s:url namespace="/" action="cancelRural" />';
}

function onNew() {
	window.location.href = '<s:url namespace="/" action="displayRural" />';
}

function onSave() {
	YAHOO.vts.container.wait.show();
	
	document.getElementById("ruralForm").action = "saveRural.action";
	return true;	
}

function onSearch() {
	YAHOO.vts.container.wait.show();
	
	document.getElementById("ruralForm").action = "searchRural.action";
	return true;
}

function editRural(zipCode, designation) {
	
	var link = '<s:url namespace="/" action="editRural" />';
	link = link + "?zipCode=" + zipCode + "&designation=" + designation;
	window.location.href = link;
}

function deleteRural(zipCode) {
	
	if (confirm("Are you sure you want to delete this rural crosswalk?")) {
		var link = '<s:url namespace="/" action="deleteRural" />';
		link = link + "?zipCode=" + zipCode;
		window.location.href = link;		
	} 
}

</script>

</head>
<body>
	
	<h1><s:text name="rural.title" /></h1>

	<s:form action="saveRural" id="ruralForm">
		<s:actionerror/>
		<s:actionmessage/>
		<table border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td><font color=red>*</font><s:text name="rural.zipCode"/>:</td>
				<td><s:textfield name="rural.name" cssClass="fieldset50" maxlength="5" /></td>
			</tr>
			<tr>
				<td><font color=red>*</font><s:text name="rural.designation"/>:</td>
				<td><s:select name="rural.value" list="#{'U':'Urban (U)','R':'Rural (R)','H':'Highly Rural (H)'}" headerKey="" headerValue=""/></td>
			</tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr><td colspan="2">
				<s:hidden name="rural.op" />
				<input type="button" name="cancel" value="<s:text name="button.cancel"/>" class="button" onclick="return onCancel();" >
				<s:reset name="reset" value="%{getText('button.reset')}" cssClass="button"/>
				<s:submit name="search" value="%{getText('button.search')}" cssClass="button" onclick="return onSearch();" />				
				<input type="button" name="new" value="<s:text name="button.new"/>" class="button" onclick="return onNew();" />
				<s:if test="rural.op == 'insert'">
					<s:submit name="save" value="%{getText('button.save')}" cssClass="button" onclick="return onSave();" />
				</s:if>
				<s:elseif test="rural.op == 'edit'">
					<s:submit name="save" value="%{getText('button.update')}" cssClass="button" onclick="return onSave();" />
				</s:elseif>
				
			</td></tr>	
		</table>
	</s:form>
	
	<div class="displaytag">
		<display:table name="rurals" defaultsort="1" defaultorder="ascending" export="false" 
			id="row" requestURI="pageRural.action" pagesize="50">
			<display:column sortable="true" title="Zip Code" property="name" />
			<display:column sortable="true" title="Designation" property="value" />
			<display:column title="Action" style="text-align: center;">
				<s:a href="javascript:editRural('%{#attr.row.name}', '%{#attr.row.value}');" title="Edit"><img src="<s:url value='/images/edit-icon.png'/>"/></s:a>
				<s:a href="javascript:deleteRural('%{#attr.row.name}');" title="Delete"><img src="<s:url value='/images/delete-icon.png'/>"/></s:a>
			</display:column>			
		</display:table>
	</div>		
		  
</body>
</html>