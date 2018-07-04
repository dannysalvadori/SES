<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.css">
	<script type="text/javascript" charset="utf8" src="https://code.jquery.com/jquery-1.12.4.js"></script>
	<script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.js"></script>
	<script>
		$(document).ready( function () {
		    $('#myTable').DataTable();
		} );
	</script>
	<title>stockSim Admin - Manage Companies</title>
</head>
<body>
	<jsp:include page="../nav.jsp"/>

	<div class="container">
		<h1>Manage Companies</h1>
		<br/>
		
		<a href="../admin/adminHome"><button type="button" class="btn-sm btn-danger">Admin Home</button></a>
		&nbsp;
		<a href="../admin/createCompany"><button type="button" class="btn-sm btn-primary">Add New Company...</button></a>
		<br/>
		<br/>
		
		<table id="myTable">
			<thead>
				<tr>
					<th>Symbol</th>
					<th>Name</th>
					<th>Stocks Available</th>
					<th>Current Value</th>
					<th class="text-center">Edit</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${companies}" var="company">
					<tr>
						<td>${company.symbol}</td>
						<td>${company.name}</td>
						<td class="text-right"><fmt:formatNumber value="${company.availableShares}" type="number" /></td>
						<td class="text-right"><fmt:formatNumber value="${company.currentShareValue}" type="currency" /></td>
						<td class="text-center">
							<a href="../admin/editCompany?cid=${company.id}">
								<button type="button" class="btn-sm btn-primary">Edit</button>
							</a>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
	</div>
</body>
</html>