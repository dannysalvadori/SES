<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core"%>
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
		    $('#salesExchange').DataTable( {
				"iDisplayLength": 5,
				"lengthMenu": [5, 10, 20, 50],
				"language": {
					"emptyTable": "You do not own any shares"
		    	}
		    } );
		} );
	</script>
	<title>Demo Index Page</title>
</head>
<body>	
	<jsp:include page="../nav.jsp"/>
	
	<br/>
	<div class="container">
		<h1>My Account</h1>
		<h2>User Details</h2>
		
		<div class="panel panel-info">
			<div class="panel-heading">
				<h3 class="panel-title">Account Information</h3>
			</div>
			<div class="panel-body">
				<table width="100%">
					<tr>
						<td class="text-right">Email Address:&nbsp;</td>	<td>${user.email}</td>
						<td class="text-right">Credit:&nbsp;</td>			<td><fmt:formatNumber value="${user.credit}" type="currency"/></td>
					</tr>
					<tr>
						<td class="text-right">Surname:&nbsp;</td>			<td>${user.lastName}</td>
						<td class="text-right">First Name:&nbsp;</td>		<td>${user.name}</td>
					</tr>
					<tr>
						<td class="text-right">Birth date:&nbsp;</td>		<td><fmt:formatDate value="${user.birthDate}" pattern="dd/MM/yyyy"/></td>
					</tr>
					<tr>
						<td colspan=3 class="text-right"><a href="../user/goToChangePassword"><button type="button" class="btn-sm btn-primary">
								Change Password</button></a></td>
						<td colspan=3 class="text-right"><a href="../user/goToEditDetails"><button type="button" class="btn-sm btn-success">
								Edit Details</button></a></td>
					</tr>
				</table>
			</div>
		</div>
		
		<div class="panel panel-info">
			<div class="panel-heading">
				<h3 class="panel-title">Your Stocks</h3>
			</div>
			<div class="panel-body">
				<table id="salesExchange">
					<thead>
						<tr class="bg-success text-white">
							<th>Symbol</th>
							<th>Name</th>
							<th>Amount Owned</th>
							<th>Avg. Purchase Price</th>
							<th>Current Market Value</th>
							<th>Total Current Value.</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${saleForm.ownedShares}" var="ownedStock" varStatus="cStatus">
							<tr>
								<td>
									${ownedStock.company.symbol}
								</td>
		
								<td>
									${ownedStock.company.name}	
								</td>
		
								<td class="text-right">
									<fmt:formatNumber value="${ownedStock.quantity}" type="number"/>
								</td>
		
								<td class="text-right">
									<fmt:formatNumber value="${ownedStock.averagePurchasePrice}" type="currency" />
								</td>
		
								<td class="text-right">
									<fmt:formatNumber value="${ownedStock.company.currentShareValue}" type="currency"/>
								</td>
		
								<td class="text-right">
									<fmt:formatNumber value="${ownedStock.company.currentShareValue * ownedStock.quantity}" type="currency"/>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<br/>
				<a href="../user/stockExchange"><button type="button" class="float-right btn-sm btn-primary">Visit the Stock Exchange...</button></a>
			</div>
		</div>
		
		<div class="panel panel-info">
			<div class="panel-heading">
				<h3 class="panel-title">Transaction History</h3>
			</div>
			<div class="panel-body">
				Render your transactions here...	
			</div>
		</div>
		
	</div>
	
</body>
</html>