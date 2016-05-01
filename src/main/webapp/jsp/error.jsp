<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>UVIS Error</title>
	
		<!--  THEME -->
		
		<!-- Page-specific script -->

	</head>

	<body>

A Severe Error Has Occurred
<br />
<br />
<%
Object status_code = request.getAttribute("javax.servlet.error.status_code");
Object message = request.getAttribute("javax.servlet.error.message");
Object error_type = request.getAttribute("javax.servlet.error.exception_type");
Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
Object request_uri = request.getAttribute("javax.servlet.error.request_uri");
java.util.Date d = new java.util.Date();
String s = "";
if(status_code != null){
out.println("<br><B>Status code:</B> " + status_code.toString());s="";
//request.setAttribute("javax.servlet.error.status_code", status_code);
}if(message != null){
out.println("<BR><B>Message</B>: " + message.toString());s="";
//request.setAttribute("javax.servlet.error.message", message);
}if(error_type != null){
out.println("<BR><B>Error type</B>: " + error_type.toString());s="";
//request.setAttribute("javax.servlet.error.exception_type", error_type);
}if(request_uri != null){
out.println("<BR><B>Request URI</B>: " + request_uri.toString());s="";
//request.setAttribute("javax.servlet.error.request_uri", request_uri);
}
out.println("<BR><BR><B>Time of error</B>: "+d);
out.println("<BR><BR><B>Memory Used</B>: "+ ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024) + "K");
out.println("<BR><BR><B>Memory Free</B>: "+ (Runtime.getRuntime().freeMemory() + " (" + (Runtime.getRuntime().freeMemory() / 1024) + "K)"));
out.println("<BR><BR><B>Memory Allowable</B>: "+ (Runtime.getRuntime().maxMemory() / 1024) + "K");
out.println(s);
out.println("<HR><BR>");

%>

						<br />
						<br />

	</body>

</html>
