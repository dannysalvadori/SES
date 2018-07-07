<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
	<title>stockSim Admin - Home</title>
</head>
<body>
	<jsp:include page="../nav.jsp"></jsp:include>

	<div class="container">
		<h1>Admin Home</h1>
		<br/>
		<a href="../admin/manageUsers"><button type="button" class="btn btn-primary">Manage Users</button></a>
		<br/><br/>
		<a href="../admin/manageCompanies"><button type="button" class="btn btn-primary">Manage Companies</button></a>
	</div>
</body>
</html>