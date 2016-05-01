<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>Login Page</title>

	</head>

	<body onload="document.forms[0].submit()">
<%if (request.getHeader("email") != null){%>
<form method="post" action='<%= response.encodeURL("j_security_check") %>' >
<input type="hidden" name="j_username" value="<%= request.getHeader("email").toLowerCase()%>"><br>
<input type="hidden" name="j_password" value="<%= request.getHeader("email").toLowerCase()%>"><br>
</form>
<%} else { %>
UMD is not running ...
<%} %>
	</body>

</html>