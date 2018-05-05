<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Sign Up to StockSim</title>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
</head>
<body>
	<jsp:include page="nav.jsp"/>
	
	<br/>
	<div class="container">
		<h1>Sign Up</h1>
		<c:if test="${success}">
			<div class="alert alert-success">
					Success!
				</div>
		</c:if>
		
		<!-- TODO: This is evidently a poor way of delivering page messages... -->
		<c:forTokens items="${failures}" delims="|" var="failure">
			<div class="alert alert-danger" id="asp-error">
				<c:out value="${failure}"/>
			</div>
		</c:forTokens>
		
		<form:form action="registerUser" method="POST" modelAttribute="newUser" class="col-md-6 col-md-offset-3">
			<label for="email">Email Address:</label>
			<form:input id="email" path="email" type="email" placeholder="email" required="true" autofocus="true" class="form-control"/>
			<label for="password">Password</label>
			<form:input id="password" path="password" placeholder="password" type="password" required="true" class="form-control"/>
			<label for="confirmPW">Password</label>
			<form:input id="confirmPW" path="confirmationPassword" placeholder="confirm your password" type="password" required="true" class="form-control"/>
			<label for="name">First Name</label>
			<form:input id="name" path="name" placeholder="first name" required="true" class="form-control"/>
			<label for="lastName">Last Name</label>
			<form:input id="lastName" path="lastName" placeholder="last name" required="true" class="form-control"/>
			<br/>
			<form:hidden path="active" value="1" required="true"/>
			<input type="submit" value="Register" class="btn btn-primary"/>
		</form:form>
	</div>
	
</body>
</html>