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
		<h1>Confirm Payment Details</h1>
		<br/>
		
		<div class="alert alert-info">
			You must confirm your credit card details to complete your transaction. <strong>You will not be charged.</strong>  
		</div>
		
		<h2>Card Details</h2>
		<br/>
		<div class="alert alert-info">
			TODO -- credit card stuff :)  
		</div>
		
		<br/>
		
		<a href="../user/stockExchange"><button type="button" class="btn-sm btn-danger">Cancel</button></a>
		&nbsp;
		<a href="../user/doPurchase"><button type="button" class="btn-sm btn-warning">Place Order</button></a>
		<br/>
			
	</div>
	
</body>
</html>