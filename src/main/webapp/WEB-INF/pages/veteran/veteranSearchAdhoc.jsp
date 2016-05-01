<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VIS <s:text name="veteranSearch" /></title>	

<link rel="stylesheet" type="text/css" href="<s:url value='/css/displaytagLib.css'/>" />
<script type="text/javascript" src="<s:url value='/scripts/jquery-1.11.1.min.js'/>"></script>
<script type="text/javascript" src="<s:url value='/scripts/jquery.maskedinput-1.3.1.min.js'/>"></script>

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
	window.location.href = '<s:url namespace="/" action="newSearchVeteranSearch?adhoc=1" />';
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
	var link = '<s:url namespace="/report" action="generateMailingAdhocListReport" />';
	window.location.href = link;
}

function onXlsReport() {
	var link = '<s:url namespace="/report" action="generateXlsAdhocReport" />';
	window.location.href = link;
}

function onCsvReport() {
	var link = '<s:url namespace="/report" action="generateCsvAdhocReport" />';
		
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

var objCount = 0;
function buildSearch() {
	var template = "";
	var objText = $("#veteranSelect option:selected").text();
	
	if (objText == "Date of Birth") {
		template = getTemplate(2);
		template.find("#nameId").attr("value", objText);
		template.find("#remove").attr("href", "javascript:removeSearchRow(" + objCount + ");");
		$("#fieldsHolder").append(template);
		$("#valueId1").mask("99/99/9999");
		$("#valueId2").mask("99/99/9999");
	} else if (objText == "State") {
		template = getTemplate(3);
		template.find("#nameId").attr("value", objText);
		template.find("#remove").attr("href", "javascript:removeSearchRow(" + objCount + ");");
		$("#fieldsHolder").append(template);
		$("#valueId").mask("aa");
	} else if (objText == "Zip") {
		template = getTemplate(4);
		template.find("#nameId").attr("value", objText);
		$("#fieldsHolder").append(template);
	} else if (objText == "Gender") {
		template = getTemplate(5);
		template.find("#nameId").attr("value", objText);
		template.find("#remove").attr("href", "javascript:removeSearchRow(" + objCount + ");");
		$("#fieldsHolder").append(template);
	} else if (objText == "Current" || objText == "Deceased" || objText == "Receiving Compensation and Pension" || 
			objText == "Enrolled in VA Medical" || objText == "Verified" || objText == "Reviewed") {
		template = getTemplate(6);
		template.find("#nameId").attr("value", objText);
		template.find("#remove").attr("href", "javascript:removeSearchRow(" + objCount + ");");
		$("#fieldsHolder").append(template);
	} else if (objText == "Zip Code Designation") {
		template = getTemplate(7);
		template.find("#nameId").attr("value", objText);
		template.find("#remove").attr("href", "javascript:removeSearchRow(" + objCount + ");");
		$("#fieldsHolder").append(template);
	} else if (objText == "Source") {
		template = getTemplate(8);
		template.find("#nameId").attr("value", objText);
		template.find("#remove").attr("href", "javascript:removeSearchRow(" + objCount + ");");
		$("#fieldsHolder").append(template);
		
		// then add option values to select from record type list
		for (var i = 0; i < $("#recordTypeList option").length; i++) {
			$('#sourceId').append('<option value="' + $("#recordTypeList option:eq(" + i + ")").val() + '">' + $("#recordTypeList option:eq(" + i + ")").text() + '</option>');
		}
	} else {
		template = getTemplate(1);
		template.find("#nameId").attr("value", objText);
		template.find("#remove").attr("href", "javascript:removeSearchRow(" + objCount + ");");
		$("#fieldsHolder").append(template);
	}
	
	objCount = objCount + 1;
}

var sortCount = 0;
function buildSort() {	
	var objText = $("#sortedBy option:selected").text();
	var template = getTemplateSort();
	template.find("#nameId").attr("value", objText);
	template.find("#remove").attr("href", "javascript:removeSortRow(" + sortCount + ");");
	$("#sortHolder").append(template);
	sortCount = sortCount + 1;
}

function getTemplate(templNum) {
	var template = $("<tr id='" + objCount + "'><td><input type='text' id='nameId' name='reportProperty.objectNames' size='20' class='gray' onclick='blur();'>" +
			"<select id='opId' name='reportProperty.operatorsArray'><option value='1'>=</option><option value='2'>Starts With</option><option value='3'>Ends With</option><option value='4'>Contains</option></select>" +
			"<input type='text' id='valueId' name='reportProperty.objectValues' size='20'>" +
			" <a id='remove' title='Remove' href=''>Remove</a></td></tr>");
	var templateDate = $("<tr id='" + objCount + "'><td><input type='text' id='nameId' name='reportProperty.objectNames' size='20' class='gray' onclick='blur();'>" +
			"<select id='opId' name='reportProperty.operatorsArray'><option value='99'>Between</option></select>" +
			"<input type='text' id='valueId1' name='reportProperty.objectValues' size='10'> and <input type='text' id='valueId2' name='reportProperty.objectValues' size='10'>" +
			" <a id='remove' title='Remove' href=''>Remove</a></td></tr>");
	var templateState = $("<tr id='" + objCount + "'><td><input type='text' id='nameId' name='reportProperty.objectNames' size='20' class='gray' onclick='blur();'>" +
			"<select id='opId' name='reportProperty.operatorsArray'><option value='1'>=</option></select>" +
			"<input type='text' id='valueId' name='reportProperty.objectValues' size='5'>" +
			" <a id='remove' title='Remove' href=''>Remove</a></td></tr>");
	var templateZip = $("<tr id='100'><td><input type='text' id='nameId' name='reportProperty.objectNames' size='20' class='gray' onclick='blur();'>" +
			"<select id='opId' name='reportProperty.operatorsArray' onchange='showIns(this.value);'><option value='1'>=</option><option value='2'>Starts With</option><option value='3'>Ends With</option><option value='4'>Contains</option><option value='5'>Is In List</option></select>" +
			"<input type='text' id='valueId' name='reportProperty.objectValues' size='20'>" +
			" <a id='remove' title='Remove' href='javascript:removeSearchRow(100)'>Remove</a></td></tr>");
	var templateGender = $("<tr id='" + objCount + "'><td><input type='text' id='nameId' name='reportProperty.objectNames' size='20' class='gray' onclick='blur();'>" +
			"<select id='opId' name='reportProperty.operatorsArray'><option value='1'>=</option></select>" +
			"<select id='valueId' name='reportProperty.objectValues'><option value='Male'>Male</option><option value='Female'>Female</option></select>" +
			" <a id='remove' title='Remove' href=''>Remove</a></td></tr>");
	var templateTrueFalse = $("<tr id='" + objCount + "'><td><input type='text' id='nameId' name='reportProperty.objectNames' size='20' class='gray' onclick='blur();'>" +
			"<select id='opId' name='reportProperty.operatorsArray'><option value='1'>=</option></select>" +
			"<select id='valueId' name='reportProperty.objectValues'><option value='true'>True</option><option value='false'>False</option></select>" +
			" <a id='remove' title='Remove' href=''>Remove</a></td></tr>");
	var templateRural = $("<tr id='" + objCount + "'><td><input type='text' id='nameId' name='reportProperty.objectNames' size='20' class='gray' onclick='blur();'>" +
			"<select id='opId' name='reportProperty.operatorsArray'><option value='1'>=</option></select>" +
			"<select id='valueId' name='reportProperty.objectValues'><option value='U'>Urban</option><option value='R'>Rural</option><option value='H'>Highly Rural</option></select>" +
			" <a id='remove' title='Remove' href=''>Remove</a></td></tr>");
	var templateSource = $("<tr id='" + objCount + "'><td><input type='text' id='nameId' name='reportProperty.objectNames' size='20' class='gray' onclick='blur();'>" +
			"<select id='opId' name='reportProperty.operatorsArray'><option value='1'>=</option></select>" +
			"<input type='hidden' name='reportProperty.objectValues' value='Source'>" +
			"<select id='sourceId' name='reportProperty.sourceArray' multiple='true' rows='3'></select>" +
			" <a id='remove' title='Remove' href=''>Remove</a></td></tr>");
	
	var ret = "";
	switch (templNum) {
	case 1:
		ret = template;
		break;
	case 2:
		ret = templateDate;
		break;
	case 3:
		ret = templateState;
		break;
	case 4:
		ret = templateZip;
		break;
	case 5:
		ret = templateGender;
		break;
	case 6:
		ret = templateTrueFalse;
		break;
	case 7:
		ret = templateRural;
		break;
	case 8:
		ret = templateSource;
		break;
	}
	
	return ret;
}

function getTemplateSort() {
	var template = $("<tr id='s" + sortCount + "'><td><input type='text' id='nameId' name='reportProperty.sortedByArray' size='20' class='gray' onclick='blur();'>" +
			"<select id='typeId' name='reportProperty.sortedTypeArray'><option value='asc'>Ascending</option><option value='desc'>Descending</option></select>" +
			" <a id='remove' title='Remove' href=''>Remove</a></td></tr>");
	
	return template;
}

function removeSearchRow(rowNum) {
	if (rowNum == 100) {
		$("#instruction").remove();
	}
	
	$("#" + rowNum).remove();	
}

function removeSortRow(rowNum) {
	$("#s" + rowNum).remove();	
}

function showIns(val) {
	var templateIns = $("<tr id='instruction'><td colspan='2'>Enter zip codes separated by spaces. For example: 84114 84107 84054</td></tr>");
	if (val == '5') {
		$("#fieldsHolder").append(templateIns);
	}
}

function redisplaySearch() {
	if ($("#objectNames") != undefined) {
		for (var i = 0; i < $("#objectNames option").length; i++) {
			objCount = i;
			var template = "";
			var objText = $("#objectNames option:eq(" + i + ")").text();
			var objValue = $("#objectValues option:eq(" + i + ")").val();
			var objOp = $("#operators option:eq(" + i + ")").val();
			
			if (objText == "Date of Birth") {
				template = getTemplate(2);
				template.find("#nameId").attr("value", objText);
				template.find("#valueId1").attr("value", objValue);
				var temp = i + 1;
				template.find("#valueId2").attr("value", $("#objectValues option:eq(" + temp + ")").val());
				i = i + 1;
				template.find("#remove").attr("href", "javascript:removeSearchRow(" + i + ");");
				$("#fieldsHolder").append(template);
				$("#valueId1").mask("99/99/9999");
				$("#valueId2").mask("99/99/9999");
			} else if (objText == "State") {
				template = getTemplate(3);
				template.find("#nameId").attr("value", objText);
				template.find("#valueId").attr("value", objValue);
				template.find("#remove").attr("href", "javascript:removeSearchRow(" + i + ");");
				$("#fieldsHolder").append(template);
				$("#valueId").mask("aa");
			} else if (objText == "Zip") {
				template = getTemplate(4);
				template.find("#nameId").attr("value", objText);
				template.find("#valueId").attr("value", objValue);
				template.find("#opId").val(objOp);
				$("#fieldsHolder").append(template);
			} else if (objText == "Gender") {
				template = getTemplate(5);
				template.find("#nameId").attr("value", objText);
				template.find("#valueId").val(objValue);
				template.find("#remove").attr("href", "javascript:removeSearchRow(" + i + ");");
				$("#fieldsHolder").append(template);
			} else if (objText == "Current" || objText == "Deceased" || objText == "Receiving Compensation and Pension" || 
					objText == "Enrolled in VA Medical" || objText == "Verified" || objText == "Reviewed") {
				template = getTemplate(6);
				template.find("#nameId").attr("value", objText);
				template.find("#valueId").val(objValue);
				template.find("#remove").attr("href", "javascript:removeSearchRow(" + i + ");");
				$("#fieldsHolder").append(template);
			} else if (objText == "Zip Code Designation") {
				template = getTemplate(7);
				template.find("#nameId").attr("value", objText);
				template.find("#valueId").val(objValue);
				template.find("#remove").attr("href", "javascript:removeSearchRow(" + i + ");");
				$("#fieldsHolder").append(template);
			} else if (objText == "Source") {
				template = getTemplate(8);
				template.find("#nameId").attr("value", objText);
				template.find("#remove").attr("href", "javascript:removeSearchRow(" + i + ");");
				$("#fieldsHolder").append(template);
				
				// add option values to select from record type list and set values that have been selected
				for (var j = 0; j < $("#recordTypeList option").length; j++) {
					var found = false;
					for (var k = 0; k < $("#sourceArray option").length; k++) {
						if ($("#sourceArray option:eq(" + k + ")").val() == $("#recordTypeList option:eq(" + j + ")").val()) {
							$('#sourceId').append('<option selected value="' + $("#recordTypeList option:eq(" + j + ")").val() + '">' + $("#recordTypeList option:eq(" + j + ")").text() + '</option>');
							found = true;
							break;
						}
					}
					if (!found) {
						$('#sourceId').append('<option value="' + $("#recordTypeList option:eq(" + j + ")").val() + '">' + $("#recordTypeList option:eq(" + j + ")").text() + '</option>');
					}
				}				
			} else {
				template = getTemplate(1);
				template.find("#nameId").attr("value", objText);
				template.find("#valueId").attr("value", objValue);
				template.find("#opId").val(objOp);
				template.find("#remove").attr("href", "javascript:removeSearchRow(" + i + ");");
				$("#fieldsHolder").append(template);
			}
		}
	}
	
	if ($("#sortBys") != undefined) {
		for (var i = 0; i < $("#sortBys option").length; i++) {
			sortCount = i;
			var nameIdText = $("#sortBys option:eq(" + i + ")").text();
			var typeIdValue = $("#sortTypes option:eq(" + i + ")").text();
			var template = getTemplateSort();
			template.find("#nameId").attr("value", nameIdText);
			template.find("#typeId").val(typeIdValue);
			template.find("#remove").attr("href", "javascript:removeSortRow(" + sortCount + ");");
			$("#sortHolder").append(template);
			sortCount = sortCount + 1;
		}
	}
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
		<h1>Ad Hoc Search Veteran</h1>
		<s:actionerror/>
		<s:actionmessage/>
		<table width="450" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td>Fields to Filter:</td>
				<td>
					<select id="veteranSelect" name="veteran" onchange="buildSearch();">
					    <option value=""></option>
					    <option value="reportProperty.objectNames">Last Name</option>
					    <option value="reportProperty.objectNames">First Name</option>
					    <option value="reportProperty.objectNames">SSN</option>
					    <option value="reportProperty.objectNames">Street Address</option>
					    <option value="reportProperty.objectNames">City</option>
					    <option value="reportProperty.objectNames">State</option>
					    <option value="reportProperty.objectNames">Zip</option>
					    <option value="reportProperty.objectNames">Primary Phone</option>
					    <option value="reportProperty.objectNames">Alternate Phone</option>
					    <option value="reportProperty.objectNames">Date of Birth</option>
					    <option value="reportProperty.objectNames">Email</option>
					    <option value="reportProperty.objectNames">Gender</option>
					    <option value="reportProperty.objectNames">Source</option>
					    <option value="reportProperty.objectNames">Current</option>
					    <option value="reportProperty.objectNames">Deceased</option>
					    <option value="reportProperty.objectNames">Receiving Compensation and Pension</option>
					    <option value="reportProperty.objectNames">Enrolled in VA Medical</option>
					    <option value="reportProperty.objectNames">Verified</option>
					    <option value="reportProperty.objectNames">Reviewed</option>
					    <option value="reportProperty.objectNames">Zip Code Designation</option>
					</select>
				</td>
			</tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr>
				<td colspan="2">
					<table width="450" border="0" cellspacing="0" cellpadding="2" id="fieldsHolder">

					</table>
				</td>
			</tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr>
				<td>Sorted By:</td>
				<td>
					<select id="sortedBy" name="sortedBy" onchange="buildSort();">
						<option value=""></option>
					    <option value="last_name">Last Name</option>
					    <option value="first_name">First Name</option>
					    <option value="city">City</option>
					    <option value="ssn">SSN</option>
					    <option value="zip">Zip</option>
					    <option value="date_of_birth">Date of Birth</option>
					    <option value="r.name">Source</option>
					    <option value="va_current">Current</option>
					    <option value="verified">Verified</option>
					    <option value="reviewed">Reviewed</option>
					    <option value="va_enrolled">Receiving Compensation and Pension</option>
					    <option value="va_med_enrolled">Enrolled in VA Medical</option>
					</select>
				</td>
			</tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr>
				<td colspan="2">
					<table width="450" border="0" cellspacing="0" cellpadding="2" id="sortHolder">

					</table>
				</td>
			</tr>
			<tr><td colspan="2">&nbsp;
				<s:select id="recordTypeList" name="recordTypeList" list="recordTypeList" listKey="id" listValue="value" cssStyle="display:none"/>
				<s:if test="reportProperty.objectNames != null">
					<s:select id="objectNames" name="objectNames" list="reportProperty.objectNames" cssStyle="display:none"/>
					<s:select id="operators" name="operatorsArray" list="reportProperty.operatorsArray" cssStyle="display:none"/>
					<s:select id="objectValues" name="objectValues" list="reportProperty.objectValues" cssStyle="display:none"/>
					<s:select id="sortBys" name="sortBys" list="reportProperty.sortedByArray" cssStyle="display:none"/>
					<s:select id="sortTypes" name="sortTypes" list="reportProperty.sortedTypeArray" cssStyle="display:none"/>
				</s:if>
				<s:if test="reportProperty.sourceArray != null">
					<s:select id="sourceArray" name="sourceArray" list="reportProperty.sourceArray" cssStyle="display:none"/>
				</s:if>
			</td></tr>
			<tr><td colspan="2" style="text-align: right;">
				<input type="hidden" name="reportProperty.adhoc" value="true">
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
			<display:column title="Action" style="text-align: center;">
				<s:a href="javascript:editVeteran(%{#attr.row.id});" title="Edit"><img src="<s:url value='/images/edit-icon.png'/>"/></s:a>
				<s:a href="javascript:editCompareVeterans('%{#attr.row.id}');" title="Edit/Compare"><img src="<s:url value='/images/compare-icon.png'/>"/></s:a>
				<s:a href="javascript:viewVeteran(%{#attr.row.id});" title="View"><img src="<s:url value='/images/view-icon.gif'/>"/></s:a>
				<s:a href="javascript:deleteVeteran(%{#attr.row.id});" title="Delete"><img src="<s:url value='/images/delete-icon.png'/>"/></s:a>
			</display:column>			
		</display:table>
		
		<br>

		<div align="right">
			<input type="button" name="saveQuery" value="<s:text name="button.saveQuery"/>" class="button" onclick="return onSaveQueryView();" >
			<input type="button" name="mailingList" value="<s:text name="button.mailingList"/>" class="button" onclick="return onMailingList();" >
			<input type="button" name="excel" value="<s:text name="button.excel"/>" class="button" onclick="return onXlsReport();" >	
			<input type="button" name="csv" value="<s:text name="button.csv"/>" class="button" onclick="return onCsvReport();" >
			<input type="button" name="pdf" value="<s:text name="button.pdf"/>" class="button" onclick="return popupWindow('<s:url namespace="/report" action="generatePdfAdhocReport" />', 'PDFReport', 820, 950);" >
			<input type="button" name="pdfImage" value="<s:text name="button.pdfImage"/>" class="button" onclick="return popupWindow('<s:url namespace="/report" action="generatePdfAdhocReport"><s:param name="image" value="%{'y'}" /></s:url>', 'PDFReport', 820, 950);" >
		</div>

		
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

<script type="text/javascript">
	$(document).ready(function() {
		redisplaySearch();
	});
</script>

</body>
</html>