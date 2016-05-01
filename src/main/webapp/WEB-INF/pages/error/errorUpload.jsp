<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<title>File Upload Error Page</title>

</head>
<body>

	<br />		
	<p>The server has received an upload error. Please contact <a href="http://veterans.utah.gov/">VA website</a> for assistance.</p>
	<font color="red">Error Message:</font>

	<s:actionerror/>

</body>
</html>
