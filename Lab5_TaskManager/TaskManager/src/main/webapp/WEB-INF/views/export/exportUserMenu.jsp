<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Export User page</title>
    <link href="<c:url value='/static/css/bootstrap.css'/>" rel="stylesheet" type="text/css"/>
    <link href="<c:url value='/static/css/app.css'/>" rel="stylesheet" type="text/css"/>

</head>
<body>

<div class="generic-container">
    <div class="well lead">Export User page</div>

    <div class="well">
        <a href="/export/export_user/get_all_users">Export All Users</a><br/>
    </div>

    <div class="well">
        <a href="/export/export_user/equals_query">Export equals user</a><br/>
    </div>

    <div class="well">
        Go to <a href="/export">Export Page</a>
    </div>

    <div class="well">
        Go to <a href="/check-user">Main Page</a>
    </div>

</div>
</body>
</html>