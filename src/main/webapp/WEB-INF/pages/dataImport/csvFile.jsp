<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<h2>Struts2 File Upload - Save Example</h2>
<s:actionerror />
<s:fielderror />

<s:form action="csvFile" namespace="/data" method="post" enctype="multipart/form-data" id="fileUploadForm">
	<s:file name="csvFile" label="CSV File" />
	<br /><br />
	<s:submit value="Upload" cssClass="button" />
</s:form>
	

	
</body>
</html>