<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
	<title>stockSim - Log In</title>
</head>
<body>
	<jsp:include page="nav.jsp"/>
	
	<br/>
	<div class="container">
		<h1>Login</h1>
		<c:if test="${successfulRegistration}">
			<div class="alert alert-success">
				Registration success! Please sign in.
			</div>
		</c:if>
		<form name="loginForm" action="/login" method="POST" class="col-md-6 col-md-offset-3">
	        <label for="username">User Name:</label>
	        <input id="username" name="username" type="text" class="form-control"/>
	        <label for="password">Password:</label>
	        <input id="password" name="password" type="password" class="form-control"/>
	        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> <!--CSRF token for security (?)-->
			<c:if test="${not empty error}">
				<br/>
				<div class="alert alert-danger" id="asp-error">
					${error}
				</div>
			</c:if>
	        <br/>
	        <input type="submit" value="Log In" class="btn btn-primary"/>
	        <p><a href="/">Forgotten password?</a></p>
	    </form>
	</div>
    
</body>

</html>