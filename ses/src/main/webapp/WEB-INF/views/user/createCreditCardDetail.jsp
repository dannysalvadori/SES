<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri = "http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>stockSim - Add Credit Card Details</title>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
</head>
<body>
	<jsp:include page="../nav.jsp"/>
	
	<div class="container">
		<h1>Add Credit Card Details</h1>
		<br/>
		
		<a href="../user/myAccount"><button type="button" class="btn-sm btn-danger">Cancel</button></a>
		<br/>
		<br/>
		
		<c:forTokens items="${failures}" delims="|" var="failure">
			<div class="alert alert-danger" id="asp-error">
				<c:out value="${failure}"/>
			</div>
		</c:forTokens>
		
		<form:form action="../user/doCreateCreditCardDetail" method="POST" modelAttribute="newCreditCardDetail" 
				class="col-md-6 col-md-offset-3">
				
			<label for="cardHolderName">Card Holder Name (as written on card)</label>
			<form:input id="cardHolderName" path="cardHolderName" placeholder="Ms Jane Doe" class="form-control"/>
			
			<label for="cardNumber">Card Number</label>
			<form:input id="cardNumber" path="cardNumber" placeholder="XXXX XXXX XXXX XXXX" class="form-control"/>
			
			<label for="expiryDate">Expiry Date</label>
			<form:input id="expiryDate" path="expiryDate" type="date" class="form-control"/>
			
			<br/>
			<input type="submit" value="Save New" class="btn btn-primary"/>
		</form:form>
	</div>
	
</body>
</html>