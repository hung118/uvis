<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VIS <s:text name="data.dld" /></title>	

<link rel="stylesheet" type="text/css" href="<s:url value='/css/displaytagLib.css'/>" />

<script type="text/javascript">

function onImport() {
	YAHOO.vts.container.wait.show();
	
	return true;	
}

function viewDataImport(id) {
	YAHOO.vts.container.wait.show();

	var link = '<s:url namespace="/data" action="viewDldFileDriverLicenseFile" />';
	link = link + "?id=" + id;
	window.location.href = link;		
}

function deleteDataImport(id) {
	
	if (confirm("Are you sure you want to delete this record?")) {
		var link = '<s:url namespace="/data" action="deleteDldFileDriverLicenseFile" />';
		link = link + "?id=" + id;
		window.location.href = link;		
	} 
}

</script>

</head>
<body>
	
	<h1><s:text name="data.dld" /> - File Upload</h1>

	<s:form action="importDldFileDriverLicenseFile" namespace="/data" method="post" enctype="multipart/form-data" id="fileUploadForm">
		<s:actionerror/>
		<s:actionmessage/>
		<table width="450" border="0" cellspacing="0" cellpadding="2">
			<s:if test="dataImport == null || dataImport.insertTimestamp == null">
				<tr><td colspan="2">
					This will upload the selected file and import its records to VTS database. 
				</td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td>Zip File:</td>
					<td>
						<s:file name="csvFile" label="CSV File" />
					</td>
				</tr>
			</s:if>
			<s:else>
				<tr>
					<td width="30%"><s:text name="data.importDate"/>:</td>
					<td width="70%">
						<s:date name="dataImport.insertTimestamp" format="MM/dd/yyyy HH:mm" />
					</td>
				</tr>	
				<tr>
					<td><s:text name="data.fileName" />: </td>
					<td>
						<s:property value="dataImport.fileName" /> 
					</td>
				</tr>
				<tr>
					<td><s:text name="data.recordCount" />: </td>
					<td>
						<s:property value="dataImport.recordCount" /> 
					</td>
				</tr>
				<tr>
					<td><s:text name="data.badRec" />: </td>
					<td>
						<s:property value="dataImport.badRec" /> 
					</td>
				</tr>	
				<tr>
					<td><s:text name="data.importedBy" />: </td>
					<td>
						<s:property value="dataImport.createdBy.firstName" /> <s:property value="dataImport.createdBy.lastName" /> 
					</td>
				</tr>	
				<tr>
					<td><s:text name="data.importStatus" />: </td>
					<td>
						<s:property value="dataImport.status" />
					</td>
				</tr>					
			</s:else>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr><td colspan="2">
				<input type="button" name="cancel" value="<s:text name="button.cancel"/>" class="button" onclick="history.back();" >
				<s:if test="dataImport == null">
					<s:reset name="reset" value="%{getText('button.reset')}" cssClass="button"/>
					<s:submit name="import_upload" value="%{getText('button.import')}" cssClass="button" onclick="return onImport();" />
				</s:if>
			</td></tr>	
		</table>
	</s:form>
	
	<div class="displaytag">
		<display:table name="dataImports" export="false" id="row" requestURI="pageDriverLicenseFile.action" pagesize="25">
			<display:column title="Date" property="updateTimestamp" decorator="gov.utah.dts.det.displaytag.decorator.DisplayDate" />
			<display:column title="File Name" property="fileName" />
			<display:column title="# Records" property="recordCount" style="text-align: right;" />
			<display:column title="# Bad" property="badRec" style="text-align: right;" />
			<display:column title="Success" style="text-align: center;">
				<s:if test="%{#attr.row.status == 'Success'}">Y</s:if>
				<s:else><font color=red>N</font></s:else>
			</display:column>
			<display:column title="Action" style="text-align: center;">
				<s:a href="javascript:viewDataImport(%{#attr.row.id});" title="View Details"><img src="<s:url value='/images/view-icon.gif'/>"/></s:a>
				<s:a href="javascript:deleteDataImport(%{#attr.row.id});" title="Delete Message"><img src="<s:url value='/images/delete-icon.png'/>"/></s:a>
			</display:column>
		</display:table>
	</div>		
		  
</body>
</html>