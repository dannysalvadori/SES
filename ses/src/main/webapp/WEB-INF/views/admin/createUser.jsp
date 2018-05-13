<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>stockSim Admin - Create New Company</title>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
</head>
<body>
	<jsp:include page="../nav.jsp"/>
	
	<div class="container">
		<h1>Create New User</h1>
		
		<c:forTokens items="${failures}" delims="|" var="failure">
			<div class="alert alert-danger" id="asp-error">
				<c:out value="${failure}"/>
			</div>
		</c:forTokens>
		
		<form:form action="../admin/doCreateUser" method="POST" modelAttribute="newUser" class="col-md-6 col-md-offset-3">
			<label for="email">Email Address (Requires verification)</label>
			<form:input id="email" path="email" type="email" placeholder="john.smith@example.com" class="form-control"/>
			
			<label for="password">Password</label>
			<form:input id="password" path="password" placeholder="********" type="password" class="form-control"/>
			
			<label for="confirmPW">Confirm Password</label>
			<form:input id="confirmPW" path="confirmationPassword" placeholder="********" type="password" class="form-control"/>
			
			<label for="name">First Name</label>
			<form:input id="name" path="name" placeholder="first name" required="true" class="form-control"/>
			
			<label for="lastName">Last Name</label>
			<form:input id="lastName" path="lastName" placeholder="last name" required="true" class="form-control"/>
			
			<label for="birthDate">Birthdate (optional)</label>
			<form:input id="birthDate" path="birthDate" type="date" class="form-control"/>
			
			<br/>
			<input type="submit" value="Save New" class="btn btn-primary"/>
		</form:form>
	</div>
	
</body>
</html>