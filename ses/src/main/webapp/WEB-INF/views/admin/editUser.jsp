<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>stockSim Admin - Edit User</title>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
</head>
<body>
	<jsp:include page="../nav.jsp"/>
	
	<div class="container">
		<h1>Edit User: ${user.email}</h1>
		<br/>
		
		<a href="../admin/manageUsers"><button type="button" class="btn-sm btn-danger">Cancel</button></a>
		<br/>
		<br/>
		
		<c:forTokens items="${failures}" delims="|" var="failure">
			<div class="alert alert-danger" id="asp-error">
				<c:out value="${failure}"/>
			</div>
		</c:forTokens>
		
		<form:form action="../admin/doEditUser" method="POST" modelAttribute="user" class="col-md-6 col-md-offset-3">
			<label for="email">Email Address <span style="color:red">(Requires verification)</span></label>
			<form:input id="email" path="email" type="email" placeholder="${user.email}" class="form-control"/>
			
			<label for="credit">Credit</label>
			<form:input id="credit" path="credit" type="currency" placeholder="${user.credit}" class="form-control"/>
			
			<label for="password">Password</label>
			<form:input id="password" path="password" placeholder="********" type="password" class="form-control"/>
			
			<label for="confirmPW">Confirm Password <span style="color:grey">(Leave blank if not changing password)</span></label>
			<form:input id="confirmPW" path="confirmationPassword" placeholder="---" type="password" class="form-control"/>
			
			<label for="name">First Name</label>
			<form:input id="name" path="name" placeholder="first name" required="true" class="form-control"/>
			
			<label for="lastName">Last Name</label>
			<form:input id="lastName" path="lastName" placeholder="last name" required="true" class="form-control"/>
			
			<label for="birthDate">Birthdate (optional)</label>
			<form:input id="birthDate" path="birthDate" type="date" placeholder="${user.birthDate}"
				min="1900-01-01" max="2018-01-01" class="form-control"/>
			
			<label for="roles">Roles (ctrl+click to select multiple)</label>
			<br/>
			<form:select id="roles" multiple="true" path="roles">
				<form:options items="${allRoles}" itemValue="id" itemLabel="role"/>
			</form:select>
			
			<!-- Non-update fields must still be in form, or will be updated to NULL -->
			<form:hidden path="id"/>
			<form:hidden path="active"/>
			
			<br/>
			<input type="submit" value="Save" class="btn btn-primary"/>
		</form:form>
	</div>
	
</body>
</html>