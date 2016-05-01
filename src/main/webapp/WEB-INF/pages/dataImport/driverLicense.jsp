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

<!-- calendar files -->
<link rel="stylesheet" type="text/css" href="<s:url value='/calendar/jscal2.css'/>" />
<link rel="stylesheet" type="text/css" href="<s:url value='/calendar/border-radius.css'/>" />
<link rel="stylesheet" type="text/css" href="<s:url value='/calendar/steel/steel.css'/>" />
<script type="text/javascript" src="<s:url value='/calendar/jscal2.js'/>"></script>
<script type="text/javascript" src="<s:url value='/calendar/en.js'/>"></script>

<script type="text/javascript">

function onImport() {
	YAHOO.vts.container.wait.show();
	
	return true;	
}

function viewDataImport(id) {
	YAHOO.vts.container.wait.show();

	var link = '<s:url namespace="/data" action="viewDldDriverLicense" />';
	link = link + "?id=" + id;
	window.location.href = link;		
}

function deleteDataImport(id) {
	
	if (confirm("Are you sure you want to delete this record?")) {
		var link = '<s:url namespace="/data" action="deleteDldDriverLicense" />';
		link = link + "?id=" + id;
		window.location.href = link;		
	} 
}

</script>

</head>
<body>
	
	<h1><s:text name="data.dld" /> - SFTP</h1>

	<s:form action="importDldDriverLicense">
		<s:actionerror/>
		<s:actionmessage/>
		<table width="450" border="0" cellspacing="0" cellpadding="2">
			<s:if test="dataImport == null || dataImport.insertTimestamp == null">
				<tr><td colspan="2">
	With specified file date, clicking on Import button will connect to DPS SFTP server to download the available CSV file 
	(veteransReport########.dat where ######## is of format yyyyMMdd) and import its records to VTS database. This import is scheduled to run every Saturday at 11:00 pm.
				</td></tr>
				<tr><td colspan="2">&nbsp;</td></tr>
				<tr>
					<td colspan="2"><font color=red>*</font><s:text name="data.fileDate"/>: 
						<s:date name="dataImport.insertTimestamp" format="MM/dd/yyyy" var="importDate"/>
						<s:textfield name="dataImport.insertTimestamp" value="%{#importDate}" cssClass="fieldset75" maxlength="10" id="importDate"/>
						<img src="<s:url value='/calendar/calendar.gif'/>" alt="Click here to use popup calendar." name="f_trigger_a" width="20" height="14" id="f_trigger_a" style="cursor: pointer; border: 1px solid #6699CC;" title="Date selector MM/DD/YYYY"
			                onmouseover="this.style.background='#6699CC';" onmouseout="this.style.background=''" />
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
					<s:submit name="import" value="%{getText('button.import')}" cssClass="button" onclick="return onImport();" />
				</s:if>
			</td></tr>	
		</table>
	</s:form>
	
	<div class="displaytag">
		<display:table name="dataImports" export="false" id="row" requestURI="pageDriverLicense.action" pagesize="25">
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
		  
    <script type="text/javascript">
    //<![CDATA[
	
	      var cal = Calendar.setup({
	          onSelect: function(cal) { cal.hide() }
	      });
	      cal.manageFields("f_trigger_a", "importDate", "%m/%d/%Y");

    //]]></script>
		    
</body>
</html>