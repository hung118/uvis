<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VIS <s:text name="report.emailRandom" /></title>	

<!-- calendar files -->
<link rel="stylesheet" type="text/css" href="<s:url value='/calendar/jscal2.css'/>" />
<link rel="stylesheet" type="text/css" href="<s:url value='/calendar/border-radius.css'/>" />
<link rel="stylesheet" type="text/css" href="<s:url value='/calendar/steel/steel.css'/>" />
<script type="text/javascript" src="<s:url value='/calendar/jscal2.js'/>"></script>
<script type="text/javascript" src="<s:url value='/calendar/en.js'/>"></script>

</head>
<body>
	
	<h1><s:text name="report.emailRandom" /></h1>
	<p>List random veterans who have email addresses.</p>
	<s:actionerror/>
	<s:form action="generateEmailRandomReport">
		<table width="450" border="0" cellspacing="0" cellpadding="2">
			<tr><td colspan="2">
				<table class="multiBox">
					<tr>							
						<td><s:text name="recordType" />:</td>
						<td>
							<div id="checkBoxList">
								<s:checkboxlist name="sourceArray" list="recordTypeList" listKey="id" listValue="value"  theme="custom" />
							</div>
						</td>
					</tr>				
				</table>			
			</td></tr>
			<tr>
				<td width="30%"><font color=red>*</font><s:text name="report.fromDate" />:</td>
				<td width="70%"> 
					<s:date name="fromDate" format="MM/dd/yyyy" var="fromDate"/>
					<s:textfield name="fromDate" value="%{#fromDate}" cssClass="fieldset75" maxlength="10" id="fromDate"/>
					<img src="<s:url value='/calendar/calendar.gif'/>" alt="Click here to use popup calendar." name="f_trigger_a" width="20" height="14" id="f_trigger_a" style="cursor: pointer; border: 1px solid #6699CC;" title="Date selector MM/DD/YYYY"
		                onmouseover="this.style.background='#6699CC';" onmouseout="this.style.background=''" />
		            (MM/DD/YYYY)
				</td>
			</tr>
			<tr>
				<td><font color=red>*</font><s:text name="report.toDate" />: </td>
				<td> 
					<s:date name="toDate" format="MM/dd/yyyy" var="toDate"/>
					<s:textfield name="toDate" value="%{#toDate}" cssClass="fieldset75" maxlength="10" id="toDate"/>
					<img src="<s:url value='/calendar/calendar.gif'/>" alt="Click here to use popup calendar." name="f_trigger_b" width="20" height="14" id="f_trigger_b" style="cursor: pointer; border: 1px solid #6699CC;" title="Date selector MM/DD/YYYY"
		                onmouseover="this.style.background='#6699CC';" onmouseout="this.style.background=''" />
		            (MM/DD/YYYY)
				</td>
			</tr>
			<tr>
				<td><s:text name="report.randomCount" />:</td>
				<td><s:textfield name="randomCount" cssClass="fieldset100"/></td>
			</tr>

			<tr><td colspan="2">&nbsp;</td></tr>
			<tr><td colspan="2">
				<input type="button" name="cancel" value="<s:text name="button.cancel"/>" class="button" onclick="history.back();" >
				<s:reset name="reset" value="%{getText('button.reset')}" cssClass="button"/>
				<s:submit name="submit" value="%{getText('button.submit')}" cssClass="button" />
			</td></tr>	
		</table>
	</s:form>
		
    <script type="text/javascript">
    //<![CDATA[
	
	      var cal = Calendar.setup({
	          onSelect: function(cal) { cal.hide() }
	      });
	      cal.manageFields("f_trigger_a", "fromDate", "%m/%d/%Y");
	      cal.manageFields("f_trigger_b", "toDate", "%m/%d/%Y");

    //]]></script>
    
</body>
</html>