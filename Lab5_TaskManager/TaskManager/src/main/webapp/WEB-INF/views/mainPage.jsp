<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Main page</title>
    <link href="<c:url value='/static/css/bootstrap.css'/>" rel="stylesheet" type="text/css"/>
    <link href="<c:url value='/static/css/app.css'/>" rel="stylesheet" type="text/css"/>
</head>
<body>

    <div class="form-control">
        <div class="well lead">Main page</div>

        <div class="well">
            <a href="/userslist">View Users</a><br/>
        </div>

        <div class="well">
            <a href="/taskslist">View Tasks</a><br/>
        </div>

        <div class="well">
            <a href="/newuser">Add User</a><br/>
        </div>

        <div class="well">
            <a href="/newtask">Add Task</a><br/>
        </div>

        <div class="well">
            <a href="/export">Export</a><br/>
        </div>

        <div class="well">
            <a href="/import_xml">Import</a><br/>
        </div>

        <div class="well">
            <a href="/search">Search</a><br/>
        </div>

    </div>
</body>
</html>
