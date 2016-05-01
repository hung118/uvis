<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="decorator"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><decorator:title default="Veterans' Affairs - VIS" />
</title>

<!-- VTS css and javascript -->
<link rel="stylesheet" type="text/css"
	href="<s:url value='/css/vts.css'/>" />
<link rel="stylesheet" type="text/css"
	href="<s:url value='/css/vtsText.css'/>" />
<script type="text/javascript"
	src="<s:url value='/scripts/vtsIncludes.js'/>"></script>

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

<!-- Menu source file -->
<script type="text/javascript"
	src="<s:url value='/scripts/yui/build/menu/menu.js'/>"></script>
<link rel="stylesheet" type="text/css"
	href="<s:url value='/scripts/yui/build/menu/assets/skins/sam/menu.css'/>" />

<script type="text/javascript">
	/*
	   Initialize and render the MenuBar when its elements are ready
	   to be scripted.
	 */
	YAHOO.util.Event.onContentReady("mainmenu", function() {
		/*
		     Instantiate a MenuBar:  The first argument passed to the
		     constructor is the id of the element in the page
		     representing the MenuBar; the second is an object literal
		     of configuration properties.
		 */
		var oMenuBar = new YAHOO.widget.MenuBar("mainmenu", {
			autosubmenudisplay : true,
			hidedelay : 750,
			iframe : true,
			lazyload : true
		});
		/*
		     Call the "render" method with no arguments since the
		     markup for this MenuBar instance is already exists in
		     the page.
		 */

		oMenuBar.render();

		YAHOO.namespace("vts.container");
	});
</script>

<decorator:head />

</head>

<body class="yui-skin-sam">

	<div id="wrapper">

		<div id="branding"><a href="http://veterans.utah.gov" id="homelink">Utah Department of Veterans and Military Affairs Website</a></div>

		<%@ include file="/WEB-INF/decorators/menu.jsp"%>

		<decorator:body />

		<div class="clear"></div>
		
		<div id="theAddress">
			<div class="center"><script type="text/javascript">copyRight();</script></div>
			<div class="center"><s:include value="/version.jsp" /></div>
		</div>	
	</div>

</body>
</html>