<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.css">
	<script type="text/javascript" charset="utf8" src="https://code.jquery.com/jquery-1.12.4.js"></script>
	<script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.js"></script>
	<script>
		$(document).ready( function () {
		    $('#myTable').DataTable();
		    var grid = {
				view:"datatable",
				columns:[
					{ id:"email",  header:[ "Email", { content:"numberFilter" } ], sort:"int" },    
				]
			};
		} );
	</script>
	<title>stockSim Admin - Manage Users</title>
</head>
<body>
	<jsp:include page="../nav.jsp"/>

	<div class="container">
		<h1>Manage Users</h1>
		<br/>
		
		<a href="../admin/createUser"><button type="button" class="btn-sm btn-primary">Add New User...</button></a>
		<br/>
		<br/>
		
		<table id="myTable" class="myTable ">
			<thead >
				<tr>
					<th>Email</th>
					<th class="text-center">Active?</th>
					<th>First Name</th>
					<th>Last Name</th>
					<th class="text-center">Admin?</th>
					<th class="text-center">Birthdate</th>
					<th class="text-center">Activate/Deactivate</th>
					<th class="text-center">Edit</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${users}" var="user">
					<tr>
						<td>${user.email}</td>
						<td class="text-center">
							<c:if test="${user.active==1}">
								<img alt="TRUE" width="20px"
									src="<%=request.getContextPath()%>/resources/img/tick.png"/>
							</c:if>
							<c:if test="${user.active==0}">
								<img alt="FALSE" width="20px"
									src="<%=request.getContextPath()%>/resources/img/cross.png"/>
							</c:if>
						</td>
						<td>${user.name}</td>
						<td>${user.lastName}</td>
						<td class="text-center"><c:forEach items="${user.roles}" var="role">
							<c:if test="${role.role=='ROLE_ADMIN'}">Yes</c:if>
						</c:forEach></td>
						<td><fmt:formatDate value="${user.birthDate}" pattern="dd MMM yyyy" /></td>
						<td class="text-center">
							<a href="../admin/toggleActive?uid=${user.id}">
								<button type="button" class="btn-sm btn-primary">Toggle</button>
							</a>
						</td>
						<td class="text-center">
							<a href="../admin/editUser?uid=${user.id}">
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