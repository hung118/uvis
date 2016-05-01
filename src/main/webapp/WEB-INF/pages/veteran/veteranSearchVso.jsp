<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VIS <s:text name="veteranSearch" /></title>	

<link rel="stylesheet" type="text/css" href="<s:url value='/css/displaytagLib.css'/>" />

<script type="text/javascript">

// To show wait message box. To call it just put YAHOO.vts.container.wait.show() line in js function
YAHOO.namespace("vts.container");

function init(){

	// Instantiate temporary Panel to display while waiting for external content to load.
	// To call it just put YAHOO.vts.container.wait.show() line in js function.
	if (!YAHOO.vts.container.wait) {
        YAHOO.vts.container.wait = new YAHOO.widget.Panel("wait",  
                                                    { width: "240px", 
                                                      fixedcenter: true, 
                                                      close: false, 
                                                      draggable: false, 
                                                      zindex:4,
                                                      modal: true,
                                                      visible: false
                                                    } 
                                                );
    
		YAHOO.vts.container.wait.setHeader("Loading, please wait...");
		YAHOO.vts.container.wait.setBody("<img src=\"<s:url value='/images/activity.gif'/>\"/>");
		YAHOO.vts.container.wait.render(document.body);
	}
}

YAHOO.util.Event.onDOMReady(init);

function onReset() {
	window.location.href = '<s:url namespace="/" action="newSearchVSOVeteranSearch" />';
}

function onSearch() {
	YAHOO.vts.container.wait.show();
		
	return true;
}

function viewVeteran(id) {
	YAHOO.vts.container.wait.show();
	
	var url = '<s:url namespace="/" action="viewVeteran" />';
	var values = {'id' : id};

	postToUrl(url, values);	
}

</script>

</head>
<body>

<s:form action="searchVeteranSearch" id="veteranSearchForm">
	<div id="rotatingPhoto">
		<p><a name="contentSkip" id="contentSkip"></a>
	    	<script language="javascript">rotatingPhoto(photo_array);</script>
	   	</p>
	   	<div class="clear"></div>
	</div>
	
	<div id="mainContent">
		<h1><s:text name="reportProperty.search" /></h1>
		<s:actionerror/>
		<s:actionmessage/>
		<table width="450" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td colspan="2">
					<s:text name="veteran.ssn"/>: <s:textfield name="reportProperty.ssn1" id="ssn1" onkeyup="return autoTab(this, 3, event);" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="reportProperty.ssn2" id="ssn2" onkeyup="return autoTab(this, 2, event);" cssClass="fieldset15" maxlength="2"/>-<s:textfield name="reportProperty.ssn3" id="ssn3" cssClass="fieldset50" maxlength="4"/>
				</td>
			</tr>

			<tr><td colspan="2">&nbsp;</td></tr>
			<tr><td colspan="2">
				<input type="hidden" name="reportProperty.vaCurrent" value="1">
				<s:reset name="reset" value="%{getText('button.reset')}" cssClass="button" onclick="onReset();"/>
				<s:submit name="search" value="%{getText('button.search')}" cssClass="primaryButton" onclick="return onSearch();"/> &nbsp;
			</td></tr>
		</table>
	
	</div>
	<div class="clear"></div>
	<div id="searchResult">
		<h1><s:text name="veteran.searchResult" /></h1>
		<p><s:property value="totalRecordsCount"/> veteran record found. Showing <s:property value="veterans.size()"/>.</p>
		
		<s:set name="pageSize" value="pageSize" />
		Page Size: <s:url id="pageSize10" namespace="/" action="setSessionPageSizeVeteranSearch"><s:param name="pageSize" value="%{'10'}" /></s:url><s:a href="%{pageSize10}">10</s:a>
		<s:url id="pageSize25" namespace="/" action="setSessionPageSizeVeteranSearch"><s:param name="pageSize" value="%{'25'}" /></s:url><s:a href="%{pageSize25}">25</s:a>
		<s:url id="pageSize50" namespace="/" action="setSessionPageSizeVeteranSearch"><s:param name="pageSize" value="%{'50'}" /></s:url><s:a href="%{pageSize50}">50</s:a>
		<s:url id="pageSizeAll" namespace="/" action="setSessionPageSizeVeteranSearch"><s:param name="pageSize" value="%{'all'}" /></s:url><s:a href="%{pageSizeAll}">All</s:a>
		<display:table name="veterans" defaultorder="ascending" export="false" 
			id="row" requestURI="pageVeteranSearch.action" pagesize="${pageSize}" style="width: 100%;">
			<display:column sortable="true" title="SSN" property="ssn" />
			<display:column sortable="true" title="Last Name" property="lastName" />
			<display:column sortable="true" title="First Name" property="firstName" />
			<display:column sortable="true" title="DOB" property="dateOfBirth" decorator="gov.utah.dts.det.displaytag.decorator.DisplayDate" />
			<display:column sortable="true" title="Source" property="recordType.name" />
			<display:column sortable="true" title="Verified" property="verified" style="text-align: center;" decorator="gov.utah.dts.det.displaytag.decorator.DisplayVerified" />
			<display:column sortable="true" title="Current" property="vaCurrent" style="text-align: center;" decorator="gov.utah.dts.det.displaytag.decorator.DisplayVaCurrent" />
			<display:column title="Action" style="text-align: center;">
				<s:a href="javascript:viewVeteran(%{#attr.row.id});">View</s:a>
			</display:column>			
		</display:table>
		
		<br>		
	</div>	
</s:form>	

</body>
</html>