<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>stockSim - Change Password</title>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
</head>
<body>
	<jsp:include page="../nav.jsp"/>
	
	<div class="container">
		<h1>Change Your Password</h1>
		<br/>
		
		<a href="../user/myAccount"><button type="button" class="btn-sm btn-danger">Cancel</button></a>
		<br/>
		<br/>
		
		<c:forTokens items="${failures}" delims="|" var="failure">
			<div class="alert alert-danger" id="asp-error">
				<c:out value="${failure}"/>
			</div>
		</c:forTokens>
		
		<form:form action="../user/doChangePassword" method="POST" modelAttribute="user" class="col-md-6 col-md-offset-3">
			<label for="password">New Password</label>
			<form:input id="password" path="password" placeholder="---" type="password" class="form-control"/>
			
			<label for="confirmPW">Confirm Password</label>
			<form:input id="confirmPW" path="confirmationPassword" placeholder="---" type="password" class="form-control"/>
			
			<!-- Non-update fields must still be in form, or will be updated to NULL -->
			<form:hidden path="id"/>
			<form:hidden path="active"/>
			<form:hidden path="email"/>
			<form:hidden path="credit"/>
			<form:hidden path="name"/>
			<form:hidden path="lastName"/>
			<form:hidden path="birthDate"/>
			<form:hidden path="roles"/>
			
			<br/>
			<input type="submit" value="Save" class="btn btn-primary"/>
		</form:form>
	</div>
	
</body>
</html>