<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VIS <s:text name="veteranEditCompare" /></title>

<link rel="stylesheet" type="text/css" href="<s:url value='/css/editCompare.css'/>" />

<link rel="stylesheet" type="text/css" href="<s:url value='/css/displaytagLib.css'/>" />

<script type='text/javascript' src="<s:url value='/scripts/validate.js'/>"></script>

<script type='text/javascript' src="<s:url value='/dwr/engine.js'/>"></script>
<script type='text/javascript' src="<s:url value='/dwr/util.js'/>"></script>
<script type='text/javascript' src="<s:url value='/scripts/dtsUtil.js'/>"></script>
<script type='text/javascript' src="<s:url value='/dwr/interface/VeteranService.js'/>"></script>
<script type='text/javascript' src="<s:url value='/dwr/interface/VeteranBenefitType.js'/>"></script>
<script type='text/javascript' src="<s:url value='/dwr/interface/VeteranBenefitRecipient.js'/>"></script>

<script type="text/javascript">

//To show wait message box. To call it just put YAHOO.vts.container.wait.show() line in js function
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
	
	// Instantiate panel1 for service period section
	YAHOO.vts.container.panel1 = new YAHOO.widget.Panel("panel1", {
		width: "460px", 
        fixedcenter: true, 
        close: true, 
        draggable: true, 
        zindex:4,
        modal: true,
        visible: false
	});
	YAHOO.vts.container.panel1.render();

	// Instantiate panel2 for benefit type section
	YAHOO.vts.container.panel2 = new YAHOO.widget.Panel("panel2", {
		width: "390px", 
        fixedcenter: true, 
        close: true, 
        draggable: true, 
        zindex:4,
        modal: true,
        visible: false
	});
	YAHOO.vts.container.panel2.render();

	// Instantiate panel3 for benefit recipient section
	YAHOO.vts.container.panel3 = new YAHOO.widget.Panel("panel3", {
		width: "430px", 
        fixedcenter: true, 
        close: true, 
        draggable: true, 
        zindex:4,
        modal: true,
        visible: false
	});
	YAHOO.vts.container.panel3.render();
}

YAHOO.util.Event.onDOMReady(init);

function initDwr() {
	fillServiceTable();
	fillBenefitTypeTable();
}

var serviceCache = { };
var benefitTypeCache = { };
var benefitRecipientCache = { };
var viewed = -1;

function fillServiceTable() {
	var veteranId = dwr.util.getValue("veteranId");
	
	VeteranService.getVeteranServices(veteranId, function(data) {
	    // Delete all the rows except for servicePeriodPattern row
	    dwr.util.removeAllRows("servicePeriodBody", { filter:function(tr) {
	      return (tr.id != "servicePeriodPattern");
	    }});
	    
	    // create a new set cloned from servicePeriodPattern row
	    var veteranServicePeriod, id;
	    for (var i = 0; i < data.length; i++) {
	    	veteranServicePeriod = data[i];
	    	id = veteranServicePeriod.id;
	    	dwr.util.cloneNode("servicePeriodPattern", { idSuffix:id });
	    	dwr.util.setValue("serviceBranch" + id, veteranServicePeriod.serviceBranchName);
	    	dwr.util.setValue("serviceEra" + id, veteranServicePeriod.serviceEraName);
	    	dwr.util.setValue("serviceBegin" + id, veteranServicePeriod.startDateStr);
	    	dwr.util.setValue("serviceEnd" + id, veteranServicePeriod.endDateStr);
	    	
	        $("servicePeriodPattern" + id).style.display = ""; // officially we should use table-row, but IE prefers "" for some reason
	        serviceCache[id] = veteranServicePeriod;
	    }
	    
	});
}

function saveServicePeriod() {
		
	if (!validateService()) return false;
	
	var veteranId = dwr.util.getValue("veteranId");
	var veteranServicePeriod = { idService:viewed, serviceBranchId:null, serviceEraId:null, monthFrom:null, dayFrom:null, yearFrom:null, monthTo:null, dayTo:null, yearTo:null, combatZoneId:null, decorationMedal:null, dischargeTypeId:null, vaEnrolled:null, activeService:null, insertTimestampService:null };
	dwr.util.getValues(veteranServicePeriod);

	dwr.engine.beginBatch();
	VeteranService.saveVeteranService(veteranId, veteranServicePeriod);
	fillServiceTable();
	dwr.engine.endBatch({
		errorHandler:function(errorString, exception) { 
			alert("Error: " + errorString);
		}
	});
	
	YAHOO.vts.container.panel1.hide();
}

function onServicePeriodView() {
	clearServicePeriod();
	YAHOO.vts.container.panel1.show();
}

function onCloseServicePeriod() {
	YAHOO.vts.container.panel1.hide();
}

function clearServicePeriod() {
	viewed = -1;
	dwr.util.setValues({ idService:null, serviceBranchId:null, serviceEraId:null, monthFrom:null, dayFrom:null, yearFrom:null, monthTo:null, dayTo:null, yearTo:null, combatZoneId:null, decorationMedal:null, dischargeTypeId:null, vaEnrolled:null, activeService:null, insertTimestampService:null });
}

function editService(eleid) {
	  // we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
	  var veteranServicePeriod = serviceCache[eleid.substring(4)];
	  dwr.util.setValues(veteranServicePeriod);
	  
	  // set insertTimeStampService manually
	  dwr.util.setValue("insertTimestampService", dateFormat(veteranServicePeriod.insertTimestamp, "mm/dd/yyyy"));
	  
	  YAHOO.vts.container.panel1.show();
}

function deleteService(eleid) {
	  // we were an id of the form "delete{id}", eg "delete42". We lookup the "42"
	  var servicePeriodId = eleid.substring(6);
	  if (confirm("Are you sure you want to delete this service period?")) {
		clearServicePeriod();
	    dwr.engine.beginBatch();
	    VeteranService.deleteVeteranService(servicePeriodId);
	    fillServiceTable();
		dwr.engine.endBatch({
			  errorHandler:function(errorString, exception) { 
				alert("Error: " + errorString);
			  }
		});
	  }
}

function validateService() {
	
	var errorMsg = "Validation error:\n";
	var flag = 0;
		
	var monthFrom = document.getElementById("monthFrom").value;
	var dayFrom = document.getElementById("dayFrom").value;
	var yearFrom = document.getElementById("yearFrom").value;
	var monthTo = document.getElementById("monthTo").value;
	var dayTo = document.getElementById("dayTo").value;
	var yearTo = document.getElementById("yearTo").value;
	
	var dateFrom = monthFrom + "/" + dayFrom + "/" + yearFrom;
	if (dateFrom == "//") {
		// ok for no date
	} else if (!(dateFrom != "//" && dateFrom.length >= 8)) {
		errorMsg += "Service Period From is invalid. It must be complete date of format MM/DD/YYYY or leave it blank.\n";
		flag += 1;
	}

	var dateTo = monthTo + "/" + dayTo + "/" + yearTo;
	if (dateTo == "//") {
		// ok for no date
	} else if (!(dateTo != "//" && dateTo.length >= 8)) {
		errorMsg += "Service Period To is invalid. It must be complete date of format MM/DD/YYYY or leave it blank.\n";
		flag += 1;
	} 
	
	if (flag == 0) {
		if (!isFromBeforeTo(yearFrom, monthFrom, dayFrom, yearTo, monthTo, dayTo)) {
			errorMsg += "Service Period To date should be after Service Period From date.";
			flag += 1;
		}
	}

	if (flag > 0) {
		alert(errorMsg);
		return false;
	} else {
		return true;
	}
}

function fillBenefitTypeTable() {
	var veteranId = dwr.util.getValue("veteranId");
	
	VeteranBenefitType.getVeteranBenefitTypes(veteranId, function(data) {
	    // Delete all the rows except for benefitTypePattern row
	    dwr.util.removeAllRows("benefitTypeBody", { filter:function(tr) {
	      return (tr.id != "benefitTypePattern");
	    }});
	    
	    // create a new set cloned from benefitTypePattern row
	    var veteranBenefit, id;
	    for (var i = 0; i < data.length; i++) {
	    	veteranBenefit = data[i];
	    	id = veteranBenefit.id;
	    	dwr.util.cloneNode("benefitTypePattern", { idSuffix:id });
	    	
	    	var recipients = "&nbsp; &nbsp; &nbsp; &nbsp;";
	    	for (var j = 0; j < veteranBenefit.benefitRecipientList.length; j++) {
	    		recipients = recipients + veteranBenefit.benefitRecipientList[j].firstName + " " + veteranBenefit.benefitRecipientList[j].lastName +
	    			" - " + veteranBenefit.benefitRecipientList[j].address1 + ", " + veteranBenefit.benefitRecipientList[j].city + ", " + veteranBenefit.benefitRecipientList[j].state + " " + veteranBenefit.benefitRecipientList[j].zip;
	    		if (j < veteranBenefit.benefitRecipientList.length - 1) {
	    			recipients = recipients + "<br>&nbsp; &nbsp; &nbsp; &nbsp;";
	    		}
	    	}
	    	if (recipients == "&nbsp; &nbsp; &nbsp; &nbsp;") {
	    		dwr.util.setValue("benefitTypeName" + id, veteranBenefit.benefitTypeName);
	    	} else {
	    		dwr.util.setValue("benefitTypeName" + id, veteranBenefit.benefitTypeName + "<br>" + recipients, { escapeHtml:false });	
	    	}
	    	
	        $("benefitTypePattern" + id).style.display = ""; // officially we should use table-row, but IE prefers "" for some reason
	        benefitTypeCache[id] = veteranBenefit;
	    }
	    
	});
}

function saveBenefitType() {
	
	if (!validateBenefitType()) return false;
	
	var veteranId = dwr.util.getValue("veteranId");
	var benefitTypeId = dwr.util.getValue("benefitTypeId");
	
	var veteranBenefit = { idBenefitType:null, benefitTypeId:null, activeBenefitType:null, insertTimestampBenefitType:null };
	dwr.util.getValues(veteranBenefit);
	dwr.engine.beginBatch();
	VeteranBenefitType.saveVeteranBenefitType(veteranId, benefitTypeId, veteranBenefit);
	fillBenefitTypeTable();
	dwr.engine.endBatch({
		  errorHandler:function(errorString, exception) { 
			alert("Error: " + errorString);
		  }
	});
	
	YAHOO.vts.container.panel2.hide();
}

function onBenefitTypeView() {
	clearBenefitType();
	YAHOO.vts.container.panel2.show();
	
	// hide & show beneficiary table and buttons
	document.getElementById("beneficiaryTable").style.display = "none";
	document.getElementById("beneficiaryButton").style.display = "none";
	document.getElementById("beneficiaryButtons").style.display = "block";
}

function onCloseBenefitType() {
	YAHOO.vts.container.panel2.hide();
}

function clearBenefitType() {
	dwr.util.setValues({ idBenefitType:null, benefitTypeId:null, activeBenefitType:null, insertTimestampBenefitType:null });
}

function editBenefitType(eleid) {
	// we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
	var veteranBenefit = benefitTypeCache[eleid.substring(4)];
	veteranBenefit.benefitRecipientList = null;	// without this, it throws an exception (redmine 12083 #4)
	dwr.util.setValues(veteranBenefit);
	
	// set insertTimeStampBenefitType manually
	dwr.util.setValue("insertTimestampBenefitType", dateFormat(veteranBenefit.insertTimestamp, "mm/dd/yyyy"));
		  
	// get benefit recipient list
	fillBenefitRecipientTable();
	clearBenefitRecipient();
	
	YAHOO.vts.container.panel2.show();
		  
	// show & hide beneficiary section
	document.getElementById("beneficiaryTable").style.display = "block";
	document.getElementById("beneficiaryButton").style.display = "block";
	document.getElementById("beneficiaryButtons").style.display = "none";
}

function deleteBenefitType(eleid) {
	  // we were an id of the form "delete{id}", eg "delete42". We lookup the "42"
	  var veteranBenefitId = eleid.substring(6);
	  if (confirm("Are you sure you want to delete this benefit type?")) {
		clearBenefitType();
	    dwr.engine.beginBatch();
	    VeteranBenefitType.deleteVeteranBenefitType(veteranBenefitId);
	    fillBenefitTypeTable();
		dwr.engine.endBatch({
			  errorHandler:function(errorString, exception) { 
				alert("Error: " + errorString);
			  }
		});
	  }
}

function validateBenefitType() {
	if (document.getElementById("benefitTypeId").value == "") {
		alert("Benefit Type is not selected.");
		return false;
	} else {
		return true;
	}
}

function fillBenefitRecipientTable() {
	var veteranBenefitId = dwr.util.getValue("idBenefitType");
	
	VeteranBenefitRecipient.getVeteranBenefitRecipients(veteranBenefitId, function(data) {
	    // Delete all the rows except for benefitRecipientPattern row
	    dwr.util.removeAllRows("benefitRecipientBody", { filter:function(tr) {
	      return (tr.id != "benefitRecipientPattern");
	    }});
	    
	    // create a new set cloned from benefitRecipientPattern row
	    var veteranBenefitRecipient, id;
	    for (var i = 0; i < data.length; i++) {
	    	veteranBenefitRecipient = data[i];
	    	id = veteranBenefitRecipient.id;
	    	dwr.util.cloneNode("benefitRecipientPattern", { idSuffix:id });
	    	dwr.util.setValue("benefitRecipientBeneficiary" + id, veteranBenefitRecipient.firstName + " " + veteranBenefitRecipient.lastName);
	    	dwr.util.setValue("benefitRecipientAddress" + id, veteranBenefitRecipient.address1 + ", " + veteranBenefitRecipient.city + ", " + veteranBenefitRecipient.state + " " + veteranBenefitRecipient.zip);
	    	
	        $("benefitRecipientPattern" + id).style.display = ""; // officially we should use table-row, but IE prefers "" for some reason
	        benefitRecipientCache[id] = veteranBenefitRecipient;
	    }
	    
	});
}

function saveBenefitRecipient() {
	
	if (!validateBenefitRecipient()) return false;
	
	var veteranBenefitId = dwr.util.getValue("idBenefitType");
	var veteranBenefitRecipient = { idBenefitRecipient:null, veteranBenefitId:veteranBenefitId, relationId:null, firstName:null, lastName:null, address1:null, address2:null, city:null, state:null, zip:null, phone1:null, phone2:null, phone3:null, activeBenefitRecipient:null, insertTimestampBenefitRecipient:null };
	dwr.util.getValues(veteranBenefitRecipient);
	
	dwr.engine.beginBatch();
	VeteranBenefitRecipient.saveVeteranBenefitRecipient(veteranBenefitRecipient);
	fillBenefitRecipientTable();
	fillBenefitTypeTable();
	dwr.engine.endBatch({
		  errorHandler:function(errorString, exception) { 
			alert("Error: " + errorString);
		  }
	});
	
	YAHOO.vts.container.panel3.hide();
	YAHOO.vts.container.panel2.show();
}

function onBenefitRecipientView() {
	clearBenefitRecipient();
	YAHOO.vts.container.panel2.hide();
	YAHOO.vts.container.panel3.show();
}

function onCloseBenefitRecipient() {
	YAHOO.vts.container.panel3.hide();
	YAHOO.vts.container.panel2.show();
}

function clearBenefitRecipient() {
	dwr.util.setValues({ idBenefitRecipient:null, veteranBenefitId:null, relationId:null, firstName:null, lastName:null, address1:null, address2:null, city:null, state:'UT', zip:null, phone1:null, phone2:null, phone3:null, activeBenefitRecipient:null, insertTimestampBenefitRecipient:null });
}

function editBenefitRecipient(eleid) {
	  // we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
	  var veteranBenefitRecipient = benefitRecipientCache[eleid.substring(4)];
	  dwr.util.setValues(veteranBenefitRecipient);
	  
	  // set insertTimeStampBenefitRecipient manually
	  dwr.util.setValue("insertTimestampBenefitRecipient", dateFormat(veteranBenefitRecipient.insertTimestamp, "mm/dd/yyyy"));
	  
	  YAHOO.vts.container.panel2.hide();
	  YAHOO.vts.container.panel3.show();
}

function deleteBenefitRecipient(eleid) {
	  // we were an id of the form "delete{id}", eg "delete42". We lookup the "42"
	  var veteranBenefitRecipientId = eleid.substring(6);
	  if (confirm("Are you sure you want to delete this benefit recipient?")) {
		clearBenefitRecipient();
	    dwr.engine.beginBatch();
	    VeteranBenefitRecipient.deleteVeteranBenefitRecipient(veteranBenefitRecipientId);
	    fillBenefitRecipientTable();
	    fillBenefitTypeTable();
		dwr.engine.endBatch({
			  errorHandler:function(errorString, exception) { 
				alert("Error: " + errorString);
			  }
		});
	  }
}

function validateBenefitRecipient() {
	if (document.getElementById("firstName").value == "" && document.getElementById("lastName").value == "") {
		alert("Please enter benefit recipient first name or last name.");
		return false;
	} else {
		return true;
	}
}

function highLightOn(id, rowIndex, rowIndex2) {
	// determine the 2 char type name for highlight background color, eg. 0ddlastName ...
	var startIndex;
	var endIndex;
	if (rowIndex2 == undefined) {	// outer s:iterator
		if (rowIndex < 10) {
			startIndex = 1;
			endIndex = 3;
		} else {
			startIndex = 2;
			endIndex = 4;
		}		
	} else {	// service period or benefit nested s:iterator
		if (rowIndex < 10) {
			startIndex = 2;
			endIndex = 4;
		} else {
			startIndex = 3;
			endIndex = 5;
		}		
	}
	
	var typeName = id.substring(startIndex, endIndex);
	document.getElementById(id).style.cursor = "pointer";
	
	if (typeName == "ct") document.getElementById(id).style.backgroundColor = "#f7f792";
	else if (typeName == "dd") document.getElementById(id).style.backgroundColor = "#8dbdfc";
	else if (typeName == "dl") document.getElementById(id).style.backgroundColor = "#7aebb1";
	else if (typeName == "dt") document.getElementById(id).style.backgroundColor = "#58b1cb";
	else if (typeName == "dw") document.getElementById(id).style.backgroundColor = "#fc7e02";
	else if (typeName == "ma") document.getElementById(id).style.backgroundColor = "#c6acfd";
	else if (typeName == "on") document.getElementById(id).style.backgroundColor = "#daabab";
		
}

function highLightOff(id, rowIndex, rowIndex2) {
	// determine the 2 char type name for highlight background color, eg. 0ddlastName ...
	var startIndex;
	var endIndex;
	if (rowIndex2 == undefined) {	// outer s:iterator
		if (rowIndex < 10) {
			startIndex = 1;
			endIndex = 3;
		} else {
			startIndex = 2;
			endIndex = 4;
		}		
	} else {	// service period or benefit nested s:iterator
		if (rowIndex < 10) {
			startIndex = 2;
			endIndex = 4;
		} else {
			startIndex = 3;
			endIndex = 5;
		}		
	}
	
	var typeName = id.substring(startIndex, endIndex);
	if (typeName == "ct") document.getElementById(id).style.backgroundColor = "#fce805";
	else if (typeName == "dd") document.getElementById(id).style.backgroundColor = "#bdd8fd";
	else if (typeName == "dl") document.getElementById(id).style.backgroundColor = "#befbdc";
	else if (typeName == "dt") document.getElementById(id).style.backgroundColor = "#a3d7e6";
	else if (typeName == "dw") document.getElementById(id).style.backgroundColor = "#f1b57b";
	else if (typeName == "ma") document.getElementById(id).style.backgroundColor = "#dfd0fe";
	else if (typeName == "on") document.getElementById(id).style.backgroundColor = "#fad2d2";
}

function getEleValue(id, rowIndex) {
	var startIndex;
	var endIndex;
	if (rowIndex < 10) {
		startIndex = 1;
		endIndex = 3;
	} else {
		startIndex = 2;
		endIndex = 4;
	}
	
	var obj = document.getElementById("veteranForm_veteran_" + id.substring(endIndex));
	var eleValue = document.getElementById(id).innerHTML;
	var selectedIndexValue = -1;
	if (id.substring(endIndex) == "relation.id") {	// relation ship drop down
		for (var i = 0; i < obj.options.length; i++) {
			if (obj.options[i].text == eleValue) {
				selectedIndexValue = i;
				break;
			}
		}
		obj.selectedIndex = selectedIndexValue;
	} else if (id.substring(endIndex) == "ethnicity.id") {	// ethnicity drop down
		for (var i = 0; i < obj.options.length; i++) {
			if (obj.options[i].text == eleValue) {
				selectedIndexValue = i;
				break;
			}
		}
		obj.selectedIndex = selectedIndexValue;
	} else {
		obj.value = eleValue;	
	}
		
	// highlight color for input fields in proposed record.
	var typeName = id.substring(startIndex, endIndex);
	if (typeName == "ct") obj.style.backgroundColor = "#fce805";
	else if (typeName == "dd") obj.style.backgroundColor = "#bdd8fd";
	else if (typeName == "dl") obj.style.backgroundColor = "#befbdc";
	else if (typeName == "dt") obj.style.backgroundColor = "#a3d7e6";
	else if (typeName == "dw") obj.style.backgroundColor = "#f1b57b";
	else if (typeName == "ma") obj.style.backgroundColor = "#dfd0fe";
	else if (typeName == "on") obj.style.backgroundColor = "#fad2d2";
}

function copyDecorationMedal(decorationMedalId) {
	var iTags = document.getElementsByTagName("input");
	for (var i = 0; i < iTags.length; i++) {
		if (iTags[i].type == "checkbox" && iTags[i].value == decorationMedalId) {
			iTags[i].checked = "checked";			
			break;
		}
	}
}

function copyServicePeriod(servicePeriodId) {
	var currentVeteranId = document.getElementById("veteranId").value;
	
	dwr.engine.beginBatch();
	VeteranService.copyVeteranService(servicePeriodId, currentVeteranId);
	fillServiceTable();
	dwr.engine.endBatch({
		errorHandler:function(errorString, exception) { 
			alert("Error: " + errorString);
		}
	});
}

function copyServiceBenefit(benefitTypeId, origVeteranId) {
	var currentVeteranId = document.getElementById("veteranId").value;
	
	dwr.engine.beginBatch();
	VeteranBenefitType.copyBenefitTypeRecipients(benefitTypeId, currentVeteranId, origVeteranId);
	fillBenefitTypeTable();
	dwr.engine.endBatch({
		  errorHandler:function(errorString, exception) { 
			alert("Error: " + errorString);
		  }
	});
}

function copyNote(noteId) {
	var currentVeteranId = document.getElementById("veteranId").value;
	var currentSsnId = document.getElementById("ssnId").value;
	var url = '<s:url namespace="/" action="copyNoteVeteran" />';
	url += "?currentVeteranId=" + currentVeteranId + "&ssn=" + currentSsnId + "&noteId=" + noteId;
	
	YAHOO.vts.container.wait.show();
	window.location.href = url;			
}

function copyAll(origVeteranId) {
	if (confirm("Operation cannot be undone. Are you sure you want to copy this record to current record/proposed record?")) {
		var currentVeteranId = document.getElementById("veteranId").value;
		var url = '<s:url namespace="/" action="copyDataVeteran" />';
		url += "?currentVeteranId=" + currentVeteranId + "&origVeteranId=" + origVeteranId;
		
		YAHOO.vts.container.wait.show();
		window.location.href = url;		
	}
}

function onSave() {
	if (!validateProposedForm()) {
		return false;
	} else {
		YAHOO.vts.container.wait.show();
		return true;	
	}	
}

function validateProposedForm() {
	
	var errorMsg = "Validation error:\n";
	var flag = 0;
	
	if (!isDateFormatOk(document.getElementById("veteranForm_veteran_dateOfBirth").value)) {
		errorMsg += "Date of Birth is invalid. It must be complete date of format MM/DD/YYYY or leave it blank.\n";
		flag += 1;		
	}
	
	if (!isValidZip(document.getElementById("veteranForm_veteran_zip").value)) {
		errorMsg += "Street Address Zip is invalid.\n";
		flag += 1;				
	}
	
	if (!isValidZip(document.getElementById("veteranForm_veteran_mailingZip").value)) {
		errorMsg += "Mailing Address Zip is invalid.\n";
		flag += 1;				
	}
	
	if (!isValidPhone(document.getElementById("veteranForm_veteran_primaryPhone").value)) {
		errorMsg += "Primary Phone is invalid.\n";
		flag += 1;				
	}
	
	if (!isValidPhone(document.getElementById("veteranForm_veteran_altPhone").value)) {
		errorMsg += "Alternate Phone is invalid.\n";
		flag += 1;				
	}

	if (!isValidEmail(document.getElementById("veteranForm_veteran_email").value)) {
		errorMsg += "Email Address is invalid.\n";
		flag += 1;				
	}
	
	if (!isValidZip(document.getElementById("veteranForm_veteran_altContactZip").value)) {
		errorMsg += "Alternate Contact Zip is invalid.\n";
		flag += 1;				
	}

	if (!isValidPhone(document.getElementById("veteranForm_veteran_altContactPhone").value)) {
		errorMsg += "Alternate Contact Phone is invalid.\n";
		flag += 1;				
	}
	
	if (!isDateFormatOk(document.getElementById("veteranForm_veteran_dateOfDeath").value)) {
		errorMsg += "Deceased is invalid. It must be complete date of format MM/DD/YYYY or leave it blank.\n";
		flag += 1;		
	}

	if (flag > 0) {
		alert(errorMsg);
		return false;
	} else {
		return true;
	}
	
}

function onCancel() {
	if (document.getElementById("fromDashboard").value == "true") {
		window.location.href = '<s:url namespace="/" action="cancelVeteran" />';	
	} else {
		window.location.href = '<s:url namespace="/" action="displaySearchVeteranSearch" />';	
	}	
}

function highLightFields() {
	var spanTags =  document.getElementsByTagName("SPAN");
	
	for (i=0; i < spanTags.length; i++) {
		var id = spanTags[i].id;
		var typeName = id.substring(1, 3);
		var fieldName = id.substring(3);
		
		var objCur = document.getElementById("veteranForm_veteran_" + fieldName);
		if (objCur != null) {
			var objCurVal;
			if (fieldName == "ethnicity.id") {	// find value for ethnicity drop down box
				for (var j=0; j < objCur.options.length; j++) {
					if (objCur.options[j].selectedIndex != -1) {
						objCurVal = objCur.options[objCur.selectedIndex].text;
						objCurVal = objCurVal.toUpperCase();
						break;
					}
				}
			} else {
				objCurVal = objCur.value.toUpperCase();
			}
			if (spanTags[i].innerHTML.toUpperCase() != objCurVal) {
				if (typeName == "ct") document.getElementById(id).style.backgroundColor = "#f7f792";
				else if (typeName == "dd") document.getElementById(id).style.backgroundColor = "#8dbdfc";
				else if (typeName == "dl") document.getElementById(id).style.backgroundColor = "#7aebb1";
				else if (typeName == "dt") document.getElementById(id).style.backgroundColor = "#58b1cb";
				else if (typeName == "dw") document.getElementById(id).style.backgroundColor = "#fc7e02";
				else if (typeName == "ma") document.getElementById(id).style.backgroundColor = "#c6acfd";
				else if (typeName == "on") document.getElementById(id).style.backgroundColor = "#daabab";
			}			
		}
	} 
}

</script>

</head>
<body>
<br />
<h1><s:text name="veteranEditCompare" /></h1>

<div id="leftTab">

	<!-- current record tab -->
	<div class="clear"></div>
	<div id="lCurrentTab">
		<img src="./images/currentTab.gif" title="Current" />
	</div>
	<div id="rCurrentTab">
		<table width="100%" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td><strong><s:property value="veteran.ssn" /></strong></td>
				<td style="text-align: right;"><strong>
					<s:if test="veteran.verified == 1">Verified</s:if>
					<s:else>Non-verified</s:else>
				</strong></td>
			</tr>
			<s:if test="veteran.vaEnrolled || veteran.vaMedEnrolled">
				<tr>
					<td>
						<s:if test="veteran.vaEnrolled">
							Enrolled in VA
						</s:if>
						&nbsp;
					</td>
					<td style="text-align: right;">
						<s:if test="veteran.vaMedEnrolled">
							Enrolled in VA Medical
						</s:if>
						&nbsp;
					</td>
				</tr>
			</s:if>
			<s:if test="veteran.shareFederalVa">
				<tr>
					<td colspan="2">OK to share information with federal VA </td>
				</tr>
			</s:if>
			<s:if test="!(veteran.lastName == null && veteran.firstName == null && veteran.dataOfBirth == null)">
				<tr>
					<td>
						<s:property value="veteran.lastName"/>,
						<s:property value="veteran.firstName"/>
						<s:property value="veteran.middleName"/>
					</td>
					<td style="text-align: right;">
						<s:text name="veteran.dob"/>: <s:date name="veteran.dateOfBirth" format="MM/dd/yyyy" /> 
					</td>
				</tr>
			</s:if>
			<tr>
				<s:if test="veteran.gender != null">
					<td>
						<s:text name="veteran.gender"/>: <s:property value="veteran.gender"/>
					</td>
				</s:if>
				<s:else>
					<td>&nbsp;</td>
				</s:else>
				<td style="text-align: right;">
					<s:text name="veteran.ethnicity"/>: <s:property value="veteran.ethnicity.name"/>
				</td>
			</tr>
			<s:if test="veteran.address1 != null || veteran.address2 != null">
				<tr><td colspan="2">					
					<strong>Street Address:</strong> 
					<s:property value="veteran.address1"/>,
					<s:property value="veteran.address2"/>,
					<s:property value="veteran.city"/>,
					<s:property value="veteran.state"/>
					<s:property value="veteran.zip"/>				
				</td></tr>
			</s:if>
			<s:if test="!(veteran.mailingAddr1 == null || veteran.mailingAddr1 == '')">
				<tr><td colspan="2">					
					<strong>Mailing Address:</strong> 
					<s:property value="veteran.mailingAddr1"/>,
					<s:property value="veteran.mailingAddr2"/>,
					<s:property value="veteran.mailingCity"/>,
					<s:property value="veteran.mailingState"/>
					<s:property value="veteran.mailingZip"/>
				</td></tr>
			</s:if>
			<s:if test="!(veteran.primaryPhone == null && veteran.altPhone == null)">
				<tr>
					<s:if test="veteran.primaryPhone != null">
						<td>
							<s:text name="veteran.primaryPhone"/>: <s:property value="veteran.primaryPhone"/>
						</td>
					</s:if>
					<s:else>
						<td>&nbsp;</td>
					</s:else>
					<s:if test="veteran.altPhone != null">
						<td style="text-align: right;">
							<s:text name="veteran.altPhone"/>: <s:property value="veteran.altPhone"/> 
						</td>
					</s:if>
					<s:else>
						<td>&nbsp;</td>
					</s:else>
				</tr>
			</s:if>
			<s:if test="veteran.email != null">
				<tr><td colspan="2">					
					<s:text name="veteran.email"/>: <s:property value="veteran.email"/>
				</td></tr>
			</s:if>
			<s:if test="veteran.altContactLastName != null || veteran.altContactFirstName != null">
				<tr><td colspan="2">					
					<strong>Alternate Contact:</strong> <s:property value="veteran.altContactLastName"/>,
					<s:property value="veteran.altContactFirstName"/>
				</td></tr>
				<tr><td colspan="2">					
					Address: 
					<s:property value="veteran.altContactAddr1"/>,
					<s:property value="veteran.altContactAddr2"/>,
					<s:property value="veteran.altContactCity"/>,
					<s:property value="veteran.altContactState"/>
					<s:property value="veteran.altContactZip"/>				
				</td></tr>
				<tr>
					<s:if test="veteran.altContactPhone != null">
						<td>					
							<s:text name="person.phone"/>: <s:property value="veteran.altContactPhone"/>
						</td>
					</s:if>
					<s:else>
						<td>&nbsp;</td>
					</s:else>
					<s:if test="veteran.relation != null">
						<td style="text-align: right;">
							<s:text name="veteran.relationship"/>: <s:property value="veteran.relation.name"/>
						</td>
					</s:if>
					<s:else>
						<td>&nbsp;</td>
					</s:else>
				</tr>
			</s:if>
			<tr><td colspan="2"><strong>Military Information</strong></td></tr>
			<tr>
				<s:if test="veteran.dateOfDeath != null">
					<td>
						<s:text name="veteran.dod"/>: <s:date name="veteran.dateOfDeath" format="MM/dd/yyyy" /> 
					</td>
				</s:if>
				<s:else>
					<td>&nbsp;</td>
				</s:else>
				<td style="text-align: right;">
					<s:text name="veteran.disability"/>: <s:property value="veteran.percentDisability"/>
				</td>
			</tr>
			<s:if test="veteran.decorationMedalList.size() > 0">
				<tr><td colspan="2">Decoration and Medal:</td></tr>
				<tr><td colspan="2">
					<s:iterator value="veteran.decorationMedalList" status="rowCount5">
						[<s:property value="name" />]
					</s:iterator>			
				</td></tr>
			</s:if>
			<s:if test="veteran.servicePeriodList.size() > 0">
				<tr><td colspan="2">
					<table width="100%" style="border: 2px #CC9 solid;" cellspacing="0" cellpadding="2">
						<tr><td colspan="4"><strong>Service Periods:</strong></td></tr>
						<s:iterator value="veteran.servicePeriodList" status="rowCount2">
							<tr>
								<td><s:property value="serviceBranch.name" /></td>
								<td><s:property value="serviceEra.name" /></td>
								<td><s:date name="startDate" format="MM/dd/yyyy" /></td>
								<td><s:date name="endDate" format="MM/dd/yyyy" /></td>
							</tr>
						</s:iterator>
					</table>
				</td></tr>
			</s:if>
			<s:if test="veteran.benefitTypeList.size() > 0">
				<tr><td colspan="2">
					<table width="100%" style="border: 2px #CC9 solid;" cellspacing="0" cellpadding="2">
						<tr><td><strong>Service Benefits:</strong></td></tr>
						<s:iterator value="veteran.benefitTypeList" status="rowCount3">
							<tr>
								<td><s:property value="name" /></td>
							</tr>
							<s:if test="benefitRecipients.size() > 0">
								<tr><td>
									<table border="0">
									<s:iterator value="benefitRecipients">
										<tr><td>
											&nbsp; &nbsp; &nbsp; &nbsp;<s:property value="firstName" /> <s:property value="lastName" /> - 
											<s:property value="address1" />, <s:property value="city" />, <s:property value="state" /> <s:property value="zip" /> 
										</td></tr>
									</s:iterator>
									</table>
								</td></tr>							
							</s:if>
						</s:iterator>
					</table>
				</td></tr>
			</s:if>
			<s:if test="veteran.noteList.size() > 0">
				<tr><td colspan="2">
					<table width="100%" border="0" cellspacing="0" cellpadding="2">
						<tr><td><strong><s:text name="veteran.noteHistory" />:</strong></td></tr>
						<tr>
							<td>
								<display:table name="veteran.noteList" export="false" id="row" requestURI="" style="width:100%;">
									<display:column title="Note Text" property="noteText" />
								</display:table>
							</td>
						</tr>
					</table>
				</td></tr>
			</s:if>
			<tr>
				<td>
					Source: <s:property value="veteran.recordType.description" /> 
				</td>
				<td style="text-align: right;">
					Last Updated: <s:date name="veteran.updateTimestamp" format="MM/dd/yyyy" /> 
				</td>
			</tr>
		</table>
	</div>
	<div class="clear"></div>
	<table border="0" cellpadding=0 cellspacing=0><tr><td><img src="images/ecblank.gif" /></td></tr></table>
	
	<!-- end of current record tab -->
	
	<!-- the rest of records -->
	<s:iterator value="veterans" status="rowCount" var="vetRecord">
		<s:if test="recordType.name == 'Client Track'">
			<s:set name="divLId" value="%{'lCTTab'}" />	
			<s:set name="divRId" value="%{'rCTTab'}" />
			<s:set name="imageTab" value="%{'./images/ctTab.gif'}" />
			<s:set name="imageTitle" value="%{'Client Track'}" />
			<s:set name="typeName" value="%{'ct'}" />
		</s:if>
		<s:if test="recordType.name == 'DD214'">
			<s:set name="divLId" value="%{'lDDTab'}" />	
			<s:set name="divRId" value="%{'rDDTab'}" />
			<s:set name="imageTab" value="%{'./images/ddTab.gif'}" />
			<s:set name="imageTitle" value="%{'DD form 214'}" />
			<s:set name="typeName" value="%{'dd'}" />
		</s:if>
		<s:if test="recordType.name == 'DL'">
			<s:set name="divLId" value="%{'lDLTab'}" />	
			<s:set name="divRId" value="%{'rDLTab'}" />
			<s:set name="imageTab" value="%{'./images/dlTab.gif'}" />
			<s:set name="imageTitle" value="%{'Driver License Record'}" />
			<s:set name="typeName" value="%{'dl'}" />
		</s:if>
		<s:if test="recordType.name == 'DTek'">
			<s:set name="divLId" value="%{'lDTTab'}" />	
			<s:set name="divRId" value="%{'rDTTab'}" />
			<s:set name="imageTab" value="%{'./images/dtTab.gif'}" />
			<s:set name="imageTitle" value="%{'Doc Tek'}" />
			<s:set name="typeName" value="%{'dt'}" />
		</s:if>
		<s:if test="recordType.name == 'DWS'">
			<s:set name="divLId" value="%{'lDwsTab'}" />	
			<s:set name="divRId" value="%{'rDwsTab'}" />
			<s:set name="imageTab" value="%{'./images/dwsTab.gif'}" />
			<s:set name="imageTitle" value="%{'DWS Record'}" />
			<s:set name="typeName" value="%{'dw'}" />
		</s:if>
		<s:if test="recordType.name == 'Manual'">
			<s:set name="divLId" value="%{'lManualTab'}" />	
			<s:set name="divRId" value="%{'rManualTab'}" />
			<s:set name="imageTab" value="%{'./images/manualTab.gif'}" />
			<s:set name="imageTitle" value="%{'Manual Record'}" />
			<s:set name="typeName" value="%{'ma'}" />
		</s:if>
		<s:if test="recordType.name == 'Online'">
			<s:set name="divLId" value="%{'lOnlineTab'}" />	
			<s:set name="divRId" value="%{'rOnlineTab'}" />
			<s:set name="imageTab" value="%{'./images/onlineTab.gif'}" />
			<s:set name="imageTitle" value="%{'Online Record'}" />
			<s:set name="typeName" value="%{'on'}" />
		</s:if>

		<div class="clear"></div>
		<div id="<s:property value="#divLId" />">
			<img src="<s:property value="#imageTab" />" title="<s:property value="#imageTitle" />" />
		</div>
		<div id="<s:property value="#divRId" />">
			<table width="100%" border="0" cellspacing="0" cellpadding="2">
				<tr>
					<td>
						<strong><s:property value="ssn" /></strong>
					</td>
					<td style="text-align: right;">
						<strong>
						<s:if test="verified == 1">Verified</s:if>
						<s:else>Non-verified</s:else>
						</strong>&nbsp; &nbsp; &nbsp; &nbsp; 
						<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />servicePeriod" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="copyAll(<s:property value="#vetRecord.id" />);">Copy All</span>
					</td>
				</tr>
				<s:if test="vaEnrolled || vaMedEnrolled">
					<tr>
						<td>
							<s:if test="vaEnrolled">
								Enrolled in VA
							</s:if>
							&nbsp;
						</td>
						<td style="text-align: right;">
							<s:if test="vaMedEnrolled">
								Enrolled in VA Medical
							</s:if>
							&nbsp;
						</td>
					</tr>
				</s:if>
				<s:if test="shareFederalVa">
					<tr>
						<td colspan="2">OK to share information with federal VA </td>
					</tr>
				</s:if>
				<s:if test="!(lastName == null && firstName == null && dataOfBirth == null)">
					<tr>
						<td>
							<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />lastName" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="lastName"/></span>,
							<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />firstName" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="firstName"/></span>
							<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />middleName" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="middleName"/></span>
						</td>
						<td style="text-align: right;">
							<s:text name="veteran.dob"/>: <span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />dateOfBirth" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:date name="dateOfBirth" format="MM/dd/yyyy" /></span> 
						</td>
					</tr>
				</s:if>
				<tr>
					<s:if test="gender != null">
						<td>
							<s:text name="veteran.gender"/>: <span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />gender" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="gender"/></span>
						</td>
					</s:if>
					<s:else>
						<td>&nbsp;</td>
					</s:else>
					<td style="text-align: right;">
						<s:text name="veteran.ethnicity"/>: <span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />ethnicity.id" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="ethnicity.name"/></span> 
					</td>
				</tr>

				<s:if test="address1 != null || address2 != null">
					<tr><td colspan="2">					
						<strong>Street Address:</strong> 
						<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />address1" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="address1"/></span>,
						<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />address2" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="address2"/></span>,
						<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />city" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="city"/></span>,
						<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />state" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="state"/></span>
						<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />zip" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="zip"/></span>				
					</td></tr>
				</s:if>
				<s:if test="mailingAddr1 != null || mailingAddr2 != null">
					<tr><td colspan="2">					
						<strong>Mailing Address:</strong> 
						<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />mailingAddr1" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="mailingAddr1"/></span>,
						<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />mailingAddr2" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="mailingAddr2"/></span>,
						<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />mailingCity" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="mailingCity"/></span>,
						<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />mailingState" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="mailingState"/></span>
						<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />mailingZip" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="mailingZip"/></span>
					</td></tr>
				</s:if>
				<s:if test="!(primaryPhone == null && altPhone == null)">
					<tr>
						<s:if test="primaryPhone != null">
							<td>
								<s:text name="veteran.primaryPhone"/>: <span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />primaryPhone" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="primaryPhone"/></span>
							</td>
						</s:if>
						<s:else>
							<td>&nbsp;</td>
						</s:else>
						<s:if test="altPhone != null">
							<td style="text-align: right;">
								<s:text name="veteran.altPhone"/>: <span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />altPhone" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="altPhone"/></span> 
							</td>
						</s:if>
						<s:else>
							<td>&nbsp;</td>
						</s:else>
					</tr>
				</s:if>
				<s:if test="email != null">
					<tr><td colspan="2">					
						<s:text name="veteran.email"/>: <span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />email" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="email"/></span>
					</td></tr>
				</s:if>
				<s:if test="altContactLastName != null || altContactFirstName != null">
					<tr><td colspan="2">					
						<strong>Alternate Contact:</strong> <span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />altContactLastName" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="altContactLastName"/></span>,
						<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />altContactFirstName" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="altContactFirstName"/></span>
					</td></tr>
					<tr><td colspan="2">					
						Address: 
						<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />altContactAddr1" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="altContactAddr1"/></span>,
						<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />altContactAddr2" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="altContactAddr2"/></span>,
						<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />altContactCity" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="altContactCity"/></span>,
						<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />altContactState" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="altContactState"/></span>
						<span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />altContactZip" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="altContactZip"/></span>				
					</td></tr>
					<tr>
						<s:if test="altContactPhone != null">
							<td>					
								<s:text name="person.phone"/>: <span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />altContactPhone" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="altContactPhone"/></span>
							</td>
						</s:if>
						<s:else>
							<td>&nbsp;</td>
						</s:else>
						<s:if test="relation != null">
							<td style="text-align: right;">
								<s:text name="veteran.relationship"/>: <span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />relation.id" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="relation.name"/></span>
							</td>
						</s:if>
						<s:else>
							<td>&nbsp;</td>
						</s:else>
					</tr>
				</s:if>
				<tr><td colspan="2"><strong>Military Information</strong></td></tr>
				<tr>
					<s:if test="dateOfDeath != null">
						<td>
							<s:text name="veteran.dod"/>: <span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />dateOfDeath" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:date name="dateOfDeath" format="MM/dd/yyyy" /></span> 
						</td>
					</s:if>
					<s:else>
						<td>&nbsp;</td>
					</s:else>
					<td style="text-align: right;">
						<s:text name="veteran.disability"/>: <span id="<s:property value="#rowCount.index" /><s:property value="#typeName" />percentDisability" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />);" onclick="getEleValue(this.id, <s:property value="#rowCount.index" />);"><s:property value="percentDisability"/></span>
					</td>
				</tr>
				<s:if test="decorationMedalList.size() > 0">
					<tr><td colspan="2">Decoration and Medal:</td></tr>
					<tr><td colspan="2">
						<s:iterator value="decorationMedalList" status="rowCount5">
							<span id="<s:property value="#rowCount.index" /><s:property value="#rowCount5.index" /><s:property value="#typeName" />decorationMedal" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />, <s:property value="#rowCount5.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />, <s:property value="#rowCount5.index" />);" onclick="copyDecorationMedal(<s:property value="id" />);">[<s:property value="name" />]</span>
						</s:iterator>			
					</td></tr>
				</s:if>
				<s:if test="servicePeriodList.size() > 0">
					<tr><td colspan="2">
						<table width="100%" style="border: 2px #CC9 solid;" cellspacing="0" cellpadding="2">
							<tr><td colspan="5"><strong>Service Periods:</strong></td></tr>
							<s:iterator value="servicePeriodList" status="rowCount2">
								<tr>
									<td><s:property value="serviceBranch.name" /></td>
									<td><s:property value="serviceEra.name" /></td>
									<td><s:date name="startDate" format="MM/dd/yyyy" /></td>
									<td><s:date name="endDate" format="MM/dd/yyyy" /></td>
									<td>
										<span id="<s:property value="#rowCount.index" /><s:property value="#rowCount2.index" /><s:property value="#typeName" />servicePeriod" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />, <s:property value="#rowCount2.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />, <s:property value="#rowCount2.index" />);" onclick="copyServicePeriod(<s:property value="id" />);">Copy</span>
									</td>
								</tr>
							</s:iterator>
						</table>
					</td></tr>
				</s:if>
				<s:if test="benefitTypeList.size() > 0">
					<tr><td colspan="2">
						<table width="100%" style="border: 2px #CC9 solid;" cellspacing="0" cellpadding="2">
							<tr><td colspan="2"><strong>Service Benefits:</strong></td></tr>
							<s:iterator value="benefitTypeList" status="rowCount3">
								<tr>
									<td><s:property value="name" /></td>
									<td>
										<span id="<s:property value="#rowCount.index" /><s:property value="#rowCount3.index" /><s:property value="#typeName" />serviceBenefit" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />, <s:property value="#rowCount3.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />, <s:property value="#rowCount3.index" />);" onclick="copyServiceBenefit(<s:property value="id" />,<s:property value="#vetRecord.id" />);">Copy</span>
									</td>
								</tr>
								<s:if test="benefitRecipients.size() > 0">
									<tr><td colspan="2">
										<table border="0">
										<s:iterator value="benefitRecipients">
											<tr><td>
												&nbsp; &nbsp; &nbsp; &nbsp;<s:property value="firstName" /> <s:property value="lastName" /> - 
												<s:property value="address1" />, <s:property value="city" />, <s:property value="state" /> <s:property value="zip" /> 
											</td></tr>
										</s:iterator>
										</table>
									</td></tr>							
								</s:if>
							</s:iterator>
						</table>
					</td></tr>
				</s:if>
				<s:if test="noteList.size() > 0">
					<tr><td colspan="2">
						<table width="100%" border="0" cellspacing="0" cellpadding="2">
							<tr><td colspan="2"><strong>Note History:</strong></td></tr>
							<s:iterator value="noteList" status="rowCount4">
								<tr>
									<td><s:property value="noteText" escape="false" /></td>
									<td>
										<span id="<s:property value="#rowCount.index" /><s:property value="#rowCount4.index" /><s:property value="#typeName" />noteText" onmouseover="highLightOn(this.id, <s:property value="#rowCount.index" />, <s:property value="#rowCount4.index" />);" onmouseout="highLightOff(this.id, <s:property value="#rowCount.index" />, <s:property value="#rowCount4.index" />);" onclick="copyNote(<s:property value="id" />);">Copy</span>
									</td>
								</tr>
							</s:iterator>
						</table>
					</td></tr>
				</s:if>
				<tr>
					<td>
						<s:if test="reviewed == 1">Reviewed</s:if>
						<s:else>Un-reviewed</s:else>
					</td>
					<td style="text-align: right;">
						Source Date: <s:date name="sourceDate" format="MM/dd/yyyy" />
					</td>
				</tr>				
			</table>
		</div>
		<div class="clear"></div>
		<table border="0" cellpadding=0 cellspacing=0><tr><td><img src="images/ecblank.gif" /></td></tr></table>

	</s:iterator>
</div>

<div id="rightTab">
	<div id="lProposedTab">
		<img src="./images/proposedTab.gif" alt="" />
	</div>
	<div id="rProposedTab">
		<s:form action="saveEditCompareVeterans" namespace="/" id="veteranForm">
			<s:actionerror/>
			<s:actionmessage/>
			<table width="430" border="0" cellspacing="0" cellpadding="2">
				<tr>
					<td><strong><s:property value="veteran.ssn" /></strong></td>
					<td id="radioButtonList" align="right">
						<s:radio name="veteran.verified" list="#{1:'Verified',0:'Non-verified'}" />
					</td>
				</tr>
				<tr>
					<td>
						<s:checkbox name="veteran.vaEnrolled" id="vaEnrolled" cssClass="chkbox" /><s:text name="veteran.vaEnrolled"/>
					</td>
					<td>
						<s:checkbox name="veteran.vaMedEnrolled" id="vaMedEnrolled" cssClass="chkbox" /><s:text name="veteran.vaMedEnrolled"/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<s:checkbox name="veteran.shareFederalVa" cssClass="chkbox"/><s:text name="veteran.shareFederalVa" />
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<s:text name="person.firstName"/>: <s:textfield name="veteran.firstName" cssClass="fieldset125"/>
						<s:text name="person.middleName"/>: <s:textfield name="veteran.middleName" cssClass="fieldset95"/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<s:text name="person.lastName"/>: <s:textfield name="veteran.lastName" cssClass="fieldset125"/>
						<s:text name="person.suffix"/>: <s:textfield name="veteran.suffix" id="suffix" cssClass="fieldset95" maxlength="6"/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<s:date name="veteran.dateOfBirth" format="MM/dd/yyyy" var="dob"/>
						<s:text name="veteran.dob"/>: <s:textfield name="veteran.dateOfBirth" value="%{#dob}"  cssClass="fieldset75" maxlength="10" />
					</td>
				</tr>
				<tr>
					<td>
						<s:text name="veteran.gender"/>: <s:select name="veteran.gender" list="#{'Male':'Male','Female':'Female'}" headerKey="" headerValue="" />
					</td>
					<td>
						<s:text name="veteran.ethnicity"/>: <s:select name="veteran.ethnicity.id" id="veteranForm_veteran_ethnicity.id" list="ethnicityList" listKey="id" listValue="value" headerKey="" headerValue="" />
					</td>
				</tr>

				<tr><td colspan="2"><strong>Street Address:</strong></td></tr>
				<tr>
					<td colspan="2">
						<s:text name="veteran.address1"/>: <s:textfield name="veteran.address1" cssClass="fieldset350"/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<s:text name="veteran.address2"/>: <s:textfield name="veteran.address2" cssClass="fieldset350"/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<s:text name="veteran.city"/>: <s:textfield name="veteran.city" cssClass="fieldset100"/>
						<s:text name="veteran.state"/>: <s:select name="veteran.state" list="stateMap" headerKey="" headerValue="" />
						<s:text name="veteran.zip"/>: <s:textfield name="veteran.zip" cssClass="fieldset75" />
					</td>
				</tr>
				<tr><td colspan="2"><strong>Mailing Address:</strong></td></tr>
				<tr>
					<td colspan="2">
						<s:text name="veteran.address1"/>: <s:textfield name="veteran.mailingAddr1" cssClass="fieldset350"/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<s:text name="veteran.address2"/>: <s:textfield name="veteran.mailingAddr2" cssClass="fieldset350"/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<s:text name="veteran.city"/>: <s:textfield name="veteran.mailingCity" cssClass="fieldset100"/>
						<s:text name="veteran.state"/>: <s:select name="veteran.mailingState" list="stateMap" headerKey="" headerValue="" />
						<s:text name="veteran.zip"/>: <s:textfield name="veteran.mailingZip" cssClass="fieldset75" />
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<s:text name="veteran.primaryPhone"/>: <s:textfield name="veteran.primaryPhone" cssClass="fieldset95" maxlength="12" />
						<s:text name="veteran.altPhone"/>: <s:textfield name="veteran.altPhone" cssClass="fieldset95" maxlength="12" />
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<s:text name="veteran.email"/>: <s:textfield name="veteran.email" cssClass="fieldset275"/>
					</td>
				</tr>				
				<tr>
					<td colspan="2">
						Preferable Email Option: <s:radio name="veteran.emailOption" list="#{1:'Regular Mail', 2:'Email', 3:'No Preference'}" />
					</td>
				</tr>
				<tr><td colspan="2"><strong>Alternate Contact</strong></td></tr>
				<tr>
					<td colspan="2">
						<s:text name="person.lastName"/>: <s:textfield name="veteran.altContactLastName" cssClass="fieldset125"/>
						<s:text name="person.firstName"/>: <s:textfield name="veteran.altContactFirstName" cssClass="fieldset125"/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<s:text name="veteran.address1"/>: <s:textfield name="veteran.altContactAddr1" cssClass="fieldset350"/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<s:text name="veteran.address2"/>: <s:textfield name="veteran.altContactAddr2" cssClass="fieldset350"/>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<s:text name="veteran.city"/>: <s:textfield name="veteran.altContactCity" cssClass="fieldset100"/>
						<s:text name="veteran.state"/>: <s:select name="veteran.altContactState" list="stateMap" headerKey="" headerValue="" />
						<s:text name="veteran.zip"/>: <s:textfield name="veteran.altContactZip" cssClass="fieldset75" />
					</td>
				</tr>
				<tr>
					<td colspan="2"><s:text name="person.phone"/>: 
						<s:textfield name="veteran.altContactPhone" cssClass="fieldset95" maxlength="12" />
					
						<s:text name="veteran.relationship"/>: 
						<s:select name="veteran.relation.id" id="veteranForm_veteran_relation.id" list="relationList" listKey="id" listValue="value" headerKey="" headerValue="" />
					</td>
				</tr>
				<tr><td colspan="2"><strong>Military Information</strong></td></tr>
				<tr>
					<td>
						<s:date name="veteran.dateOfDeath" format="MM/dd/yyyy" var="dod"/>
						<s:text name="veteran.dod"/>: <s:textfield name="veteran.dateOfDeath" value="%{#dod}"  cssClass="fieldset75" maxlength="10" />					
					</td>
					<td align="right">
						<s:text name="veteran.disability"/>: <s:select name="veteran.percentDisability" list="#{0:'0%',10:'10%',20:'20%',30:'30%',40:'40%',50:'50%',60:'60%',70:'70%',80:'80%',90:'90%',100:'100%'}" />
					</td>
				</tr>
				<tr><td colspan="2">
					<table class="multiBox2">
						<tr>							
							<td><s:text name="decorationMedal"/>:</td>
							<td><s:checkboxlist name="veteran.decorationMedals" id="decorationMedals" list="decorationMedalList" listKey="id" listValue="value" theme="custom" /></td>
						</tr>				
					</table>			
				</td></tr>
				<tr>
					<td colspan="2">
						<table style="border: 2px #CC9 solid;" width="428" border="0" cellspacing="0" cellpadding="2">
							<tr>
								<td colspan="2"><Strong>Service Periods</Strong></td>
							</tr>
							<tr><td colspan="2" style="text-align: right;">
								<input type="button" name="newServicePeriod" value="<s:text name="button.newServicePeriod"/>" class="button" onclick="onServicePeriodView();" >
							</td></tr>
							<tr><td colspan="2">
								<table width="100%">
									<thead>
										<tr>
											<th>Branch</th>
											<th>Era</th>
											<th>Begin</th>
											<th>End</th>
											<th>Action</th>
										</tr>
									</thead>
									<tbody id="servicePeriodBody">
										<tr id="servicePeriodPattern" style="display:none;">
											<td><span id="serviceBranch">&nbsp;</span></td>
											<td><span id="serviceEra">&nbsp;</span></td>
											<td><span id="serviceBegin">&nbsp;</span></td>
											<td><span id="serviceEnd">&nbsp;</span></td>
											<td style="text-align: center;">
												<a href="#" id="edit" onclick="javascript:editService(this.id);" title="Edit"><img src="<s:url value='/images/edit-icon.png'/>"/></a>
												&nbsp; <a href="#" id="delete" onclick="javascript:deleteService(this.id);" title="Delete"><img src="<s:url value='/images/delete-icon.png'/>"/></a>
											</td>
										</tr>
									</tbody>
								</table>
							</td></tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<table style="border: 2px #CC9 solid;" width="428" border="0" cellspacing="0" cellpadding="2">
							<tr>
								<td colspan="2"><Strong>Service Benefits</Strong></td>
							</tr>
							<tr><td colspan="2" style="text-align: right;">
								<input type="button" name="newBenefitType" value="<s:text name="button.newBenefitType"/>" class="button" onclick="onBenefitTypeView();" >
							</td></tr>
							<tr><td colspan="2">
								<table width="100%">
									<thead>
										<tr>
											<th>Benefit Type</th>
											<th>Action</th>
										</tr>
									</thead>
									<tbody id="benefitTypeBody">
										<tr id="benefitTypePattern" style="display:none;">
											<td><span id="benefitTypeName">&nbsp;</span></td>
											<td style="text-align: center;">
												<a href="#" id="edit" onclick="javascript:editBenefitType(this.id);" title="Edit/Beneficiary"><img src="<s:url value='/images/edit-icon.png'/>"/></a>
												&nbsp; <a href="#" id="delete" onclick="javascript:deleteBenefitType(this.id);" title="Delete"><img src="<s:url value='/images/delete-icon.png'/>"/></a>
											</td>
										</tr>
									</tbody>
								</table>
							</td></tr>
						</table>
					</td>
				</tr>
				<tr><td colspan="2">
					<table  width="428" border="0" cellspacing="0" cellpadding="2">
						<tr><td colspan="2"><strong><s:text name="veteran.noteHistory" />:</strong></td></tr>
						<tr>
							<td colspan="2">
								<display:table name="veteran.noteList" export="false" id="row" requestURI="" style="width:100%;">
									<display:column title="Note Text" property="noteText" />
								</display:table>
							</td>
						</tr>
						<tr>
							<td width="60"><s:text name="veteran.note" />:</td>
							<td width="368"><s:textarea name="veteran.noteText" rows="4" cols="40"/></td>
						</tr>					
					</table>
				</td></tr>
				
			</table>
			<br />
			<div style="text-align: right;">
				<s:hidden name="veteran.ssn" id="ssnId" />
				<s:hidden name="veteran.recordType.id" />
				<s:hidden name="veteran.shareFederalVa" />
				<s:hidden name="veteran.fromDashboard" id="fromDashboard" />
				
				<s:hidden name="veteran.id" id="veteranId" />
				<s:hidden name="veteran.contactable" />
				<s:hidden name="veteran.sourceDate" />
				<s:hidden name="veteran.vaCurrent" />
				<s:hidden name="veteran.reviewed" />
				<s:hidden name="veteran.active" />
				<s:hidden name="veteran.insertTimestamp" />
				<s:hidden name="veteran.createdBy.id" />
				<input type="button" name="cancel" value="<s:text name="button.cancel"/>" class="button" onclick="return onCancel();" >
				<s:reset name="reset" value="%{getText('button.reset')}" cssClass="button"/>
				<s:submit name="submit" value="%{getText('button.submit')}" cssClass="primaryButton" onclick="return onSave();" /> &nbsp;
			</div>
		</s:form>
		
		<script language="javascript" type="text/javascript">
		   initDwr();
		   highLightFields();
		</script>
		
	</div>
</div>

<div class="clear"></div>
<br />

<!-- yui panels -->
<s:include value="panelsInclude.jsp"></s:include>

</body>
</html>