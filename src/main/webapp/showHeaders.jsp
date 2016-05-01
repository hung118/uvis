<%@page contentType="text/html; charset=UTF8" 
	import="java.io.*,javax.servlet.*,javax.servlet.http.*,java.util.*" 
	isErrorPage="false"%>
<html>
<head>
<title>State Phone Directory</title>
</head>
<body>

<%    
	response.setContentType("text/html");
    PrintWriter output = response.getWriter();
    String title = "Showing Request Headers";
    
    output.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">" +
    			"\n<HTML>\n<HEAD><TITLE>" + title + "</TITLE></HEAD>\n" +
                "<BODY BGCOLOR=\"#FDF5E6\">\n" +
                "<H1 ALIGN=CENTER>" + title + "</H1>\n" +
                "<B>Request Method: </B>" +
                request.getMethod() + "<BR>\n" +
                "<B>Request URI: </B>" +
                request.getRequestURI() + "<BR>\n" +
                "<B>Request Protocol: </B>" +
                request.getProtocol() + "<BR><BR>\n" +
                "<TABLE BORDER=1 ALIGN=CENTER>\n" +
                "<TR BGCOLOR=\"#FFAD00\">\n" +
                "<TH>Header Name<TH>Header Value");
    Enumeration headerNames = request.getHeaderNames();
    while(headerNames.hasMoreElements()) {
      String headerName = (String)headerNames.nextElement();
      output.println("<TR><TD>" + headerName);
      output.println("    <TD>" + request.getHeader(headerName));
    }
    output.println("</TABLE>\n</BODY></HTML>");
%>

<br/><br/><br/>

</body>
</html>
