<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VIS <s:text name="veteran" /></title>	

<link rel="stylesheet" type="text/css" href="<s:url value='/css/displaytagLib.css'/>" />

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
	
}

YAHOO.util.Event.onDOMReady(init);

function initDwr() {
	fillServiceTable();
	fillBenefitTypeTable();
}

var serviceCache = { };
var benefitTypeCache = { };
var benefitRecipientCache = { };

function fillServiceTable() {
	var veteranId = dwr.util.getValue("veteranId");
	
	VeteranService.getVeteranServices(veteranId, function(data) {
	    var veteranServicePeriod, id;
	    for (var i = 0; i < data.length; i++) {
	    	veteranServicePeriod = data[i];
	    	id = veteranServicePeriod.id;
	        serviceCache[id] = veteranServicePeriod;
	    }
	    
	});
}

function viewService(id) {
	var veteranServicePeriod = serviceCache[id];
	dwr.util.setValues(veteranServicePeriod);
		  
	YAHOO.vts.container.panel1.show();
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
	    	dwr.util.setValue("benefitTypeName" + id, veteranBenefit.benefitTypeName);
	    	
	        $("benefitTypePattern" + id).style.display = ""; // officially we should use table-row, but IE prefers "" for some reason
	        benefitTypeCache[id] = veteranBenefit;
	    }
	    
	});
}

function viewBenefitType(eleid) {
	// we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
	var veteranBenefit = benefitTypeCache[eleid.substring(4)];
	dwr.util.setValues(veteranBenefit);
		
	// get benefit recipient list
	fillBenefitRecipientTable();
	clearBenefitRecipient();
	  
	YAHOO.vts.container.panel2.show();
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

function clearBenefitRecipient() {
	dwr.util.setValues({ idBenefitRecipient:null, veteranBenefitId:null, relationId:null, firstName:null, lastName:null, address1:null, address2:null, city:null, state:'UT', zip:null, phone1:null, phone2:null, phone3:null, activeBenefitRecipient:null, insertTimestampBenefitRecipient:null });
}

function editBenefitRecipient(eleid) {
	// we were an id of the form "edit{id}", eg "edit42". We lookup the "42"
	var veteranBenefitRecipient = benefitRecipientCache[eleid.substring(4)];
	dwr.util.setValues(veteranBenefitRecipient);
		  
	YAHOO.vts.container.panel2.hide();
	YAHOO.vts.container.panel3.show();
}

function onCloseBenefitRecipient() {
	YAHOO.vts.container.panel3.hide();
	YAHOO.vts.container.panel2.show();
}

function backToSearch() {
	<logic:notPresent role="VSO">
		window.location.href = '<s:url namespace="/" action="displaySearchVeteranSearch" />';
	</logic:notPresent>
	<logic:present role="VSO">
		window.location.href = '<s:url namespace="/" action="displaySearchVeteranSearch?vaCurrent" />';
	</logic:present>
}

function printPdf(image) {
	var ssn = document.getElementById("ssn").value;
	var link = '<s:url namespace="/report" action="generatePdfIndReport" />';
	if (image != undefined) {
		link = link + "?ssn=" + ssn + "&image=y";
	} else {
		link = link + "?ssn=" + ssn;
	}
	popupWindow(link,"PDFReport", 820, 950);
}

</script>

</head>
<body>

<s:form action="displaySearchVeteranSearch" id="veteranForm">
	<div id="rotatingPhoto">
		<p><a name="contentSkip" id="contentSkip"></a>
	    	<script language="javascript">rotatingPhoto(photo_array);</script>
	   	</p>
		
		<table style="border: 2px #CC9 solid;" width="405" border="0" cellspacing="2" cellpadding="2">
			<tr>
				<td colspan="2"><Strong>Service Benefits</Strong></td>
			</tr>
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
								<a href="#" id="view" onclick="javascript:viewBenefitType(this.id);">View</a>
							</td>
						</tr>
					</tbody>
				</table>
			</td></tr>
		</table>
	   	<br />
	   	<table  width="405" border="0" cellspacing="2" cellpadding="2">
	   		<tr><td colspan="2"><strong><s:text name="veteran.noteHistory" />:</strong></td></tr>
			<tr>
				<td colspan="2">
					<display:table name="veteran.noteList" export="false" id="row" requestURI="" style="width:100%;">
						<display:column title="Note Text" property="noteText" />
					</display:table>
				</td>
			</tr>
	   	</table>
	   	  	
	   	<div class="clear"></div>
	</div>
	
	<div id="mainContent">
		<h1><s:text name="veteran.view" /></h1>
		<table width="450" border="0" cellspacing="2" cellpadding="2">
			<tr>
				<td>
					<s:if test="veteran.verified == 1"><b>Verified</b></s:if>
					<s:else><b>Non-verified</b></s:else>
				</td>
				<td>
					<s:text name="recordType" />: <b><s:property value="veteran.recordType.name" /></b>
				</td>
			</tr>
			<tr>
				<td>
					<s:if test="veteran.vaEnrolled == true"><b>Enrolled in VA</b></s:if>
					<s:else><b>Not enrolled in VA</b></s:else>
				</td>
				<td>
					<s:if test="veteran.vaMedEnrolled == true"><b>Enrolled in VA Medical</b></s:if>
					<s:else><b>Not enrolled in VA Medical</b></s:else>
				</td>
			</tr>
			<tr>
				<td>
					<s:if test="veteran.shareFederalVa == true"><b>OK to share information with federal VA</b></s:if>
					<s:else><b>Not ok to share information with federal VA</b></s:else>
				</td>
				<td style="text-align: right;">
					<s:if test="veteran.vaCurrent == 1"><strong>Current Veteran</strong></s:if>
				</td>
			</tr>
			<tr>
				<td><s:text name="veteran.ssn"/>: <b><s:property value="veteran.ssn" /></b></td>
				<td><s:text name="veteran.dob"/>: <b><s:date name="veteran.dateOfBirth" format="MM/dd/yyyy"/></b></td>
			</tr>
			<tr>
				<td><s:text name="person.firstName"/>: <b><s:property value="veteran.firstName" default=""/></b></td>
				<td><s:text name="person.middleName"/>: <b><s:property value="veteran.middleName" default=""/></b></td>
			</tr>
			<tr>
				<td colspan="2"><s:text name="person.lastName"/>: <b><s:property value="veteran.lastName" default=""/></b></td>
			</tr>
			<tr>
				<td><s:text name="veteran.ethnicity"/>: <b><s:property value="veteran.ethnicity.name" default=""/></b></td>
				<td><s:text name="veteran.gender"/>: <b><s:property value="veteran.gender" default=""/></b></td>
			</tr>
			<tr><td colspan="2"><strong>Street Address</strong></td></tr>
			<tr><td colspan="2">
				<table width="450" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td>
							<s:text name="veteran.address1"/>: <b><s:property value="veteran.address1" default=""/></b>
						</td>
					</tr>
					<tr>
						<td>
							<s:text name="veteran.address2"/>: <b><s:property value="veteran.address2" default=""/></b>
						</td>
					</tr>
					<tr>
						<td>
							<s:text name="veteran.city"/>: <b><s:property value="veteran.city" default=""/></b>
							<s:text name="veteran.state"/>: <b><s:property value="veteran.state" default=""/></b>
							<s:text name="veteran.zip"/>: <b><s:property value="veteran.zip" default=""/></b>
						</td>
					</tr>
				</table>
			</td></tr>
			

			<tr><td colspan="2">
				<strong>Mailing Address Different?</strong>
				<s:if test="veteran.mailingAddr == \"y\"">Yes
					<table width="450" border="0" cellspacing="2" cellpadding="2">
						<tr>
							<td>
								<s:text name="veteran.address1"/>: <b><s:property value="veteran.mailingAddr1" default=""/></b>
							</td>
						</tr>
						<tr>
							<td>
								<s:text name="veteran.address2"/>: <b><s:property value="veteran.mailingAddr2" default=""/></b>
							</td>
						</tr>
						<tr>
							<td>
								<s:text name="veteran.city"/>: <b><s:property value="veteran.mailingCity" default=""/></b>
								<s:text name="veteran.state"/>: <b><s:property value="veteran.mailingState" default=""/></b>
								<s:text name="veteran.zip"/>: <b><s:property value="veteran.mailingZip" default=""/></b>
							</td>
						</tr>									
					</table>
				</s:if>
				<s:else>No</s:else>
			</td></tr>
			<tr>
				<td><s:text name="veteran.primaryPhone"/>: <b><s:property value="veteran.primaryPhone" default=""/></b></td>
				<td><s:text name="veteran.altPhone"/>: <b><s:property value="veteran.altPhone" default=""/></b></td>
			</tr>
			<tr>
				<td colspan="2">
					<s:text name="veteran.email"/>: <b><s:property value="veteran.email" default=""/></b>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					Preferable Email Option: 
					<s:if test="veteran.emailOption == 1"><b>Regular Mail</b></s:if>
					<s:elseif test="veteran.emailOption == 2"><b>Email</b></s:elseif>
					<s:else><b>No Preference</b></s:else>
				</td>
			</tr>
			<tr><td colspan="2"><strong>Alternate Contact</strong></td></tr>
			<tr><td colspan="2">
				<table width="450" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td><s:text name="person.firstName"/>: <b><s:property value="veteran.altContactFirstName" default=""/></b></td>
						<td><s:text name="person.lastName"/>: <b><s:property value="veteran.altContactLastName" default=""/></b></td>
					</tr>
					<tr>
						<td colspan="2">
							<s:text name="veteran.address1"/>: <b><s:property value="veteran.altContactAddr1" default=""/></b>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<s:text name="veteran.address2"/>: <b><s:property value="veteran.altContactAddr2" default=""/></b>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<s:text name="veteran.city"/>: <b><s:property value="veteran.altContactCity" default=""/></b>
							<s:text name="veteran.state"/>: <b><s:property value="veteran.altContactState" default=""/></b>
							<s:text name="veteran.zip"/>: <b><s:property value="veteran.altContactZip" default=""/></b>
						</td>
					</tr>
					<tr>
						<td><s:text name="person.phone"/>: <b><s:property value="veteran.altContactPhone" default=""/></b></td>
						<td><s:text name="veteran.relationship"/>: <b><s:property value="veteran.relation.name" default=""/></b> </td>
					</tr>				
				</table>
			</td></tr>
			<tr><td colspan="2"><strong>Military Information</strong></td></tr>
			<tr><td colspan="2">
				<table width="450" border="0" cellspacing="2" cellpadding="2">
					<tr>
						<td><s:text name="veteran.dod"/>: <b><s:date name="veteran.dateOfDeath" format="MM/dd/yyyy" /></b></td>
						<td><s:text name="veteran.disability"/>: <b><s:property value="veteran.percentDisability"/>%</b></td>
					</tr>
					<tr>
						<td colspan="2"><s:text name="veteran.decorationMedal"/>: 
							<s:iterator value="veteran.decorationMedalList">
								<b>[<s:property value="name"/>]</b>
							</s:iterator>							
						</td>
					</tr>
				</table>
			</td></tr>
			<tr>
				<td colspan="2">
					<table style="border: 2px #CC9 solid;" width="428" border="0" cellspacing="2" cellpadding="2">
						<tr>
							<td><Strong>Service Periods</Strong></td>
						</tr>
						<tr><td>
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
								<tbody>
									<s:iterator value="veteran.servicePeriodList" status="row">
										<tr>
											<td><s:property value="serviceBranch.name" /></td>
											<td><s:property value="serviceEra.name" /></td>
											<td><s:date name="startDate" format="MM/dd/yyyy" /></td>
											<td><s:date name="endDate" format="MM/dd/yyyy" /></td>
											<td style="text-align: center;">
												<a href="javascript:viewService(<s:property value="id" />)">View</a>
											</td>
										</tr>
									</s:iterator>								
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
		<s:hidden name="veteran.ssn" id="ssn" />
		<input type="button" name="pdf" value="<s:text name="button.pdf"/>" class="button" onclick="printPdf();" >
		<input type="button" name="pdfImage" value="<s:text name="button.pdfImage"/>" class="button" onclick="printPdf('y');" >
		<input type="button" name="returnToSearch" value="<s:text name="button.returnToSearch"/>" class="button" onclick="backToSearch();" >
	</div>
	<br />
</s:form>	

<script language="javascript" type="text/javascript">
   initDwr();
</script>

<div id="panel1">
	<div class="hd">
		Service Period
	</div>
	<div class="bd">
		<table border="0" cellspacing="2" cellpadding="2">
			<tr>
				<td><s:text name="veteran.serviceBranch"/>:</td>
				<td>
					<s:select name="serviceBranchId" id="serviceBranchId" list="serviceBranchList" listKey="id" listValue="value" cssStyle="color:black" disabled="true"/>
				</td>
			</tr>
			<tr>
				<td><s:text name="veteran.servicePeriod" />:</td>
				<td>
					<table border="0" cellspacing="2" cellpadding="2">
						<tr>
							<td>From:</td>
							<td><s:select name="monthFrom" id="monthFrom" list="monthMap" headerKey="" headerValue="MM" cssStyle="color:black" disabled="true"/>/
								<s:select name="dayFrom" id="dayFrom" list="dayMap" headerKey="" headerValue="DD" cssStyle="color:black" disabled="true"/>/
								<s:select name="yearFrom" id="yearFrom" list="yearMap" headerKey="" headerValue="YYYY" cssStyle="color:black" disabled="true"/>
							</td>
						</tr>
						<tr>
							<td>To:</td>
							<td><s:select name="monthTo" id="monthTo" list="monthMap" headerKey="" headerValue="MM" cssStyle="color:black" disabled="true"/>/
								<s:select name="dayTo" id="dayTo" list="dayMap" headerKey="" headerValue="DD" cssStyle="color:black" disabled="true"/>/
								<s:select name="yearTo" id="yearTo" list="yearMap" headerKey="" headerValue="YYYY" cssStyle="color:black" disabled="true"/> 
							</td>
						</tr>									
					</table>
				</td>
			</tr>
			<tr>
				<td><s:text name="veteran.combatZone" />:</td>
				<td><s:textfield name="combatZone" id="combatZone" cssClass="fieldset225" cssStyle="color:black" disabled="true"/></td>
			</tr>
			<tr>
				<td colspan="2">
					<s:text name="veteran.dischargeType"/>: <s:select name="dischargeTypeId" id="dischargeTypeId" list="dischargeTypeList" listKey="id" listValue="value" cssStyle="color:black" disabled="true"/>
				</td>
			</tr>
		</table>
	</div>
</div>  

<div id="panel2">
	<div class="hd">
		Benefit Type
	</div>
	<div class="bd">
		<center>
		<table width="380" border="0" cellspacing="2" cellpadding="2">
			<tr><td>
				<s:text name="benefitType"/>: <s:select name="benefitTypeId" id="benefitTypeId" list="benefitTypeList" listKey="id" listValue="value" headerKey="" headerValue="" cssStyle="color:black" disabled="true"/>
			</td></tr>
			<tr id="beneficiaryTable"><td>		
				<table width="370" border="0" cellspacing="2" cellpadding="2">
					<thead>
						<tr>
							<th>Beneficiary</th>
							<th>Address</th>
							<th>Action</th>
						</tr>
					</thead>
					<tbody id="benefitRecipientBody">
						<tr id="benefitRecipientPattern" style="display:none;">
							<td><span id="benefitRecipientBeneficiary">&nbsp;</span></td>
							<td><span id="benefitRecipientAddress">&nbsp;</span></td>
							<td style="text-align: center;">
								<a href="#" id="edit" onclick="javascript:editBenefitRecipient(this.id);">View</a>
							</td>
						</tr>
					</tbody>
				</table>
			</td></tr>
			<tr id="beneficiaryButtons"><td style="text-align: right;">
					<s:hidden name="idBenefitType" />
					<s:hidden name="activeBenefitType" />
					<s:hidden name="insertTimestampBenefitType" />
			</td></tr>
		</table>
		</center>
	</div>
</div>  

<div id="panel3">
	<div class="hd">
		Benefit Recipient
	</div>
	<div class="bd">
		<table width="420" border="0" cellspacing="0" cellpadding="2">
			<tr><td colspan="2"><strong>Beneficiary(s) Other Than Veteran</strong></td></tr>
			<tr>
				<td>
					<s:text name="person.firstName"/>: <s:textfield name="firstName" id="firstName" cssClass="fieldset125" cssStyle="color:black" disabled="true"/>
				</td>
				<td align="right">
					<s:text name="person.lastName"/>: <s:textfield name="lastName" id="lastName" cssClass="fieldset125" cssStyle="color:black" disabled="true"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<s:text name="veteran.address1"/>: <s:textfield name="address1" cssClass="fieldset275" cssStyle="color:black" disabled="true"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<s:text name="veteran.address2"/>: <s:textfield name="address2" cssClass="fieldset275" cssStyle="color:black" disabled="true"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<s:text name="veteran.city"/>: <s:textfield name="city" cssClass="fieldset135" cssStyle="color:black" disabled="true"/>
					<s:text name="veteran.state"/>: <s:select name="state" list="stateMap" value="%{'UT'}" cssStyle="color:black" disabled="true"/>
				</td>
			</tr>
			<tr>
				<td>
					<s:text name="veteran.zip"/>: <s:textfield name="zip" cssClass="fieldset75" cssStyle="color:black" disabled="true"/>
				</td>
				<td>
					<s:text name="person.phone"/>: <s:textfield name="phone1" cssClass="fieldset25" maxlength="3" cssStyle="color:black" disabled="true"/>-<s:textfield name="phone2" cssClass="fieldset25" maxlength="3" cssStyle="color:black" disabled="true"/>-<s:textfield name="phone3" cssClass="fieldset50" maxlength="4" cssStyle="color:black" disabled="true"/>
				</td>
			</tr>
			<tr>
				<td colspan="2"><s:text name="veteran.relationship"/>: 
					<s:select name="relationId" list="relationList" listKey="id" listValue="value" cssStyle="color:black" disabled="true"/>
				</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr><td>
				<s:hidden name="idBenefitRecipient" />
				<s:hidden name="activeBenefitRecipient" />
				<s:hidden name="insertTimestampBenefitRecipient" />
				<input type="button" name="close" value="<s:text name="button.close"/>" class="button" onclick="return onCloseBenefitRecipient();" >
			</td></tr>
		</table>
	</div>
</div>  

</body>
</html>