<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
	<title>stockSim - Index</title>
</head>
<body>
	<jsp:include page="nav.jsp"/>
	
	<div class="container">
		<h1>Welcome to stockSim</h1>		
		<br/>
		<div class="panel panel-info">
			<div class="panel-heading">
				<h3 class="panel-title">Getting Started</h3>
			</div>
			<div class="panel-body">
				Use the panel at the top of the page to log in and navigate the site. If you don't yet have an account,
				 you can register via the link above, or by clicking <a href="/register">here</a>.
			</div>
		</div>
		
		<c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN')==true}">
		<div class="panel panel-info">
			<div class="panel-heading">
				<h3 class="panel-title">Dev Test Panel</h3>
			</div>
			<div class="panel-body">
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
		</div>
		</c:if>
		
	</div>
	
</body>
</html>