<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title><decorator:title default="Veterans' Affairs - VIS" /></title>

	<!-- VTS css and javascript -->	
	<link rel="stylesheet" type="text/css" href="<s:url value='/css/vts.css'/>" />
	<link rel="stylesheet" type="text/css" href="<s:url value='/css/vtsText.css'/>" />			
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
	YAHOO.util.Event.onContentReady("mainmenu", 
		function () {
		   /*
		        Instantiate a MenuBar:  The first argument passed to the
		        constructor is the id of the element in the page
		        representing the MenuBar; the second is an object literal
		        of configuration properties.
		   */
		   var oMenuBar = new YAHOO.widget.MenuBar("mainmenu", {	        											
		                                               autosubmenudisplay: true,
		                                               hidedelay: 750,	
		                                               iframe: true,                                                    
		                                               lazyload: true });
		   /*
		        Call the "render" method with no arguments since the
		        markup for this MenuBar instance is already exists in
		        the page.
		   */
		
		   oMenuBar.render();
		   
		   YAHOO.namespace("vts.container");
		
		}
	);
	    
	/** To show wait message box. To call it just put YAHOO.vts.container.wait.show() line in js function */
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
	/** end of wait message box script. */
	
	</script>
	
	<decorator:head />
	
</head>

<body class="yui-skin-sam">
	
	<div id="wrapper">
		
		<div id="branding"><a href="http://veterans.utah.gov" id="homelink">Utah Department of Veterans and Military Affairs Website</a></div>

		<%-- 
		<%@ include file="/WEB-INF/decorators/no_menu.jsp"%>
		--%>

	    <div id="rotatingPhoto">
	    	<script type="text/javascript">rotatingPhoto(photo_array);</script>
	        <div class="clear"></div>
	    </div>

		<div id="mainContent">
		
			<decorator:body />
			
		</div>
		
		<div class="clear"></div>
	
		<div id="theAddress">
			<a href="http://veterans.utah.gov" class="logo"><img src="<s:url value='/images/vma_logo_notitle.png'/>" alt="Utah Veterans and Military Affairs Logo" /></a>
			
			<div class="col35 left">
				<p>Utah Department of Veterans and Military Affairs</p>

				<div class="col left last">
				   <p>550 Foothill Drive, Suite 202<br />
					  Salt Lake City, UT 84113</p>
				</div>

				<div class="col right">
				   <p>Phone: (801) 326-2372<br />
				   Toll Free: (800) 894-9497<br />
				   Fax: (801) 326-2369<br />
				   Email: <a href="mailto:veterans@utah.gov">veterans@utah.gov</a></p>
				</div>
				<div class="clearfix"></div>
            </div>
          
			<div class="col20 right">
			  <ul>	
				  <li><a href="http://www.utah.gov/main/index">Utah.gov&nbsp;Home</a></li>
				  <li><a href="http://www.utah.gov/disclaimer.html">Utah.gov&nbsp;Terms&nbsp;of&nbsp;Use</a></li>
				  <li><a href="http://www.utah.gov/privacypolicy.html">Utah.gov&nbsp;Privacy&nbsp;Policy</a></li>
				  <li><a href="http://www.utah.gov/accessibility.html">Utah.gov&nbsp;Accessibility&nbsp;Policy</a></li>
				  <li><a href="http://www.utah.gov/translate.html">Translate&nbsp;Utah.gov</a></li>
			  </ul>
			</div>

			<div class="clear"></div>
			<div class="center"><script type="text/javascript">copyRight();</script></div>
			<div class="center"><s:include value="/version.jsp" /></div>
		</div>	
	
	</div>
	
	

</body>
</html>