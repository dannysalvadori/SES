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
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
</head>
<body>
	<jsp:include page="../nav.jsp"/>
	
	<div class="container">
		<h1>Create Company</h1>
		<br/>
		
		<a href="../admin/manageCompanies"><button type="button" class="btn-sm btn-danger">Cancel</button></a>
		<br/>
		<br/>
		
		<c:forTokens items="${failures}" delims="|" var="failure">
			<div class="alert alert-danger" id="asp-error">
				<c:out value="${failure}"/>
			</div>
		</c:forTokens>
		
		<form:form action="../admin/doCreateCompany" method="POST" modelAttribute="company" class="col-md-6 col-md-offset-3">
			<label for="symbol">Symbol</label>
			<form:input id="symbol" path="symbol" placeholder="${company.symbol}" class="form-control" autofocus="true"/>
			
			<label for="name">Name</label>
			<form:input id="name" path="name" placeholder="${company.name}" class="form-control"/>
			
			<label for="availableShares">Available Shares</label>
			<form:input id="availableShares" path="availableShares" class="form-control"/>
			
			<label for="currentShareValue">Share Value</label>
			<form:input id="currentShareValue" path="currentShareValue" class="form-control"/>
			
			<br/>
			<input type="submit" value="Save New" class="btn btn-primary"/>
		</form:form>
	</div>
	
</body>
</html>