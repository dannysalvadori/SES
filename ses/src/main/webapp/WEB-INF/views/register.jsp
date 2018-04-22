<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Sign Up to StockSim</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/ses_core.css"/>
</head>
<body>
	<c:if test="${success}">
		Success!
	</c:if>
	<br/>
	<a href="/"><input type="button" value="home"/></a>
	<br/>
	<form:form action="createUser" method="POST" modelAttribute="newUser">
		<table>
			<tr>
				<td>Email</td>
				<td><form:input path="email" type="email" placeholder="email" required="true" autofocus="true"/></td>
			</tr>
			<tr>
				<td>Password</td>
				<td><form:input path="password" placeholder="password" type="password" min="6" max="50" required="true"/></td>
			</tr>
			<tr>
				<td>First Name</td>
				<td><form:input path="name" placeholder="first name" required="true"/></td>
			</tr>			
			<tr>
				<td>Last Name</td>
				<td><form:input path="lastName" placeholder="last name" required="true"/></td>
			</tr>
			<tr>
				<td>Active?</td>
				<td><form:input path="active" type="number" max="1" required="true"/></td>
			</tr>
			<tr>
			    <td></td>
				<td><input type="submit" value="Register"/></td>
			</tr>
		</table>
	</form:form>
</body>
</html>