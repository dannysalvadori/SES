<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Demo Index Page</title>
	<link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/ses_core.css"/>
</head>
<body>
	Sup friends?
	<br/>
	<a href="hello"><input type="button" value="say hi"/></a>
	<br/>
	<a href="jigvu"><input type="button" value="error :)"/></a>
	<br/>
	<a href="exception"><input type="button" value="exception :o"/></a>
	<br/>
	<a href="admin"><input type="button" value="Admin?"/></a>
	<br/>
	<a href="adminEdit"><input type="button" value="Admin Edit"/></a>
</body>
</html>