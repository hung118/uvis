<!-- Local host login.jsp -->
<html>

<head>
<META HTTP-EQUIV="Pragma" CONTENT="no-cache"> 
<title>Login Page</title>
</head>

<!-- Change to your email address in lower case for username/password. Make sure at least one row for this email
is inserted in user_role table. User info will be retrieved from SessionInitAction.java.
 -->
<body onload="document.forms[0].submit();">
<form method="POST" action='<%= response.encodeURL("j_security_check") %>' >
  <table>
    <tr>
      <th align="right">Username:</th>
      <td align="left"><input type="text" name="j_username" value="hnguyen@utah.gov"></td>
    </tr>
    <tr>
      <th align="right">Password:</th>
      <td align="left"><input type="password" name="j_password" value="hnguyen@utah.gov"></td>
    </tr>
    <tr>
      <td align="right"><input type="submit" value="Log In"></td>
      <td align="left"><input type="reset"></td>
    </tr>
  </table>
</form>
</body>
</html>
