<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>HDOI)(@2024FNWNNC FNNOWOONNNN)</title>
</head>
<body>
	Err -- I -- am -- Error ........ ERROR!!
	<br/>
	<br/>
	A Spring Boot error page has access to the following:
	<table>
        <tr>
            <td>Date</td>
            <td>${timestamp}</td>
        </tr>
        <tr>
            <td>Error</td>
            <td>${error}</td>
        </tr>
        <tr>
            <td>Status</td>
            <td>${status}</td>
        </tr>
        <tr>
            <td>Message</td>
            <td>${message}</td>
        </tr>
        <tr>
            <td>Exception</td>
            <td>${exception}</td>
        </tr>
        <tr>
            <td>Path</td>
            <td>${path}</td>
        </tr>
        <tr>
            <td>Trace (note: app.props file should have server.error.include-stacktrace=always)</td>
            <td>
                <pre>${trace}</pre>
            </td>
        </tr>
    </table>
</body>
</html>