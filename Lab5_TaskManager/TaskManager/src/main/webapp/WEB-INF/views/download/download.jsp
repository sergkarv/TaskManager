<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Download export file</title>
    <link href="<c:url value='/static/css/bootstrap.css'/>" rel="stylesheet" type="text/css"/>
    <link href="<c:url value='/static/css/app.css'/>" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="generic-container">

    <div class="alert alert-success lead">
        ${downloadInfo}
    </div>

    <span class="well floatRight">
		Go to <a href="/export">Export Page</a>
	</span>

    <span class="well floatRight">
		Go to <a href="/check-user">Main Page</a>
	</span>

    <span class="well floatRight">
        <a href="/export/view_xml-${nameXML}" >View file</a>
    </span>

    <span class="well floatRight">
        <a href="/export/download_xml-${nameXML}" >Download file</a>
    </span>

</div>
</body>

</html>