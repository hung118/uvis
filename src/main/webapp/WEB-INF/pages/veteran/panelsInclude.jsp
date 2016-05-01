<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<div id="panel1">
	<div class="hd">
		Service Period
	</div>
	<div class="bd">
		<table border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td><s:text name="veteran.serviceBranch"/>:</td>
				<td>
					<s:select name="serviceBranchId" id="serviceBranchId" list="serviceBranchList" listKey="id" listValue="value" />
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
				<td><s:text name="veteran.combatZone"/>:</td> 
				<td><s:select name="combatZoneId" id="combatZoneId" list="combatZoneList" listKey="id" listValue="value" headerKey="" headerValue=""/></td>
			</tr>
			<tr>
				<td><s:text name="veteran.dischargeType"/>:</td> 
				<td><s:select name="dischargeTypeId" id="dischargeTypeId" list="dischargeTypeList" listKey="id" listValue="value" headerKey="" headerValue=""/></td>
			</tr>
			<tr><td colspan="2" style="text-align: right;">
				<s:hidden name="idService" />
				<s:hidden name="activeService" />
				<s:hidden name="insertTimestampService" />	
				<input type="button" name="cancel" value="<s:text name="button.cancel"/>" class="button" onclick="return onCloseServicePeriod();" >						
				<input type="button" name="save" value="<s:text name="button.save"/>" class="button" onclick="saveServicePeriod();" >
			</td></tr>
		</table>
	</div>
</div>  

<div id="panel2">
	<div class="hd">
		Benefit Type
	</div>
	<div class="bd">
		<center>
		<table width="380" border="0" cellspacing="0" cellpadding="2">
			<tr><td>
				<s:text name="benefitType"/>: <s:select name="benefitTypeId" id="benefitTypeId" list="benefitTypeList" listKey="id" listValue="value" headerKey="" headerValue="" />
			</td></tr>
			<tr id="beneficiaryButton"><td style="text-align: right;">
				<input type="button" name="reset" value="<s:text name="button.newBeneficiary"/>" class="button" onclick="onBenefitRecipientView();" >
			</td></tr>
			<tr id="beneficiaryTable"><td>		
				<table width="370">				
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
								<a href="#" id="edit" onclick="javascript:editBenefitRecipient(this.id);" title="Edit"><img src="<s:url value='/images/edit-icon.png'/>"/></a>
								&nbsp; <a href="#" id="delete" onclick="javascript:deleteBenefitRecipient(this.id);" title="Delete"><img src="<s:url value='/images/delete-icon.png'/>"/></a>
							</td>
						</tr>
					</tbody>
				</table>
			</td></tr>
			<tr><td>&nbsp;</td></tr>
			<tr id="beneficiaryButtons"><td style="text-align: right;">
					<s:hidden name="idBenefitType" />
					<s:hidden name="activeBenefitType" />
					<s:hidden name="insertTimestampBenefitType" />
				<input type="button" name="cancel" value="<s:text name="button.cancel"/>" class="button" onclick="return onCloseBenefitType();" >	
				<input type="button" name="save" value="<s:text name="button.save"/>" class="button" onclick="saveBenefitType();" >
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
		<form action="">
		<table width="420" border="0" cellspacing="0" cellpadding="2">
			<tr><td colspan="2"><strong>Beneficiary(s) Other Than Veteran</strong></td></tr>
			<tr>
				<td>
					<s:text name="person.firstName"/>: <s:textfield name="firstName" id="firstName" cssClass="fieldset125"/>
				</td>
				<td align="right">
					<s:text name="person.lastName"/>: <s:textfield name="lastName" id="lastName" cssClass="fieldset125"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<s:text name="veteran.address1"/>: <s:textfield name="address1" cssClass="fieldset275"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<s:text name="veteran.address2"/>: <s:textfield name="address2" cssClass="fieldset275"/>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<s:text name="veteran.city"/>: <s:textfield name="city" cssClass="fieldset135"/>
					<s:text name="veteran.state"/>: <s:select name="state" list="stateMap" headerKey="" headerValue="" />
				</td>
			</tr>
			<tr>
				<td>
					<s:text name="veteran.zip"/>: <s:textfield name="zip" cssClass="fieldset75" />
				</td>
				<td>
					<s:text name="person.phone"/>: <s:textfield name="phone1" onkeyup="return autoTab(this, 3, event);" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="phone2" onkeyup="return autoTab(this, 3, event);" cssClass="fieldset25" maxlength="3"/>-<s:textfield name="phone3" cssClass="fieldset50" maxlength="4"/>
				</td>
			</tr>
			<tr>
				<td colspan="2"><s:text name="veteran.relationship"/>: 
					<s:select name="relationId" list="relationList" listKey="id" listValue="value" headerKey="" headerValue="" />
				</td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr><td>
				<s:hidden name="idBenefitRecipient" />
				<s:hidden name="activeBenefitRecipient" />
				<s:hidden name="insertTimestampBenefitRecipient" />
				<input type="button" name="cancel" value="<s:text name="button.cancel"/>" class="button" onclick="return onCloseBenefitRecipient();" >
				<input type="button" name="save" value="<s:text name="button.save"/>" class="button" onclick="saveBenefitRecipient();" >
			</td></tr>
		</table>
		</form>
	</div>
</div>  

</body>
</html>