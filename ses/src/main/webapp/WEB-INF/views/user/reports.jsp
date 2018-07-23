<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
	<title>stockSim - Reports</title>
</head>
<body>	
	<jsp:include page="../nav.jsp"/>
	
	<div class="container">
		<h1>Reports</h1>		
		<br/>
		<div class="panel panel-info">
			<div class="panel-heading">
				<h3 class="panel-title">How to Export Reports</h3>
			</div>
			<div class="panel-body">
				Here you can extract information about the stock exchange and your sales/purchases into downloadable
				 files. Use the form below to specify which information you want to report, and in which format.
			</div>
		</div>
		
		<div class="panel panel-info">
			<div class="panel-heading">
				<h3 class="panel-title">Customise your Report</h3>
			</div>
			<div class="panel-body">
				<a href="../user/doRequestReport"><button type="button" class="btn-sm btn-primary">Let's do it!</button></a>
			</div>
			<br/>
			<br/>
			
			<form:form action="../user/doRequestReport" method="POST" modelAttribute="reportForm">
				<br/>
				<br/>
				<table id="reportForm">
					<thead>
						<tr class="bg-primary text-white">
							<th>Type</th>
							<th>Format</th>
							<th>Other stuff</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>
								<form:select path="${reportForm.type}">
									<form:options items="${reportTypes}"/>
								</form:select>
							</td>
							<td>
								<form:select path="${reportForm.format}">
									<form:options items="${reportFormats}"/>
								</form:select>									
							</td>
							<td>
								Stuff :)
							</td>
						</tr>
					</tbody>
				</table>
				<br/>
				<input type="submit" value="Generate Report" class="btn-sm btn-primary float-right">
			</form:form>
			
		</div>
	</div>
	
</body>
</html>