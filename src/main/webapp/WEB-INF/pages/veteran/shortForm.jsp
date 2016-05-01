<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib prefix="s" uri="/struts-tags"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><s:text name="online_veteran" /></title>

<script type="text/javascript">
function onSave() {
	YAHOO.vts.container.wait.show();
	if (document.getElementById("veteranId").value != "") {	// update the form
		document.getElementById("veteranForm").action = "updateShortForm";	
	}
	
	return true;	
}

function onReset() {
	window.location.href = '<s:url namespace="/" action="ShortForm" />';
}

function populateName() {
	var name = document.getElementById("lastName").value.split(" ");
	if (name.length > 1) {
		document.getElementById("lastName").value = name[0];
		document.getElementById("firstName").value = name[1];
		document.getElementById("middleName").value = name[2];		
	}
}

function populateCity() {
	var city = document.getElementById("city").value.split(" ");
	if (!/\D/.test(city[city.length-1])) {
		document.getElementById("state").value = city[city.length-2];
		document.getElementById("zip").value = city[city.length-1];
		document.getElementById("city").value = city[0];
		for (var i = 1; i < city.length-2; i++) {
			document.getElementById("city").value = document.getElementById("city").value + " " + city[i];
		}
	}
	
}

</script>

</head>
<body>
<s:form action="insertShortForm" method="post" enctype="multipart/form-data" id="veteranForm">
	<h1>New Short Entry</h1>
	<p>Asterisk <font color=red>*</font> indicates a required field.</p>
	<p>When a DD214 in PDF format for supported forms is detected in Document Upload section, its content info will be retrieved and saved to database automatically. </p>
	<s:actionerror/>
	<s:actionmessage/>
	<table width="450" border="0" cellspacing="3" cellpadding="3">
		<tr>
			<td><font color=red>*</font><s:text name="veteran.ssn2"/>: 
				<s:textfield name="veteran.ssn" cssClass="fieldset100" maxlength="16"/>
			</td>
			<td style="text-align: right;"><s:text name="recordType" />:
				<select id="veteranForm_veteran_recordType_id" name="veteran.recordType.id" onchange="setVerified(this);">
					<option selected="selected" value="2">DD214</option>
					<option value="6">Manual</option>
				</select>
			</td>
		</tr>
		<tr>
			<td>
				<s:text name="person.firstName"/>: <s:textfield name="veteran.firstName" id="firstName" cssClass="fieldset135"/>
			</td>
			<td style="text-align: right;">
				<s:text name="person.middleName"/>: <s:textfield name="veteran.middleName" id="middleName" cssClass="fieldset100"/>
			</td>
		</tr>
		<tr>
			<td>
				<s:text name="person.lastName"/>: <s:textfield name="veteran.lastName" id="lastName" onchange="populateName();" cssClass="fieldset135"/>
			</td>
			<td style="text-align: right;">
				<s:text name="person.suffix"/>: <s:textfield name="veteran.suffix" id="suffix" cssClass="fieldset100" maxlength="6"/>
			</td>
		</tr>
		<tr>
			<td colspan="2"><s:text name="veteran.dob"/>: 
				<s:textfield name="veteran.dateOfBirthStr" cssClass="fieldset100" maxlength="10"/> (YYYYMMDD)
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<s:text name="veteran.address1"/>: <s:textfield name="veteran.address1" id="address1" cssClass="fieldset275"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<s:text name="veteran.address2"/>: <s:textfield name="veteran.address2" cssClass="fieldset275"/>
			</td>
		</tr>
		<tr>
			<td><s:text name="veteran.city"/>: <s:textfield name="veteran.city" id="city" onchange="populateCity();" cssClass="fieldset135"/></td>
			<td style="text-align: right;"><s:text name="veteran.state"/>: <s:select name="veteran.state" id="state" list="stateMap" /></td>
		</tr>
		<tr>
			<td>
				<s:text name="veteran.zip"/>: <s:textfield name="veteran.zip" id="zip" cssClass="fieldset75" />
			</td>
			<td style="text-align: right;"><s:text name="person.phone"/>:
				<s:textfield name="veteran.primaryPhone" cssClass="fieldset100" maxlength="12"/>
			</td>
		</tr>
		<tr>
			<td>
				<s:text name="veteran.gender"/>: <s:select name="veteran.gender" list="#{'Male':'Male','Female':'Female'}" headerKey="" headerValue="" />
			</td>
			<td style="text-align: right;">
				<s:text name="veteran.ethnicity"/>: <s:select name="veteran.ethnicity.id" list="ethnicityList" listKey="id" listValue="value" headerKey="" headerValue="" />
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
		<tr>
			<td colspan="2">
				<p><strong><s:text name="decorationMedal2"/></strong></p>
				<div id="checkBoxList">
					<s:checkboxlist name="veteran.decorationMedals" id="decorationMedals" list="decorationMedalList" listKey="id" listValue="value" />
				</div>
			</td>
		</tr>
		<s:if test="veteran.id == null">
			<tr>
				<td colspan="2">
					<p><strong>Document Upload</strong></p>
					<p id="radioButtonList"><s:text name="docType" />:<br /> 
					<s:radio name="docType.id" list="docTypeList" listKey="id" listValue="value" onclick="toggleAttachmentTxt(this)" theme="custom" /></p>
					<p><s:text name="veteran.image" />: <s:file name="attachment" size="40" id="attachmentId" /></p>
				</td>
			</tr>
		</s:if>
	</table>
	<br />
	<div style="text-align: right;">
		<s:hidden name="veteran.id" id="veteranId" />
		<s:hidden name="oldSsn" />
		<input type="button" name="cancel" value="<s:text name="button.cancel"/>" class="button" onclick="history.back();" >
		<s:reset name="reset" value="%{getText('button.reset')}" cssClass="button" onclick="return onReset();"/>
		
		<s:if test="veteran.id == null">
			<s:submit name="submit" value="%{getText('button.submit')}" cssClass="primaryButton" onclick="return onSave();" />
		</s:if>
		<s:else>
			<s:submit name="submit" value="Update" cssClass="primaryButton" onclick="return onSave();" />
		</s:else>
		
		
	</div>
	
</s:form>
</body>
</html>