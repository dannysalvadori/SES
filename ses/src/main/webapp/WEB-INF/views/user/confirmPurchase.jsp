<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>stockSim - Confirm Stock Purchase</title>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
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
			<form:form action="../user/authenticatePurchase" method="POST" modelAttribute="transactionForm">
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
							
							<td>
								<fmt:formatNumber value="${company.currentShareValue}" type="currency"/>
								<form:hidden path="companies[${cStatus.index}].currentShareValue"/>
							</td>

							<td>
								<fmt:formatNumber value="${company.transactionQuantity}" type="number"/>
								<form:hidden path="companies[${cStatus.index}].transactionQuantity"/>
								<%-- <form:input style="display:none;" path="${company.transactionQuantity}"/> --%>
							</td>
							
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
			<input type="submit" value="Proceed to Payment" class="btn-sm btn-primary"/>
		</form:form>
			
	</div>
	
</body>
</html>