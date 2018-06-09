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
		    $('#stockExchange').DataTable();
		} );
	</script>
	<title>stockSim - Stock Exchange</title>
</head>
<body>
	<jsp:include page="../nav.jsp"/>

	<div class="container">
		<h1>Stock Exchange</h1>
		<br/>
		
		<h2>Purchase Stocks</h2>
		<br/>
		
		<c:forTokens items="${failures}" delims="|" var="failure">
			<div class="alert alert-danger" id="asp-error">
				<c:out value="${failure}"/>
			</div>
		</c:forTokens>
		
		<form:form action="../user/doPlaceOrder" method="POST" modelAttribute="transactionForm">
			<table id="stockExchange">
				<thead>
					<tr>
						<th></th>
						<th>Symbol</th>
						<th>Name</th>
						<th>Stocks Available</th>
						<th>Current Value</th>
						<th>Qty.</th>
						<th class="text-center">Buy</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${transactionForm.companies}" var="company" varStatus="cStatus">
						<tr>
							<td>
								<form:checkbox path="companies[${cStatus.index}].selected"/>
								<form:hidden path="companies[${cStatus.index}].Id"/>
							</td>
	
							<td>
								${company.symbol}
								<form:hidden path="companies[${cStatus.index}].Symbol"/>
							</td>
	
							<td>
								${company.name}
								<form:hidden path="companies[${cStatus.index}].Name"/>	
							</td>
	
							<td>
								<fmt:formatNumber value="${company.availableShares}" type="number" />
							</td>
	
							<td>
								<fmt:formatNumber value="${company.currentShareValue}" type="currency"/>
								<form:hidden path="companies[${cStatus.index}].currentShareValue"/>
							</td>
	
							<td>
								<form:input path="companies[${cStatus.index}].transactionQuantity" type="number" min="1"/>
								<form:hidden path="companies[${cStatus.index}].transactionQuantity"/>
							</td>
	
							<td><input type="submit" value="Buy" class="btn-sm btn-primary"/>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<br/>
			<input type="submit" value="Buy Selected" class="btn-sm btn-primary">
		</form:form>
		
	</div>
</body>
</html>