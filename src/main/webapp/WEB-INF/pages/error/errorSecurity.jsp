<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>Security Error Page</title>

<script type="text/javascript">
	function toggleDiv(val) {
		if (val == 'show') {
			document.getElementById('show').style.display = 'block';
			document.getElementById('hide').style.display = 'none';
		} else {
			document.getElementById('show').style.display = 'none';
			document.getElementById('hide').style.display = 'block';			
		}
	}

</script>

</head>
<body>

	<br />
	<p>There has been a security error. Please contact <a href="http://veterans.utah.gov/">VA website</a> for assistance.</p>
	<font color="red">Error Message:</font>
	<s:property value="exception" /> <br /><br />
	<font color="red">Technical Details: </font><br>
	
	<span id='hide' style='display:block'>
		<a href="#" onclick="toggleDiv('show')">Show</a>
	</span>
	
	<span id='show' style="display:none;">
		<a href="#" onclick="toggleDiv('hide')">Hide</a><br /><br />
		<s:property value="exceptionStack" /> 
	</span>
	<br /><br />

</body>
</html>
