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
</head>
<body>
	<jsp:include page="../nav.jsp"/>
	
	<div class="container">
		<h1>Confirm Sale</h1>
		<br/>
		
		<div class="alert alert-danger">
			You are SELLING the selected stocks. You will be credited the amount detailed below. Please review and
			 confirm your sale. <strong>Sales are not reversible.</strong>  
		</div>
		
		<h2>Your Sale</h2>
		<br/>
			<form:form action="../user/doSale" method="POST" modelAttribute="saleForm">
			<table class="table table-sm table-bordered">
				<thead>
					<tr class="table-success">
						<th>Symbol</th>
						<th>Name</th>
						<th>Total Owned</th>
						<th>Your Avg. Purchase Price</th>
						<th>Current Value</th>
						<th>Sale Quantity</th>
						<th>Subtotal</th>
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
							
							<td class="text-right">
								<fmt:formatNumber value="${ownedStock.company.transactionQuantity}" type="number" />
								<form:hidden path="ownedShares[${cStatus.index}].company.transactionQuantity"/>
							</td>

							<td>
								<fmt:formatNumber type="currency"
									value="${ownedStock.company.transactionQuantity * ownedStock.company.currentShareValue}"/>
							</td>
						</tr>
					</c:forEach>
					<tr>
						<td colspan="6" class="text-right"><strong>Total:</strong></td>
						<td><strong><fmt:formatNumber value="${total}" type="currency"/></strong></td>
					</tr>
				</tbody>
			</table>
		
			<br/>
		
			<a href="../user/stockExchange"><button type="button" class="btn-sm btn-danger">Cancel</button></a>
			&nbsp;
			<input type="submit" value="Confirm Sale" class="btn-sm btn-success float-right"/>
		</form:form>
			
	</div>
	
</body>
</html>