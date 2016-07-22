<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>List All Customers</title>
</head>
<body>
<b> Result: ${requestScope.success}</b>
<p></p> 
<c:choose>
<c:when  test="${not empty requestScope.customers}">
<c:forEach items="${requestScope.customers}" var="cust">
 			Customer in the list: ${cust.firstName}<br>
 		 	</c:forEach>
 		</c:when>
 		</c:choose>
<c:choose>
<c:when  test="${not empty requestScope.cust}">
 			You are searching for: ${requestScope.cust.firstName}<br>
 		</c:when>
 			</c:choose>
 <c:choose>
<c:when  test="${not empty requestScope.uuid}">
 			ID: ${requestScope.uuid}<br>
 		</c:when>
 			</c:choose>
</body>
</html>