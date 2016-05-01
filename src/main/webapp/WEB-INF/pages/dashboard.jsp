<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/WEB-INF/tlds/struts-logic.tld" prefix="logic" %>
<%@ taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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

function editCompareVeterans(id) {
	YAHOO.vts.container.wait.show();
	
	var url = '<s:url namespace="/" action="editCompareVeterans" />';
	var values = {'id' : id, 'fromDashboard' : 'true'};

	postToUrl(url, values);	
}

</script>

</head>
<body>

	<h1>Dashboard</h1>
	
	<s:if test="veterans.size() > 0">
		<p>There are <s:property value="totalUnreviewedRecords"/> unreviewed veteran records to resolve. Showing <s:property value="veterans.size()"/>.</p>
	</s:if>            	
	<s:else>
		<p>There is 0 unreviewed veteran record to resolve.</p>
	</s:else>
   
	<s:set name="pageSize" value="pageSize" />
	Page Size: <s:url id="pageSize10" namespace="/" action="setSessionPageSizeDashboard"><s:param name="pageSize" value="%{'10'}" /></s:url><s:a href="%{pageSize10}">10</s:a>
	<s:url id="pageSize25" namespace="/" action="setSessionPageSizeDashboard"><s:param name="pageSize" value="%{'25'}" /></s:url><s:a href="%{pageSize25}">25</s:a>
	<s:url id="pageSize50" namespace="/" action="setSessionPageSizeDashboard"><s:param name="pageSize" value="%{'50'}" /></s:url><s:a href="%{pageSize50}">50</s:a>
	<s:url id="pageSizeAll" namespace="/" action="setSessionPageSizeDashboard"><s:param name="pageSize" value="%{'all'}" /></s:url><s:a href="%{pageSizeAll}">All</s:a>
	<display:table name="veterans" defaultorder="ascending" export="false" 
		id="row" requestURI="pageDashboard.action" pagesize="${pageSize}" style="width: 100%;">
		<display:column sortable="true" title="SSN" property="ssn" />
		<display:column sortable="true" title="Last Name" property="lastName" />
		<display:column sortable="true" title="First Name" property="firstName" />
		<display:column sortable="true" title="# Sources" property="numberOfSources" style="text-align: right;" />
		<display:column title="Action" style="text-align: center;">
			<s:a href="javascript:editCompareVeterans('%{#attr.row.id}');" title="Edit/Compare"><img src="<s:url value='/images/compare-icon.png'/>"/></s:a>
		</display:column>			
	</display:table>
      
</body>
</html>