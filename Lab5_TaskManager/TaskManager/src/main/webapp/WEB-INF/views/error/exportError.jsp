<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Export Error Page</title>
    <link href="<c:url value='/static/css/bootstrap.css'/>" rel="stylesheet" type="text/css"/>
    <link href="<c:url value='/static/css/app.css'/>" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="generic-container">
    <div class="alert alert-warning lead">
        ${error}
    </div>

	<span class="well floatRight">
		Go to <a href="/export">Export Page</a>
	</span>
</div>
</body>

</html>