<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VIS <s:text name="report.zipMailingList" /></title>	

</head>
<body>
	
	<h1><s:text name="report.zipMailingList" /></h1>
	<p>Enter zip codes separated by spaces. For example: 84114 84107 84054</p>
	<s:actionerror/>
	<s:form action="generateZipMailingListReport">
		<table width="450" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td width="25%"><s:text name="report.zipCodes" />:</td>
				<td width="75%"><s:textarea name="zipCodes" rows="4" cols="40"/></td>
			</tr>

			<tr><td colspan="2">&nbsp;</td></tr>
			<tr><td colspan="2">
				<input type="button" name="cancel" value="<s:text name="button.cancel"/>" class="button" onclick="history.back();" >
				<s:reset name="reset" value="%{getText('button.reset')}" cssClass="button"/>
				<s:submit name="generateML" value="Generate Mailing List" cssClass="button" />
			</td></tr>	
		</table>
	</s:form>
		    
</body>
</html>