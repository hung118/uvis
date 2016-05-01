<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VIS <s:text name="docType" /></title>	

<link rel="stylesheet" type="text/css" href="<s:url value='/css/displaytagLib.css'/>" />

<script type="text/javascript">

function onCancel() {
	window.location.href = '<s:url namespace="/" action="cancelDocType" />';
}

function onNew() {
	window.location.href = '<s:url namespace="/" action="displayDocType" />';
}

function onSave() {
	YAHOO.vts.container.wait.show();
	
	document.getElementById("docTypeForm").action = "saveDocType.action";
	return true;	
}

function editDocType(id) {
	
	var link = '<s:url namespace="/" action="editDocType" />';
	link = link + "?id=" + id;
	window.location.href = link;
}

function deleteDocType(id) {
	
	if (confirm("Are you sure you want to delete this doc type?")) {
		var link = '<s:url namespace="/" action="deleteDocType" />';
		link = link + "?id=" + id;
		window.location.href = link;		
	} 
}

</script>

</head>
<body>
	
	<h1><s:text name="docType.title" /></h1>

	<s:form action="saveDocType" id="docTypeForm">
		<s:actionerror/>
		<s:actionmessage/>
		<table width="450" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td><font color=red>*</font><s:text name="common.name"/>:</td>
				<td><s:textfield name="docType.name" cssClass="fieldset135" maxlength="64" /></td>
			</tr>
			<tr>
				<td><s:text name="common.description"/>:</td>
				<td><s:textfield name="docType.description" cssClass="fieldset200" maxlength="64" /></td>
			</tr>
			<tr>
				<td><font color=red>*</font><s:text name="common.status"/>:</td>
				<td>
					<div id="radioButtonList">
						<s:radio name="docType.active" list="activeList" listKey="active" listValue="value" />
					</div>
				</td>
			</tr>
			<tr><td colspan="2">
				<font style="color:gray;font-size:11px">Names for DD214 forms which have both image and OCR text files must contain string "Form 214"; otherwise those names must not contain that string.</font>
			</td></tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr><td colspan="2">
				<s:hidden name="docType.id" />
				<s:hidden name="docType.insertTimestamp" />
				<s:hidden name="docType.updateTimestamp" />
				<s:hidden name="docType.op" />
				<input type="button" name="cancel" value="<s:text name="button.cancel"/>" class="button" onclick="return onCancel();" >
				<s:reset name="reset" value="%{getText('button.reset')}" cssClass="button"/>
				<input type="button" name="new" value="<s:text name="button.new"/>" class="button" onclick="return onNew();" />
				<s:if test="docType.op == 'insert'">
					<s:submit name="save" value="%{getText('button.save')}" cssClass="button" onclick="return onSave();" />
				</s:if>
				<s:elseif test="docType.op == 'edit'">
					<s:submit name="save" value="%{getText('button.update')}" cssClass="button" onclick="return onSave();" />
				</s:elseif>
				
			</td></tr>	
		</table>
	</s:form>
	
	<div class="displaytag">
		<display:table name="docTypes" export="false" 
			id="row" requestURI="pageDocType.action">
			<display:column title="Name" property="name" />
			<display:column title="Description" property="description" />
			<display:column title="Status" >
				<s:if test="%{#attr.row.active}">Active</s:if>
				<s:else>Inactive</s:else>
			</display:column>
			<display:column title="Action" style="text-align: center;">
				<s:a href="javascript:editDocType(%{#attr.row.id});" title="Edit"><img src="<s:url value='/images/edit-icon.png'/>"/></s:a>
				<s:a href="javascript:deleteDocType(%{#attr.row.id});" title="Delete"><img src="<s:url value='/images/delete-icon.png'/>"/></s:a>
			</display:column>			
		</display:table>
	</div>		
		  
</body>
</html>