<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>

 <H3>Fill the form: </H3>
<body>
<form id="registrationForm" method="get" action="<%= request.getContextPath() %>/customer">
<input type="hidden" name="customerUUID" value="${customerInfo.uuid}" />
<table>
  <tr>
    <td>First Name:</td>
        <td><input type="text" id="firstName" name="firstName" value=" " /></td>
  </tr>
    <tr>
        <td>Last Name:</td>
        <td><input type="text" name="lastName" value=" " required /></td>
    </tr>
    
    <tr>
        <td>Email:</td>
        <td><input type="text" name="email" value=" "  required /></td>
    </tr>
    
    <tr>
        <td>UserName:</td>
        <td><input type="text" name="userName" value=" " required /></td>
    </tr>
    <tr>
        <td>Password:</td>
        <td><input type="password" name="password" value=" " required /></td>
    </tr>
    
    <tr>
      <td></td>
      <td align="right">
          <input type="submit" name = "action" value="add" />
      </td>
    </tr>  
</table>
</form>

 <H3>Enter consumer email: </H3>
<form method="get" action="<%= request.getContextPath() %>/customer">
<input type="text" name="cust_email" value=" "   />
 <input type="submit" name = "action" value="get_by_email" />
  </form>
  
 <H3>Get all the customers: </H3>
<form method="get" action="<%= request.getContextPath() %>/customer">
 <input type="submit" name = "action" value="get_all" />
  </form>
</body>
</html>

