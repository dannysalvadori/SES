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
		    $('#stockExchange').DataTable( {
				"iDisplayLength": 20,
				"lengthMenu": [5, 10, 20, 50],
				"language": {
					"emptyTable": "Shares are not available to purchase now"
		    	}
		    } );
		    $('#salesExchange').DataTable( {
				"iDisplayLength": 5,
				"lengthMenu": [5, 10, 20, 50],
				"language": {
					"emptyTable": "You do not own any shares"
		    	}
		    } );
		} );
	</script>
	<title>stockSim - Stock Exchange</title>
</head>
<body>
	<jsp:include page="../nav.jsp"/>

	<div class="container">
		<h1>Stock Exchange</h1>
		<br/>
		
		<!-- PURCHASES -->
		<h2>Purchase Stocks</h2>
		
		<c:forTokens items="${purchaseFailures}" delims="|" var="failure">
			<div class="alert alert-danger" id="asp-error">
				<c:out value="${failure}"/>
			</div>
		</c:forTokens>
		
		<form:form action="../user/doPlaceOrder" method="POST" modelAttribute="transactionForm">
			<input type="submit" value="BUY SELECTED STOCKS" class="btn-sm btn-primary float-right">
			<br/>
			<br/>
			<table id="stockExchange">
				<thead>
					<tr class="bg-primary text-white">
						<th>Symbol</th>
						<th>Name</th>
						<th>Stocks Available</th>
						<th>Current Value</th>
						<th>Qty.</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${transactionForm.companies}" var="company" varStatus="cStatus">
						<tr>
							<td>
								${company.symbol}
								<form:hidden path="companies[${cStatus.index}].Symbol"/>
							</td>
	
							<td>
								${company.name}
								<form:hidden path="companies[${cStatus.index}].Name"/>	
							</td>
	
							<td class="text-right">
								<fmt:formatNumber value="${company.availableShares}" type="number" />
							</td>
	
							<td class="text-right">
								<fmt:formatNumber value="${company.currentShareValue}" type="currency"/>
								<form:hidden path="companies[${cStatus.index}].currentShareValue"/>
							</td>
	
							<td>
								<form:input path="companies[${cStatus.index}].transactionQuantity" type="number" min="1"/>
								<form:hidden path="companies[${cStatus.index}].transactionQuantity"/>
							</td>
	
							<td>
								<form:checkbox path="companies[${cStatus.index}].selected"/>
								<form:hidden path="companies[${cStatus.index}].Id"/>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<br/>
			<input type="submit" value="BUY SELECTED STOCKS" class="btn-sm btn-primary float-right">
		</form:form>
		
		<br/>
		<br/>
		
		<!-- SALES -->
		<h2>Sell Stocks</h2>
		<br/>
		
		<c:forTokens items="${saleFailures}" delims="|" var="failure">
			<div class="alert alert-danger" id="asp-error">
				<c:out value="${failure}"/>
			</div>
		</c:forTokens>
		
		<form:form action="../user/sellSelected" method="POST" modelAttribute="saleForm">
			<table id="salesExchange">
				<thead>
					<tr class="bg-success text-white">
						<th>Symbol</th>
						<th>Name</th>
						<th>Amount Owned</th>
						<th>Avg. Purchase Price</th>
						<th>Current Market Value</th>
						<th>Qty.</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${saleForm.ownedShares}" var="ownedStock" varStatus="cStatus">
						<tr>
							<td>
								${ownedStock.company.symbol}
								<form:hidden path="ownedShares[${cStatus.index}].company.Symbol"/>
							</td>
	
							<td>
								${ownedStock.company.name}
								<form:hidden path="ownedShares[${cStatus.index}].company.Name"/>	
							</td>
	
							<td class="text-right">
								<fmt:formatNumber value="${ownedStock.quantity}" type="number" />
							</td>
	
							<td class="text-right">
								<fmt:formatNumber value="${ownedStock.averagePurchasePrice}" type="currency" />
							</td>
	
							<td class="text-right">
								<fmt:formatNumber value="${ownedStock.company.currentShareValue}" type="currency"/>
								<form:hidden path="ownedShares[${cStatus.index}].company.currentShareValue"/>
							</td>
	
							<td>
								<form:input path="ownedShares[${cStatus.index}].company.transactionQuantity" type="number" min="1"/>
								<form:hidden path="ownedShares[${cStatus.index}].company.transactionQuantity"/>
							</td>
							
							<td>
								<form:checkbox path="ownedShares[${cStatus.index}].selected"/>
								<form:hidden path="ownedShares[${cStatus.index}].Id"/>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			<br/>
			<input type="submit" value="SELL SELECTED STOCKS" class="btn-sm btn-success float-right">
		</form:form>
		
		<br/>
		
	</div>
</body>
</html>