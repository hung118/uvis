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
	return true;	
}

</script>

</head>
<body>
<s:form namespace="/public" action="saveRegister" id="veteranForm">
	<h1><s:text name="online_veteran" /></h1>
	<p>Thank you for registering with the Utah Department of Veterans Affairs. This will allow us to contact you when changes to the Programs and Services of the Veterans Administration occur.</p>
	<p>Asterisk <font color=red>*</font> indicates a required field.</p>
	<s:actionerror/>
	<table width="450" border="0" cellspacing="3" cellpadding="3">
		<tr>
			<td colspan="2"><font color=red>*</font><s:text name="veteran.ssn"/>: 
				<s:textfield name="veteran.ssn1" onkeyup="return autoTab(this, 3, event);" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="veteran.ssn2" onkeyup="return autoTab(this, 2, event);" cssClass="fieldset15" maxlength="2"/>-<s:textfield name="veteran.ssn3" cssClass="fieldset50" maxlength="4"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<s:checkbox name="veteran.shareFederalVa" cssClass="chkbox"/><s:text name="veteran.shareFederalVa" />
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
			<td>
				<s:text name="person.firstName"/>: <s:textfield name="veteran.firstName" cssClass="fieldset135"/>
			</td>
			<td style="text-align: right;">
				<s:text name="person.middleName"/>: <s:textfield name="veteran.middleName" cssClass="fieldset100"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<s:text name="person.lastName"/>: <s:textfield name="veteran.lastName" cssClass="fieldset135"/>
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
		<tr><td colspan="2"><strong>Street Address</strong></td></tr>
		<tr>
			<td colspan="2">
				<s:text name="veteran.address1"/>: <s:textfield name="veteran.address1" cssClass="fieldset370"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<s:text name="veteran.address2"/>: <s:textfield name="veteran.address2" cssClass="fieldset370"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<s:text name="veteran.city"/>: <s:textfield name="veteran.city" cssClass="fieldset135"/>
				<s:text name="veteran.state"/>: <s:select name="veteran.state" list="stateMap" value="%{'UT'}" />
				<s:text name="veteran.zip"/>: <s:textfield name="veteran.zip" cssClass="fieldset75" />
			</td>
		</tr>
		<tr>
			<td colspan="2"><s:text name="person.phone"/>: &nbsp;
				<s:textfield name="veteran.primaryPhone1" onkeyup="return autoTab(this, 3, event);" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="veteran.primaryPhone2" onkeyup="return autoTab(this, 3, event);" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="veteran.primaryPhone3" cssClass="fieldset50" maxlength="4"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<s:text name="veteran.email"/>: <s:textfield name="veteran.email" cssClass="fieldset340"/>
			</td>
		</tr>
		<tr><td colspan="2"><strong>Military Information</strong></td></tr>
		<tr>
			<td>
				<s:checkbox name="veteran.vaEnrolled" id="vaEnrolled" cssClass="chkbox" /><s:text name="veteran.vaEnrolled"/>
			</td>
			<td style="text-align: right;">
				<s:text name="veteran.disability"/>: <s:select name="veteran.percentDisability" list="#{0:'0%',10:'10%',20:'20%',30:'30%',40:'40%',50:'50%',60:'60%',70:'70%',80:'80%',90:'90%',100:'100%'}" />
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<p><strong><s:text name="decorationMedal2"/></strong></p>
				<p><s:checkboxlist name="veteran.decorationMedals" id="decorationMedals" list="decorationMedalList" listKey="id" listValue="value" /></p>
			</td>
		</tr>
		<tr><td colspan="2">
			<table style="border: 1px #CCC solid;" width="450" border="0" cellspacing="3" cellpadding="3">
				<tr>
					<td colspan="2"><Strong>Service Periods</Strong></td>
				</tr>
				<tr>
					<td><s:text name="veteran.serviceBranch"/>:</td>
					<td>
						<s:select name="serviceBranchId" id="serviceBranchId" list="serviceBranchList" listKey="id" listValue="value" headerKey="" headerValue="" />
					</td>
				</tr>
				<tr>
					<td><s:text name="veteran.servicePeriod" />:</td>
					<td>
						<table border="0" cellspacing="0" cellpadding="2">
							<tr>
								<td>From:</td>
								<td><s:select name="monthFrom" id="monthFrom" list="monthMap" headerKey="" headerValue="MM" />/
									<s:select name="dayFrom" id="dayFrom" list="dayMap" headerKey="" headerValue="DD" />/
									<s:select name="yearFrom" id="yearFrom" list="yearMap" headerKey="" headerValue="YYYY" />
								</td>
							</tr>
							<tr>
								<td>To:</td>
								<td><s:select name="monthTo" id="monthTo" list="monthMap" headerKey="" headerValue="MM" />/
									<s:select name="dayTo" id="dayTo" list="dayMap" headerKey="" headerValue="DD" />/
									<s:select name="yearTo" id="yearTo" list="yearMap" headerKey="" headerValue="YYYY" /> 
								</td>
							</tr>									
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<s:text name="veteran.dischargeType"/>: <s:select name="dischargeTypeId" id="dischargeTypeId" list="dischargeTypeList" listKey="id" listValue="value" headerKey="" headerValue=""/>
					</td>
				</tr>
			</table>
		</td></tr>
	</table>
	<br />
	<div style="text-align: right;">
		<input type="button" name="cancel" value="<s:text name="button.cancel"/>" class="button" onclick="history.back();" >
		<s:reset name="reset" value="%{getText('button.reset')}" cssClass="button"/>
		<s:submit name="save" value="%{getText('button.submit')}" cssClass="primaryButton" onclick="return onSave();" />
	</div>
	
</s:form>
</body>
</html>