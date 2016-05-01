<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VIS <s:text name="veteran" /></title>	

<link rel="stylesheet" type="text/css" href="<s:url value='/css/displaytagLib.css'/>" />

<script type='text/javascript' src="<s:url value='/scripts/validate.js'/>"></script>

<script type='text/javascript' src="<s:url value='/dwr/engine.js'/>"></script>
<script type='text/javascript' src="<s:url value='/dwr/util.js'/>"></script>
<script type='text/javascript' src="<s:url value='/scripts/dtsUtil.js'/>"></script>
<script type='text/javascript' src="<s:url value='/dwr/interface/VeteranService.js'/>"></script>
<script type='text/javascript' src="<s:url value='/dwr/interface/VeteranBenefitType.js'/>"></script>
<script type='text/javascript' src="<s:url value='/dwr/interface/VeteranBenefitRecipient.js'/>"></script>

<script type="text/javascript">

/** To show wait message box. To call it just put YAHOO.vts.container.wait.show() line in js function */
YAHOO.namespace("vts.container");

function init(){

	// Initialize the temporary Panel to display while waiting for external content to load.
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
        close: false, 
        draggable: true, 
        zindex:4,
        modal: true,
        visible: false
	});
	YAHOO.vts.container.panel3.render();
	
	// Instantiate panel4 for duplicate ssn section
	YAHOO.vts.container.panel4 = new YAHOO.widget.Panel("panel4", {
		width: "430px", 
        fixedcenter: true, 
        close: true, 
        draggable: true, 
        zindex:4,
        modal: true,
        visible: false
	});
	YAHOO.vts.container.panel4.render();
	
	// Instantiate panel5 for upload DD214 attachments
	YAHOO.vts.container.panel5 = new YAHOO.widget.Panel("panel5", {
		width: "460px", 
        fixedcenter: true, 
        close: true, 
        draggable: true, 
        zindex:4,
        modal: true,
        visible: false
	});
	YAHOO.vts.container.panel5.render();
	
	// Instantiate panel6 for displaying DD214 attachments list
	YAHOO.vts.container.panel6 = new YAHOO.widget.Panel("panel6", {
		width: "630px", 
        fixedcenter: true, 
        close: true, 
        draggable: true, 
        zindex:4,
        modal: true,
        visible: false
	});
	YAHOO.vts.container.panel6.render();

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
var duplicateSsn = "No";

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
	var veteranServicePeriod = { idService:viewed, serviceBranchId:null, serviceEraId:null, monthFrom:null, dayFrom:null, yearFrom:null, monthTo:null, dayTo:null, yearTo:null, combatZoneId:null, dischargeTypeId:null, activeService:null, insertTimestampService:null };
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
	dwr.util.setValues({ idService:null, serviceBranchId:null, serviceEraId:null, monthFrom:null, dayFrom:null, yearFrom:null, monthTo:null, dayTo:null, yearTo:null, combatZoneId:null, dischargeTypeId:null, vaEnrolled:null, activeService:null, insertTimestampService:null });
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
	    	dwr.util.setValue("benefitRecipientAddress" + id, veteranBenefitRecipient.city + ", " + veteranBenefitRecipient.state + " " + veteranBenefitRecipient.zip);
	    	
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
	} 
	
	var phoneNumberPattern = /^\(?(\d{3})\)?[- ]?(\d{3})[- ]?(\d{4})$/;
	var phone = document.getElementById("phone1").value + document.getElementById("phone2").value + document.getElementById("phone3").value;
	if (phone != "") {
		if (!phoneNumberPattern.test(phone)) {
			alert("Invalid phone number.");
			return false
		}
	}
		
	return true;
}

function onCancel() {
	window.location.href = '<s:url namespace="/" action="displaySearchVeteranSearch" />';
}

function onSave() {
	checkDuplicateSsn();
	if (duplicateSsn == "Yes") { 
		return false;
	} else {
		YAHOO.vts.container.wait.show();
		
		document.getElementById("veteranForm").action = "saveVeteran.action";
		return true;			
	}
}

function backToSearch() {
	window.location.href = '<s:url namespace="/" action="displaySearchVeteranSearch" />';
}

function toggleMailingAddr(obj) {
	if (obj.value == "y") {
		document.getElementById("mailingAddrId").style.display = "block";
	} else {
		document.getElementById("mailingAddrId").style.display = "none";
		document.getElementById("mailingAddr1").value = "";
		document.getElementById("mailingAddr2").value = "";
		document.getElementById("mailingCity").value = "";
		document.getElementById("mailingZip").value = "";
	}
}

function checkDuplicateSsn() {
	var temp = "<s:property value="veteran.vetPageTitle" />";
	if (temp != "New Entry") {
		return true;
	}
	
	var ssn = document.getElementById("ssn1").value + "-" + document.getElementById("ssn2").value + "-" + document.getElementById("ssn3").value;
	
	VeteranService.checkDuplicateSsn(ssn, function(retStr) {
		if (retStr == "Yes") {
			duplicateSsn = "Yes";
			YAHOO.vts.container.panel4.show();
		} else {
			duplicateSsn = "No";
		}
	});
}

function goToEditCompare() {
	YAHOO.vts.container.wait.show();
	
	var ssn = document.getElementById("ssn1").value + "-" + document.getElementById("ssn2").value + "-" + document.getElementById("ssn3").value;
	var url = '<s:url namespace="/" action="editCompareSsnVeterans" />';
	var values = {'ssn' : ssn};

	postToUrl(url, values);
}

function setVerified(obj) {
	if (obj.value == 2) {	// DD214
		document.getElementById("veteranForm_veteran_verified1").checked = true;
	} else {
		document.getElementById("veteranForm_veteran_verified0").checked = true;
	}
}

function onSaveAttachment() {
	var radioObj = document.forms['veteranAttachmentForm'].elements['veteranAttachment.docType.id'];
	var radioValue;
	for (var i = 0; i < radioObj.length; i++) {
		if (radioObj[i].checked) {
			radioValue = radioObj[i].value;
			break;
		}
	}
	
	if (isDd214Matched(radioValue)) {
		if (document.getElementById("attachmentId").value == "" || document.getElementById("attachmentTxtId").value == "" ) {
			alert("Image and Text attachments are required.");
			return false;
		}
	} else {
		if (document.getElementById("attachmentId").value == "") {
			alert("Image attachment is required.");
			return false;
		}
	}
	
	// everything good, submit the form.
	YAHOO.vts.container.wait.show();
	document.getElementById("veteranAttachmentId").value = document.getElementById("veteranId").value;
	document.getElementById("veteranAttachmentPageTitleId").value = document.getElementById("vetPageTitleId").value;
	
	return true;	
	

}

function onAddDoc() {
	YAHOO.vts.container.panel5.show();
}

function onShowDoc() {
	YAHOO.vts.container.panel6.show();
}

function showAttachment(id) {
	var link = '<s:url namespace="/" action="showAttachment" />';
	link = link + "?id=" + id;
	//window.location.href = link;
	popupWindow(link, 'attachment', 610, 750);
}

function deleteAttachment(id) {
	if (confirm("Are you sure you want to delete the attachment?")) {
		var link = '<s:url namespace="/" action="deleteAttachment" />';
		link = link + "?id=" + id + "&veteranId=" + document.getElementById("veteranId").value;
		window.location.href = link;		
	}
}

function toggleAttachmentTxt(obj) {
	if (isDd214Matched(obj.value)) {
		document.getElementById("attachmentTxtRowId").style.display = "block";
	} else {
		document.getElementById("attachmentTxtRowId").style.display = "none";
	}
}

function isDd214Matched(val) {
	var dd214Values = document.getElementById("dd214Id").value;
	var dd214s = dd214Values.split(',');
	var match = false;
	for (var i = 0; i < dd214s.length; i++) {
		if (val == dd214s[i]) {
			match = true;
			break;
		}
	}

	return match;
}

</script>

</head>
<body>

<s:form action="saveVeteran" id="veteranForm">
	<div id="rotatingPhoto">
		<p><a name="contentSkip" id="contentSkip"></a>
	    	<script language="javascript">rotatingPhoto(photo_array);</script>
	   	</p>
		
		<table style="border: 2px #CC9 solid;" width="405" border="0" cellspacing="0" cellpadding="2">
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
	   	<br />
	   	<table  width="405" border="0" cellspacing="0" cellpadding="2">
	   		<tr><td colspan="2"><strong><s:text name="veteran.noteHistory" />:</strong></td></tr>
			<tr>
				<td colspan="2">
					<display:table name="veteran.noteList" export="false" id="row" requestURI="" style="width:100%;">
						<display:column title="Note Text" property="noteText" />
					</display:table>
				</td>
			</tr>
			<tr>
				<td width="100"><s:text name="veteran.note" />: </td>
				<td width="270">
					<s:textarea name="veteran.noteText" rows="4" cols="40"/>
				</td>
			</tr>					  	
	   	</table>
	   	  	
	   	<div class="clear"></div>
	</div>
	
	<div id="mainContent">
		<h1><s:property value="veteran.vetPageTitle" /></h1>
		<p>Asterisk <font color=red>*</font> indicates a required field.</p>
		<s:actionerror/>
		<s:actionmessage/>
		<table width="450" border="0" cellspacing="0" cellpadding="2">
			<s:if test="veteran.vetPageTitle == 'Edit Veteran'">
				<tr>
					<td>Last Updated:
						<font color='gray'><s:date name="veteran.updateTimestamp" format="MM/dd/yyyy"/>
					</font></td>
					<td style="text-align: right;">
						Updated By: <font color='gray'><s:property value="veteran.updatedBy.firstName" /> <s:property value="veteran.updatedBy.lastName" />
					</font></td>
				</tr>
			</s:if>
		
			<tr>
				<td id="radioButtonList">
					<s:radio name="veteran.verified" list="#{1:'Verified',0:'Non-verified'}" /> 
				</td>
				<td style="text-align: right;">
					<s:text name="recordType" />: 
					<s:if test="veteran.vetPageTitle == 'Edit Veteran'">
						<s:select name="veteran.recordType.id" list="recordTypeList" listKey="id" listValue="value" onchange="setVerified(this);" />
					</s:if>
					<s:else>
						<select id="veteranForm_veteran_recordType_id" name="veteran.recordType.id" onchange="setVerified(this);">
							<option value="2">DD214</option>
							<option selected="selected" value="6">Manual</option>
						</select>
					</s:else>
				</td>
			</tr>
			<tr>
				<td>
					<s:checkbox name="veteran.vaEnrolled" id="vaEnrolled" cssClass="chkbox" /><s:text name="veteran.vaEnrolled"/>
				</td>
				<td style="text-align: right;">
					<s:checkbox name="veteran.vaMedEnrolled" id="vaMedEnrolled" cssClass="chkbox" /><s:text name="veteran.vaMedEnrolled"/>
				</td>
			</tr>
			<tr>
				<td>
					<s:checkbox name="veteran.shareFederalVa" cssClass="chkbox"/><s:text name="veteran.shareFederalVa" />
				</td>
				<td style="text-align: right;"><font color='gray'>
					<s:if test="veteran.vaCurrent == 1">Current Veteran</s:if>
					<s:elseif test="veteran.reviewed == 1">Reviewed</s:elseif>
					<s:else>Un-reviewed</s:else>
				</font></td>
			</tr>
			<tr>
				<td colspan="2"><font color=red>*</font><s:text name="veteran.ssn"/>: 
					<s:textfield name="veteran.ssn1" onkeyup="return autoTab(this, 3, event);" id="ssn1" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="veteran.ssn2" onkeyup="return autoTab(this, 2, event);" id="ssn2" cssClass="fieldset15" maxlength="2"/>-<s:textfield name="veteran.ssn3" id="ssn3" cssClass="fieldset50" maxlength="4" onblur="checkDuplicateSsn();" />
				</td>
			</tr>
			<tr>
				<td colspan="2"><s:text name="veteran.dob"/>: 
					<s:select name="veteran.month" list="monthMap" headerKey="" headerValue="MM" />/
					<s:select name="veteran.day" list="dayMap" headerKey="" headerValue="DD" />/
					<s:select name="veteran.year" list="yearMap" headerKey="" headerValue="YYYY" /> 
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<s:text name="person.firstName"/>: <s:textfield name="veteran.firstName" cssClass="fieldset135"/>
					<s:text name="person.middleName"/>: <s:textfield name="veteran.middleName" cssClass="fieldset125"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<s:text name="person.lastName"/>: <s:textfield name="veteran.lastName" cssClass="fieldset135"/>
					<s:text name="person.suffix"/>: <s:textfield name="veteran.suffix" id="suffix" cssClass="fieldset100" maxlength="6"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<s:text name="veteran.gender"/>: <s:select name="veteran.gender" list="#{'Male':'Male','Female':'Female'}" headerKey="" headerValue="" />
					&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<s:text name="veteran.ethnicity"/>: <s:select name="veteran.ethnicity.id" list="ethnicityList" listKey="id" listValue="value" headerKey="" headerValue="" />
				</td>
			</tr>
			<tr><td colspan="2"><strong>Street Address</strong></td></tr>
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
					<s:text name="veteran.city"/>: <s:textfield name="veteran.city" cssClass="fieldset200"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<s:text name="veteran.state"/>: <s:select name="veteran.state" list="stateMap" headerKey="" headerValue="" />
				</td>
			</tr>
			<tr><td colspan="2">
				<s:text name="veteran.zip"/>: <s:textfield name="veteran.zip" cssClass="fieldset75" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<s:text name="veteran.rural"/>: 
				<font color="gray">
					<s:if test="veteran.rural == \"U\"">Urban</s:if>
					<s:if test="veteran.rural == \"R\"">Rural</s:if>
					<s:if test="veteran.rural == \"H\"">Highly Rural</s:if>
				</font>		
			</td></tr>
			<tr><td colspan="2">
				<div id="radioButtonList">
					<strong>Mailing Address Different?</strong>
					<s:radio name="veteran.mailingAddr" list="#{'n':'No','y':'Yes'}" onclick="toggleMailingAddr(this)" />
				</div>
			</td></tr>
			<tr>
				<td colspan="2">
					<s:if test="veteran.mailingCity.length() > 1">
						<s:set var="mailingAddrDisp" value="%{'display:block'}" />
					</s:if>
					<s:else>
						<s:set var="mailingAddrDisp" value="%{'display:none'}" />
					</s:else>
					<table width="450" id="mailingAddrId" bgcolor="#CDCCE6" cellspacing="2" cellpadding="2" style='<s:property value="#mailingAddrDisp" />'>
						<tr>
							<td>
								<s:text name="veteran.address1"/>: <s:textfield name="veteran.mailingAddr1" id="mailingAddr1" cssClass="fieldset350"/>
							</td>
						</tr>
						<tr>
							<td>
								<s:text name="veteran.address2"/>: <s:textfield name="veteran.mailingAddr2" id="mailingAddr2" cssClass="fieldset350"/>
							</td>
						</tr>
						<tr>
							<td>
								<s:text name="veteran.city"/>: <s:textfield name="veteran.mailingCity" id="mailingCity" cssClass="fieldset125"/>
								<s:text name="veteran.state"/>: <s:select name="veteran.mailingState" list="stateMap" headerKey="" headerValue="" />
								<s:text name="veteran.zip"/>: <s:textfield name="veteran.mailingZip" id="mailingZip" cssClass="fieldset75" />
							</td>
						</tr>									
				</table>
				</td>
			</tr>
			<tr>
				<td colspan="2"><s:text name="veteran.primaryPhone"/>: &nbsp;
					<s:textfield name="veteran.primaryPhone1" onkeyup="return autoTab(this, 3, event);" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="veteran.primaryPhone2" onkeyup="return autoTab(this, 3, event);" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="veteran.primaryPhone3" cssClass="fieldset50" maxlength="4"/>
				</td>
			</tr>
			<tr>
				<td colspan="2"><s:text name="veteran.altPhone"/>: 
					<s:textfield name="veteran.altPhone1" onkeyup="return autoTab(this, 3, event);" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="veteran.altPhone2" onkeyup="return autoTab(this, 3, event);" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="veteran.altPhone3" cssClass="fieldset50" maxlength="4"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<s:text name="veteran.email"/>: <s:textfield name="veteran.email" cssClass="fieldset340"/>
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
					<s:text name="person.firstName"/>: <s:textfield name="veteran.altContactFirstName" cssClass="fieldset135"/>
					<s:text name="person.lastName"/>: <s:textfield name="veteran.altContactLastName" cssClass="fieldset135"/>
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
					<s:textfield name="veteran.altContactPhone1" onkeyup="return autoTab(this, 3, event);" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="veteran.altContactPhone2" onkeyup="return autoTab(this, 3, event);" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="veteran.altContactPhone3" cssClass="fieldset50" maxlength="4"/>
				</td>
			</tr>
			<tr>
				<td colspan="2"><s:text name="veteran.relationship"/>: 
					<s:select name="veteran.relation.id" list="relationList" listKey="id" listValue="value" headerKey="" headerValue="" />
				</td>
			</tr>
			<tr><td colspan="2"><strong>Military Information</strong></td></tr>
			<tr>
				<td colspan="2"><s:text name="veteran.dod"/>: 
					<s:select name="veteran.monthD" list="monthMap" headerKey="" headerValue="MM" />/
					<s:select name="veteran.dayD" list="dayMap" headerKey="" headerValue="DD" />/
					<s:select name="veteran.yearD" list="yearMap" headerKey="" headerValue="YYYY" /> 
					<s:text name="veteran.disability"/>: <s:select name="veteran.percentDisability" list="#{0:'0%',10:'10%',20:'20%',30:'30%',40:'40%',50:'50%',60:'60%',70:'70%',80:'80%',90:'90%',100:'100%'}" />
				</td>
			</tr>
			<tr><td colspan="2">
				<table class="multiBox">
					<tr>							
						<td><s:text name="decorationMedal"/>:</td>
						<td>
							<div id="checkBoxList">
								<s:checkboxlist name="veteran.decorationMedals" id="decorationMedals" list="decorationMedalList" listKey="id" listValue="value" theme="custom" />
							</div>
						</td>
					</tr>				
				</table>			
			</td></tr>
			<tr><td colspan="2">&nbsp;</td></tr>
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
			
		</table>
	
	</div>
	<div class="clear"></div>
	<br />
	<div style="text-align: right; margin-right: 16px;">
		<s:hidden name="veteran.id" id="veteranId" />
		<s:hidden name="veteran.contactable" />
		<s:hidden name="veteran.sourceDate" />
		<s:hidden name="veteran.vaCurrent" />
		<s:hidden name="veteran.reviewed" />
		<s:hidden name="veteran.active" />
		<s:hidden name="veteran.insertTimestamp" />
		<s:hidden name="veteran.createdBy.id" />
		<s:hidden name="veteran.vetPageTitle" id="vetPageTitleId" />
		<input type="button" name="cancel" value="<s:text name="button.cancel"/>" class="button" onclick="return onCancel();" >
		<s:if test="veteran.vetPageTitle == 'Edit Veteran'">
			<input type="button" name="returnToSearch" value="<s:text name="button.returnToSearch"/>" class="button" onclick="return backToSearch();" >
		</s:if>
		<s:reset name="reset" value="%{getText('button.reset')}" cssClass="button"/>
		<s:if test="veteran.vaCurrent == 1 || veteran.vetPageTitle == 'New Entry'">
			<input type="button" name="addDoc" value="<s:text name="button.addDoc"/>" class="secondaryButton" onclick="onAddDoc()" >
		</s:if>
		<s:if test="veteran.attachmentsList.size() > 0">
			<input type="button" name="showDocuments" value="<s:text name="button.showDocuments"/>" class="secondaryButton" onclick="onShowDoc()" >
		</s:if>
		<s:submit name="save" value="%{getText('button.save')}" cssClass="primaryButton" onclick="return onSave();" />
	</div>
	<br />
</s:form>

<script language="javascript" type="text/javascript">
   initDwr();
</script>	

<div id="panel4">
	<div class="hd">
		Duplicate SSN
	</div>
	<div class="bd">
		Social Security Number already exists! 
		<br /><br />
		Click <a href="javascript:goToEditCompare()">here</a> to go to the Edit/Compare screen.
		<br /><br />
	</div>
</div>  

<div id="panel5">
	<div class="hd">
		Veteran attachments Add
		
	</div>
	<div class="bd">
		<s:form action="saveVeteranAttachments" namespace="/" method="post" enctype="multipart/form-data" name="veteranAttachmentForm">
			<h1><s:text name="veteran.attachmentTitle"/></h1>
			<table border="0" cellspacing="0" cellpadding="2">
				<tr><td>
					<table>
						<tr>
							<td width="25%"><s:text name="docType" />:</td>
							<td id="radioButtonList" width="75%">
								<s:radio name="veteranAttachment.docType.id" list="docTypeList" listKey="id" listValue="value" onclick="toggleAttachmentTxt(this)" value="%{getFirstDd214()}" theme="custom" />
							</td>
						</tr>
					</table>
				</td></tr>
				<!-- yui paneling misalign - table tags to work around  for FF when toggleAttachmentTxt() is called. Works fine in IE8 -->
				<tr><td>
					<table>
						<tr>
							<td width="25%"><s:text name="veteran.image" />:</td>
							<td width="75%"><s:file name="attachment" size="40" id="attachmentId" /></td>
						</tr>
					</table>				
				</td></tr>
				<tr><td>
					<table id="attachmentTxtRowId">
						<tr>
							<td width="25%"><s:text name="veteran.text" />:</td>
							<td width="75%"><s:file name="attachmentTxt" size="40" id="attachmentTxtId" /></td>
						</tr>
					</table>				
				</td></tr>
				<tr><td>&nbsp;</td></tr>
				<tr>
					<td>
						<s:hidden name="veteranAttachment.veteran.id" id="veteranAttachmentId"/>
						<s:hidden name="veteranAttachment.pageTitle" id="veteranAttachmentPageTitleId"/>
						<s:hidden name="dd214" id="dd214Id" />
						<s:submit name="load" value="%{getText('button.load')}" cssClass="primaryButton" onclick="return onSaveAttachment();" />
					</td>
				</tr>
		
			</table>
		</s:form>
	</div>
</div>  

<div id="panel6">
	<div class="hd">
		Veteran attachments List
	</div>
	<div class="bd">
		<div class="displaytag">
			<display:table name="veteran.attachmentsList" export="false" id="row" requestURI="">
				<display:column title="File Name" property="attachmentFileName" />
				<display:column title="Format" property="attachmentContentType" />
				<display:column title="Doc Type" property="docType.name" />
				<display:column title="Uploaded by">${row.createdBy.firstName} ${row.createdBy.lastName}</display:column>
				<display:column title="Date" property="insertTimestamp" decorator="gov.utah.dts.det.displaytag.decorator.DisplayDate" />
				<display:column title="Action" style="text-align: center;">
					<s:a href="javascript:showAttachment(%{#attr.row.id});" title="View"><img src="<s:url value='/images/view-icon.gif'/>"/></s:a>
					<s:a href="javascript:deleteAttachment(%{#attr.row.id});" title="Delete"><img src="<s:url value='/images/delete-icon.png'/>"/></s:a>
				</display:column>			
			</display:table>
		</div>		
	</div>
</div>  

<!-- yui panels -->
<s:include value="panelsInclude.jsp"></s:include>

</body>
</html>