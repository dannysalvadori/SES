<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
	<title>Demo Index Page</title>
</head>
<body>	
	<jsp:include page="../nav.jsp"/>
	
	<br/>
	<div class="container">
		<h1>My Account</h1>
		<h2>User Details</h2>
		
		<table>
			<tr>
				<td class="text-right">Email Address:</td>	<td>${user.email}</td>
				<td class="text-right">Credit:</td>			<td>${user.credit}</td>
			</tr>
			<tr>
				<td class="text-right">Surname:</td>		<td>${user.lastName}</td>
				<td class="text-right">First Name:</td>		<td>${user.name}</td>
			</tr>
			<tr>
				<td class="text-right">Birthdate:</td>		<td><fmt:formatDate value="${user.birthDate}" pattern="dd/MM/yyyy"/></td>
			</tr>
			<tr>
				<td colspan=3 class="text-right"><a href="hello"><button type="button" class="btn-sm btn-primary">
						Edit Details</button></a></td>
				<td colspan=3 class="text-right"><a href="hello"><button type="button" class="btn-sm btn-primary">
						Change Password</button></a></td>
			</tr>
		</table>
		
	</div>
	
</body>
</html>