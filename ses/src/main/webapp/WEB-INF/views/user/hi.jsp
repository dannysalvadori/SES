<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Demo Index Page</title>
</head>
<body>
	<jsp:include page="../nav.jsp"/>
	Hi hi :D
	<br/>
	User count: ${userCount}
	<br/>
	Role count: ${roleCount}
	<br/>
	<a href="/"><input type="button" value="home"/></a>
</body>
</html>