<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>stockSim - Sale Complete</title>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
</head>
<body>
	<jsp:include page="../nav.jsp"/>
	
	<div class="container">
		<h1>Sale Complete</h1>
		<br/>
		
		<div class="alert alert-success">
			Thank you. Your sale is now complete. You can view your transactions under
			 <a href="../user/myAccount">My Account</a> or make another transaction via the
			 <a href="../user/stockExchange">Stock Exchange</a>.
		</div>
			
		<a href="../user/stockExchange"><button type="button" class="btn-sm btn-primary">Return to Stock Exchange</button></a>
		&nbsp;
		<a href="../user/myAccount"><button type="button" class="btn-sm btn-primary">Go to Account</button></a>
		<br/>
			
	</div>
	
</body>
</html>