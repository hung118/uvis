<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VIS <s:text name="report.title" /></title>	

<link rel="stylesheet" type="text/css" href="<s:url value='/css/displaytagLib.css'/>" />

<script type="text/javascript">

function runReport(id) {
	
	var link = '<s:url namespace="/report" action="runReport" />';
	link = link + "?id=" + id;
	window.location.href = link;
}

function deleteReport(id) {
	
	if (confirm("Are you sure you want to delete this report?")) {
		var link = '<s:url namespace="/report" action="deleteReport" />';
		link = link + "?id=" + id;
		window.location.href = link;		
	} 
}

</script>

</head>
<body>
	
	<h1><s:text name="report.title" /></h1>
	<s:if test="#session.reportTypeSession == 1">User Reports - Created by current user</s:if>
	<s:elseif test="#session.reportTypeSession == 2">System Reports - Sharable reports</s:elseif>
	<s:else>All Reports</s:else>
	
	<div class="displaytag">
		<display:table name="reportList" export="false" id="row" requestURI="">
			<display:column title="Report Name" property="reportName" sortable="true"/>
			<display:column title="Created Date" property="insertTimestamp" decorator="gov.utah.dts.det.displaytag.decorator.DisplayDate" sortable="true" />
			<display:column title="Created By" sortable="true">${row.user.lastName}, ${row.user.firstName}</display:column>
			<display:column title="Action" style="text-align: center;">
				<s:a href="javascript:runReport(%{#attr.row.id});" title="Run"><img src="<s:url value='/images/run-icon.png'/>"/></s:a>
				<s:a href="javascript:deleteReport(%{#attr.row.id});" title="Delete"><img src="<s:url value='/images/delete-icon.png'/>"/></s:a>
			</display:column>			
		</display:table>
	</div>		
		  
</body>
</html>