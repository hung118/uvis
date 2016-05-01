<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>VIS Dashboard</title>

<link rel="stylesheet" type="text/css" href="<s:url value='/css/displaytagLib.css'/>" />

<script type="text/javascript">
YAHOO.namespace("vts.container");

function init(){

if (!YAHOO.vts.container.wait) {

            // Initialize the temporary Panel to display while waiting for external content to load

            YAHOO.vts.container.wait = 
                    new YAHOO.widget.Panel("wait",  
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
}

YAHOO.util.Event.onDOMReady(init);

function gotoDashboard() {
	YAHOO.vts.container.wait.show();
	
	var link = '<s:url namespace="/" action="Dashboard" />';
	window.location.href = link;
}

</script>

</head>
<body>

	<h1>Help us identify Utah's Veterans!</h1>

<logic:present role="Read Only">
	Welcome ${user.firstName} ${user.lastName}. <br /><br />
	<p>	
	You can only view this information. If you need to change or add information on this site, please contact an Administrator.
	</p>
</logic:present>

<logic:present role="VSO">
	Welcome ${user.firstName} ${user.lastName}. <br /><br />
	This is for users whose role is VSO (Veterans Service Organization). It allows searching for single SSNs, and returns only 
	that veteran's record or none. The record can be viewed and printed as a PDF.
</logic:present>

<logic:present role="Admin,Super User,Data Entry">
	Welcome ${user.firstName} ${user.lastName}. <br /><br />
	<p>
	<s:if test="totalUnreviewedRecords > 0">
		There are <font color="red"><s:property value="totalUnreviewedRecords"/></font> unreviewed veteran records to resolve.
	</s:if>
	<s:else>
		There is 0 unreviewed veteran record to resolve.
	</s:else>
	
	<br><br>
	To view and edit/compare unreviewed veteran records, click <a href="javascript:gotoDashboard()">Dashboard</a> link on the tab menu.
	</p>
</logic:present>

</body>
</html>