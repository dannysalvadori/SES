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
	<jsp:include page="nav.jsp"/>
	
	<br/>
	<div class="container">
		<h1>Welcome to stockSim</h1>
		<br/>
		<a href="hello"><button type="button" class="btn btn-primary">Say Hi</button></a>
		<br/><br/>
		<a href="jigvu"><button type="button" class="btn btn-primary">Bad Link?</button></a>
		<br/><br/>
		<a href="exception"><button type="button" class="btn btn-primary">Throw Exception</button></a>
		<br/><br/>
		<a href="admin"><button type="button" class="btn btn-primary">Go to Admin Home</button></a>
		<br/><br/>
		<a href="adminEdit"><button type="button" class="btn btn-primary">Go to Admin Edit</button></a>
	</div>
	
</body>
</html>