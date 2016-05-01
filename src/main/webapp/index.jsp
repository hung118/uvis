<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Refresh" content="0;URL=sessionInit.action?login_attempt">	

<title>UVIS Login</title>

<!-- YUI Dependency source files -->
<script type="text/javascript"
	src="<s:url value='/scripts/yui/build/yahoo-dom-event/yahoo-dom-event.js'/>"></script>

<link rel="stylesheet" type="text/css"
	href="<s:url value='/scripts/yui/build/container/assets/skins/sam/container.css'/>" />

<script type="text/javascript"
	src="<s:url value='/scripts/yui/build/utilities/utilities.js'/>"></script>

<script type="text/javascript"
	src="<s:url value='/scripts/yui/build/container/container-min.js'/>"></script>

<script type="text/javascript"
	src="<s:url value='/scripts/yui/build/container/container_core.js'/>"></script>
	
<script type="text/javascript">

	// To show wait message box. To call it just put YAHOO.vts.container.wait.show() line in js function
	YAHOO.namespace("vts.container");

	function init() {

		// Instantiate temporary Panel to display while waiting for external content to load.
		// To call it just put YAHOO.vts.container.wait.show() line in js function.
		if (!YAHOO.vts.container.wait) {
			YAHOO.vts.container.wait = new YAHOO.widget.Panel("wait", {
				width : "240px",
				fixedcenter : true,
				close : false,
				draggable : false,
				zindex : 4,
				modal : true,
				visible : true	// set false for other cases
			});

			YAHOO.vts.container.wait.setHeader("Loading UVIS, please wait...");
			YAHOO.vts.container.wait.setBody("<img src=\"<s:url value='/images/activity.gif'/>\"/>");
			YAHOO.vts.container.wait.render(document.body);
		}
	}
	
	YAHOO.util.Event.onDOMReady(init);
</script>

</head>

<body class="yui-skin-sam">

</body>
</html>


