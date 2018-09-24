<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
	<title>stockSim - Error</title>
</head>
<body>
	
	<jsp:include page="nav.jsp"/>
		
	<div class="container">
		<h1>Oops - Something went wrong...</h1>
		<br/>
		<div class="panel panel-info">
			<div class="panel-body">
				<c:choose>
					<c:when test="${error=='Not Found'}">
						The page you were looking for is unavailable. Did you type the wrong URL?
					</c:when>
					<c:otherwise>
						Something went wrong with the site. Please try again.
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
	
	<div class="container">
		<div class="panel panel-info">
			<div class="panel-body">
				<table class="table">
					<tr>
						<td>Date</td>
						<td>${timestamp}</td>
					</tr>
					<tr>
						<td>Error</td>
						<td>${error}</td>
					</tr>
					<tr>
						<td>Status</td>
						<td>${status}</td>
					</tr>
					<tr>
						<td>Message</td>
						<td>${message}</td>
					</tr>
					<tr>
						<td>Exception</td>
						<td>${exception}</td>
					</tr>
					<tr>
						<td>Trace</td>
						<td>
							<pre>${trace}</pre>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>
	
</body>
</html>