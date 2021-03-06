<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>stockSim - Edit Details</title>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
</head>
<body>
	<jsp:include page="../nav.jsp"/>
	
	<div class="container">
		<h1>Edit Details</h1>
		<br/>
		
		<a href="../user/myAccount"><button type="button" class="btn-sm btn-danger">Cancel</button></a>
		<br/>
		<br/>
		
		<c:forTokens items="${failures}" delims="|" var="failure">
			<div class="alert alert-danger" id="asp-error">
				<c:out value="${failure}"/>
			</div>
		</c:forTokens>
		
		<form:form action="../user/doEditDetails" method="POST" modelAttribute="user" class="col-md-6 col-md-offset-3">
			<label for="email">Email Address <span style="color:red">(Requires verification)</span></label>
			<form:input id="email" path="email" type="email" placeholder="${user.email}" class="form-control"/>
			
			<label for="name">First Name</label>
			<form:input id="name" path="name" placeholder="first name" required="true" class="form-control"/>
			
			<label for="lastName">Last Name</label>
			<form:input id="lastName" path="lastName" placeholder="last name" required="true" class="form-control"/>
			
			<label for="birthDate">Birthdate (optional)</label>
			<form:input id="birthDate" path="birthDate" type="date" placeholder="${user.birthDate}"
				min="1900-01-01" max="2018-01-01" class="form-control"/>
			
			<!-- Non-update fields must still be in form, or will be updated to NULL -->
			<form:hidden path="id"/>
			<form:hidden path="active"/>
			<form:hidden path="password"/>
			<form:hidden path="confirmationPassword"/>
			<form:hidden path="credit"/>
			<form:hidden path="roles"/>
			
			<br/>
			<input type="submit" value="Save" class="btn btn-primary"/>
		</form:form>
	</div>
	
</body>
</html>