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
	
	// Instantiate save query panel from markup
	YAHOO.vts.container.panel1 = new YAHOO.widget.Panel("panel1", {
		width: "310px", 
        fixedcenter: true, 
        close: false, 
        draggable: true, 
        zindex:4,
        modal: true,
        visible: false
	});
	YAHOO.vts.container.panel1.render();
	
}

YAHOO.util.Event.onDOMReady(init);

function onCancel() {
	window.location.href = '<s:url namespace="/" action="cancelVeteran" />';
}

function onSearch() {
	YAHOO.vts.container.wait.show();
		
	return true;
}

function onNewSearch() {
	window.location.href = '<s:url namespace="/" action="newSearchVeteranSearch" />';
}

function onSaveQueryView() {
	YAHOO.vts.container.panel1.show();
}

function onCloseSaveQuery() {
	YAHOO.vts.container.panel1.hide();
}

function onSaveQuery() {
	if (document.getElementById("reportName").value == "") {
		document.getElementById("saveQueryErr").innerHTML = "<font color='red'>Query name is required.</font>";
		return false;
	}
	
	YAHOO.vts.container.wait.show();
	return true;
}

function onMailingList() {
	var link = '<s:url namespace="/report" action="generateMailingListReport" />';
	window.location.href = link;
}

function onXlsReport() {
	var link = '<s:url namespace="/report" action="generateXlsReport" />';
	window.location.href = link;
}

function onCsvReport() {
	var link = '<s:url namespace="/report" action="generateCsvReport" />';
		
	//popupWindow(link, 'CSVReport', 820, 950);
	window.location.href = link;
}

function editVeteran(id) {
	YAHOO.vts.container.wait.show();
	
	var url = '<s:url namespace="/" action="editVeteran" />';
	var values = {'id' : id};

	postToUrl(url, values);
}

function editCompareVeterans(id) {
	YAHOO.vts.container.wait.show();
	
	var url = '<s:url namespace="/" action="editCompareVeterans" />';
	var values = {'id' : id};

	postToUrl(url, values);
}

function deleteVeteran(id) {
	
	if (confirm("Are you sure you want to delete this veteran?")) {
		var link = '<s:url namespace="/" action="deleteVeteran" />';
		link = link + "?id=" + id;
		window.location.href = link;		
	} 
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
			<tr><td colspan="2">
				<table class="multiBox">
					<tr>							
						<td><s:text name="recordType" />:</td>
						<td>
							<div id="checkBoxList">
								<s:checkboxlist name="reportProperty.sourceArray" list="recordTypeList" listKey="id" listValue="value" theme="custom" />
							</div>
						</td>
					</tr>				
				</table>			
			</td></tr>
			<tr>
				<td colspan="2">
					<s:checkbox name="reportProperty.vaEnrolled" cssClass="chkbox" /><s:text name="veteran.vaEnrolled"/><br />
					<s:checkbox name="reportProperty.vaMedEnrolled" cssClass="chkbox" /><s:text name="veteran.vaMedEnrolled"/>
					<s:checkbox name="reportProperty.deceased" cssClass="chkbox" /><s:text name="veteran.deceased"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div id="checkBoxList">
						<s:checkboxlist name="reportProperty.verifiedArray" list="#{1:'Verified',0:'Non-verified'}" />
						<s:checkboxlist name="reportProperty.reviewedArray" list="#{1:'Reviewed',0:'Unreviewed'}" />
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<div id="checkBoxList">
						<s:checkboxlist name="reportProperty.vaCurrentArray" list="#{1:'Current',0:'Non-current'}" />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<s:text name="veteran.rural"/>: <s:select name="reportProperty.rural" list="#{'U':'Urban','R':'Rural','H':'Highly Rural'}" headerKey="" headerValue="" />
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<s:text name="person.lastName"/>: <s:textfield name="reportProperty.lastName" cssClass="fieldset135"/>
					&nbsp; &nbsp;<s:text name="person.firstName"/>: <s:textfield name="reportProperty.firstName" cssClass="fieldset135"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<s:text name="veteran.address"/>: <s:textfield name="reportProperty.address1" cssClass="fieldset370"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<s:text name="veteran.city"/>: <s:textfield name="reportProperty.city" cssClass="fieldset120"/>
					<s:text name="veteran.state"/>: <s:select name="reportProperty.state" list="stateMap" headerKey="" headerValue="" />
					<s:text name="veteran.zip"/>: <s:textfield name="reportProperty.zip" cssClass="fieldset75" />
				</td>
			</tr>
			<tr>
				<td colspan="2"><s:text name="veteran.primaryPhone"/>: &nbsp;
					<s:textfield name="reportProperty.primaryPhone1" onkeyup="return autoTab(this, 3, event);" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="reportProperty.primaryPhone2" onkeyup="return autoTab(this, 3, event);" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="reportProperty.primaryPhone3" cssClass="fieldset50" maxlength="4"/>
				</td>
			</tr>
			<tr>
				<td colspan="2"><s:text name="veteran.altPhone"/>: 
					<s:textfield name="reportProperty.altPhone1" onkeyup="return autoTab(this, 3, event);" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="reportProperty.altPhone2" onkeyup="return autoTab(this, 3, event);" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="reportProperty.altPhone3" cssClass="fieldset50" maxlength="4"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<s:text name="veteran.ssn"/>: <s:textfield name="reportProperty.ssn1" onkeyup="return autoTab(this, 3, event);" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="reportProperty.ssn2" onkeyup="return autoTab(this, 2, event);" cssClass="fieldset15" maxlength="2"/>-<s:textfield name="reportProperty.ssn3" cssClass="fieldset50" maxlength="4"/>
					<s:text name="person.email"/>: <s:textfield name="reportProperty.email" cssClass="fieldset125"/>
				</td>
			</tr>
			<tr>
				<td colspan="2"><s:text name="veteran.dob"/>: 
					<s:select name="reportProperty.month" list="monthMap" headerKey="" headerValue="MM" />/
					<s:select name="reportProperty.day" list="dayMap" headerKey="" headerValue="DD" />/
					<s:select name="reportProperty.year" list="yearMap" headerKey="" headerValue="YYYY" /> 
					&nbsp; &nbsp; &nbsp;<s:text name="veteran.gender"/>: <s:select name="reportProperty.gender" list="#{'Male':'Male','Female':'Female'}" headerKey="" headerValue="" />
				</td>
			</tr>
			<tr><td colspan="2">
				<table class="multiBox">
					<tr>							
						<td><s:text name="decorationMedal"/>:</td>
						<td>
							<div id="checkBoxList">
								<s:checkboxlist name="reportProperty.decorationMedalArray" list="decorationMedalList" listKey="id" listValue="value" theme="custom" />
							</div>
						</td>
					</tr>				
				</table>			
			</td></tr>
			<tr><td colspan="2">
				<table class="multiBox">
					<tr>							
						<td><s:text name="veteran.serviceEra"/>:</td>
						<td>
							<div id="checkBoxList">
								<s:checkboxlist name="reportProperty.serviceEraId" list="serviceEraList" listKey="id" listValue="value" theme="custom" />
							</div>
						</td>
					</tr>				
				</table>			
			</td></tr>
			<tr>
				<td><s:text name="veteran.combatZone"/>:</td> 
				<td><s:select name="reportProperty.combatZoneId" list="combatZoneList" listKey="id" listValue="value" headerKey="" headerValue="" /></td>
			</tr>
			<tr>
				<td><s:text name="veteran.dischargeType"/>:</td> 
				<td><s:select name="reportProperty.dischargeTypeId" list="dischargeTypeList" listKey="id" listValue="value" headerKey="" headerValue="" /></td>
			</tr>
			<tr>
				<td><s:text name="serviceBranch"/>:</td> 
				<td><s:select name="reportProperty.serviceBranchId" list="serviceBranchList" listKey="id" listValue="value" headerKey="" headerValue="" /></td>
			</tr>
			<tr>
				<td><s:text name="veteran.servicePeriod" />:</td>
				<td>
					From: <s:select name="reportProperty.monthFrom" list="monthMap" headerKey="" headerValue="MM" />/
							<s:select name="reportProperty.dayFrom" list="dayMap" headerKey="" headerValue="DD" />/
							<s:select name="reportProperty.yearFrom" list="yearMap" headerKey="" headerValue="YYYY" /><br /> 
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>
					To: &nbsp; &nbsp; <s:select name="reportProperty.monthTo" list="monthMap" headerKey="" headerValue="MM" />/
						<s:select name="reportProperty.dayTo" list="dayMap" headerKey="" headerValue="DD" />/
						<s:select name="reportProperty.yearTo" list="yearMap" headerKey="" headerValue="YYYY" /> 
				</td>
			</tr>
			<tr>
				<td><s:text name="benefitType"/>:</td>
				<td><s:select name="reportProperty.benefitTypeId" id="benefitTypeId" list="benefitTypeList" listKey="id" listValue="value" headerKey="" headerValue="" /></td>
			</tr>
			<tr>
				<td><s:text name="veteran.note" />:</td> 
				<td><s:textfield name="reportProperty.note" cssClass="fieldset135"/></td>
			</tr>
			<tr>
				<td>Age Range:</td>
				<td><s:select name="reportProperty.ageRange" id="ageRangeId" list="ageRanges" headerKey="" headerValue="" /></td>
			</tr>

			<tr><td colspan="2">&nbsp;</td></tr>
			<tr><td colspan="2" style="text-align: right;">
				<input type="button" name="cancel" value="<s:text name="button.cancel"/>" class="button" onclick="return onCancel();" >
				<s:reset name="reset" value="%{getText('button.reset')}" cssClass="button"/>
				<input type="button" name="search" value="<s:text name="button.newSearch"/>" class="button" onclick="return onNewSearch();" >
				<s:submit name="search" value="%{getText('button.search')}" cssClass="primaryButton" onclick="return onSearch();"/> &nbsp;
			</td></tr>
		</table>
	
	</div>
	<div class="clear"></div>
	<div id="searchResult">
		<h1><s:text name="veteran.searchResult" /></h1>
		<p><s:property value="totalRecordsCount"/> veteran record(s) found. Showing <s:property value="veterans.size()"/>.</p>
		
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
			<logic:notPresent role="Read Only">
				<display:column title="Action" style="text-align: center;">
					<s:a href="javascript:editVeteran(%{#attr.row.id});" title="Edit"><img src="<s:url value='/images/edit-icon.png'/>"/></s:a>
					<s:a href="javascript:editCompareVeterans('%{#attr.row.id}');" title="Edit/Compare"><img src="<s:url value='/images/compare-icon.png'/>"/></s:a>
					<s:a href="javascript:viewVeteran(%{#attr.row.id});" title="View"><img src="<s:url value='/images/view-icon.gif'/>"/></s:a>
					<s:a href="javascript:deleteVeteran(%{#attr.row.id});" title="Delete"><img src="<s:url value='/images/delete-icon.png'/>"/></s:a>
				</display:column>			
			</logic:notPresent>
			<logic:present role="Read Only">
				<display:column title="Action" style="text-align: center;">
					<s:a href="javascript:viewVeteran(%{#attr.row.id});">View</s:a>
				</display:column>			
			</logic:present>
		</display:table>
		
		<br>

		<logic:notPresent role="Read Only">
			<div align="right">
				<input type="button" name="saveQuery" value="<s:text name="button.saveQuery"/>" class="button" onclick="return onSaveQueryView();" >
				<input type="button" name="mailingList" value="<s:text name="button.mailingList"/>" class="button" onclick="return onMailingList();" >
				<input type="button" name="excel" value="<s:text name="button.excel"/>" class="button" onclick="return onXlsReport();" >	
				<input type="button" name="csv" value="<s:text name="button.csv"/>" class="button" onclick="return onCsvReport();" >
				<input type="button" name="pdf" value="<s:text name="button.pdf"/>" class="button" onclick="return popupWindow('<s:url namespace="/report" action="generatePdfReport" />', 'PDFReport', 820, 950);" >
				<input type="button" name="pdfImage" value="<s:text name="button.pdfImage"/>" class="button" onclick="return popupWindow('<s:url namespace="/report" action="generatePdfReport"><s:param name="image" value="%{'y'}" /></s:url>', 'PDFReport', 820, 950);" >
			</div>
		</logic:notPresent>
		
	</div>	
</s:form>	
	  
<div id="panel1">
	<div class="hd">
		Save Query
	</div>
	<div class="bd">
		<s:form action="saveQueryVeteranSearch" id="saveQueryForm">	
			<span id="saveQueryErr"></span> 
					
			<table border="0" cellspacing="0" cellpadding="2">
				<tr>
					<td>Name: </td>
					<td>
						<s:textfield name="report.reportName" id="reportName" cssClass="fieldset225" />			
					</td>
				</tr>
				<tr>
					<td colspan="2">Share with other users: <s:checkbox name="report.sharable" cssClass="chkbox" /></td>
				</tr>
				<tr><td colspan="2">
					<div align="right">
						<input type="button" name="close" value="<s:text name="button.cancel"/>" class="button" onclick="return onCloseSaveQuery();" >
						<s:submit name="save" value="%{getText('button.save')}" cssClass="button" onclick="return onSaveQuery();" /> &nbsp;			
					</div>
				</td></tr>
			</table>
		</s:form>
	</div>
</div>  

</body>
</html>