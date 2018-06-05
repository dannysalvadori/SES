<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>stockSim - Purchase Stocks</title>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
</head>
<body>
	<jsp:include page="../nav.jsp"/>
	
	<div class="container">
		<h1>Confirm Purchase</h1>
		<br/>
		
		<div class="alert alert-warning">
			Your purchase is not complete. Please review and confirm your order.  
		</div>
		
		<h2>Your Order</h2>
		<br/>
		<table class="table table-sm table-bordered">
			<thead>
				<tr class="table-primary">
					<th>Symbol</th>
					<th>Name</th>
					<th>Current Value</th>
					<th>Purchase Quantity</th>
					<th>Subtotal</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${purchaseCompanies}" var="company">
					<tr>
						<td><c:out value="${company.symbol}"/></td>
						<td><c:out value="${company.name}"/></td>
						<td><fmt:formatNumber value="${company.currentShareValue}" type="currency"/></td>
						<td><fmt:formatNumber value="${company.transactionQuantity}" type="number"/></td>
						<td>
							<fmt:formatNumber value="${company.transactionQuantity * company.currentShareValue}" type="currency"/>
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="4" class="text-right"><strong>Total:</strong></td>
					<td><strong><fmt:formatNumber value="${total}" type="currency"/></strong></td>
				</tr>
			</tbody>
		</table>
		
		<br/>
		
		<a href="../user/stockExchange"><button type="button" class="btn-sm btn-danger">Cancel</button></a>
		&nbsp;
		<a href="../user/authenticatePurchase"><button type="button" class="btn-sm btn-primary">Proceed to Payment</button></a>
		<br/>
			
	</div>
	
</body>
</html>