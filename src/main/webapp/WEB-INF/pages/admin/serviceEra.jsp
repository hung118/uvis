<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VIS <s:text name="serviceEra" /></title>	

<link rel="stylesheet" type="text/css" href="<s:url value='/css/displaytagLib.css'/>" />

<!-- calendar files -->
<link rel="stylesheet" type="text/css" href="<s:url value='/calendar/jscal2.css'/>" />
<link rel="stylesheet" type="text/css" href="<s:url value='/calendar/border-radius.css'/>" />
<link rel="stylesheet" type="text/css" href="<s:url value='/calendar/steel/steel.css'/>" />
<script type="text/javascript" src="<s:url value='/calendar/jscal2.js'/>"></script>
<script type="text/javascript" src="<s:url value='/calendar/en.js'/>"></script>

<script type="text/javascript">

function onCancel() {
	window.location.href = '<s:url namespace="/" action="cancelServiceEra" />';
}

function onNew() {
	window.location.href = '<s:url namespace="/" action="displayServiceEra" />';
}

function onSave() {
	YAHOO.vts.container.wait.show();
	
	document.getElementById("serviceEraForm").action = "saveServiceEra.action";
	return true;	
}

function editServiceEra(id) {
	
	var link = '<s:url namespace="/" action="editServiceEra" />';
	link = link + "?id=" + id;
	window.location.href = link;
}

function deleteServiceEra(id) {
	
	if (confirm("Are you sure you want to delete this service era?")) {
		var link = '<s:url namespace="/" action="deleteServiceEra" />';
		link = link + "?id=" + id;
		window.location.href = link;		
	} 
}

</script>

</head>
<body>
	
	<h1><s:text name="serviceEra.title" /></h1>

	<s:form action="saveServiceEra" id="serviceEraForm">
		<s:actionerror/>
		<s:actionmessage/>
		<table width="450" border="0" cellspacing="0" cellpadding="2">
			<tr>
				<td><font color=red>*</font><s:text name="common.name"/>:</td>
				<td><s:textfield name="serviceEra.name" cssClass="fieldset200" maxlength="64" /></td>
			</tr>
			<tr>
				<td><font color=red>*</font><s:text name="serviceEra.startDate"/>:</td>
				<td>
					<s:textfield name="serviceEra.startDateStr" cssClass="fieldset95" id="startDateStr"/>
					<img src="<s:url value='/calendar/calendar.gif'/>" alt="Click here to use popup calendar." name="f_trigger_a" width="20" height="14" id="f_trigger_a" style="cursor: pointer; border: 1px solid #6699CC;" title="Date selector MM/DD/YYYY"
		                onmouseover="this.style.background='#6699CC';" onmouseout="this.style.background=''" />
				</td>
			</tr>
			<tr>
				<td><font color=red>*</font><s:text name="serviceEra.endDate"/>:</td>
				<td>
					<s:textfield name="serviceEra.endDateStr" cssClass="fieldset95" id="endDateStr" />
					<img src="<s:url value='/calendar/calendar.gif'/>" alt="Click here to use popup calendar." name="f_trigger_b" width="20" height="14" id="f_trigger_b" style="cursor: pointer; border: 1px solid #6699CC;" title="Date selector MM/DD/YYYY"
		                onmouseover="this.style.background='#6699CC';" onmouseout="this.style.background=''" />
				</td>
			</tr>
			<tr>
				<td><font color=red>*</font><s:text name="common.status"/>:</td>
				<td>
					<div id="radioButtonList">
						<s:radio name="serviceEra.active" list="activeList" listKey="active" listValue="value" />
					</div>
				</td>
			</tr>
			<tr><td colspan="2">&nbsp;</td></tr>
			<tr><td colspan="2">
				<s:hidden name="serviceEra.id" />
				<s:hidden name="serviceEra.insertTimestamp" />
				<s:hidden name="serviceEra.updateTimestamp" />
				<s:hidden name="serviceEra.op" />
				<input type="button" name="cancel" value="<s:text name="button.cancel"/>" class="button" onclick="return onCancel();" >
				<s:reset name="reset" value="%{getText('button.reset')}" cssClass="button"/>
				<input type="button" name="new" value="<s:text name="button.new"/>" class="button" onclick="return onNew();" />
				<s:if test="serviceEra.op == 'insert'">
					<s:submit name="save" value="%{getText('button.save')}" cssClass="button" onclick="return onSave();" />
				</s:if>
				<s:elseif test="serviceEra.op == 'edit'">
					<s:submit name="save" value="%{getText('button.update')}" cssClass="button" onclick="return onSave();" />
				</s:elseif>
				
			</td></tr>	
		</table>
	</s:form>
	
	<div class="displaytag">
		<display:table name="serviceEras" export="false" 
			id="row" requestURI="pageServiceEra.action">
			<display:column title="Name" property="name" />
			<display:column title="Start Date" property="startDate" decorator="gov.utah.dts.det.displaytag.decorator.DisplayDate" />
			<display:column title="End Date" property="endDate" decorator="gov.utah.dts.det.displaytag.decorator.DisplayDate" />
			<display:column title="Status" >
				<s:if test="%{#attr.row.active}">Active</s:if>
				<s:else>Inactive</s:else>
			</display:column>
			<display:column title="Action" style="text-align: center;">
				<s:a href="javascript:editServiceEra(%{#attr.row.id});" title="Edit"><img src="<s:url value='/images/edit-icon.png'/>"/></s:a>
				<s:a href="javascript:deleteServiceEra(%{#attr.row.id});" title="Delete"><img src="<s:url value='/images/delete-icon.png'/>"/></s:a>
			</display:column>
		</display:table>
	</div>		
		  
    <script type="text/javascript">
    //<![CDATA[
	
	      var cal = Calendar.setup({
	          onSelect: function(cal) { cal.hide() }
	      });
	      cal.manageFields("f_trigger_a", "startDateStr", "%m/%d/%Y");
	      cal.manageFields("f_trigger_b", "endDateStr", "%m/%d/%Y");

    //]]></script>
		    
</body>
</html>