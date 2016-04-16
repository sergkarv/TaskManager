<%--
  Created by IntelliJ IDEA.
  User: Сергей
  Date: 09.04.16
  Time: 6:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Main page</title>
    <link href="static/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="static/css/app.css" rel="stylesheet" type="text/css"/>
</head>
<body>

    <div class="generic-container">
        <div class="well lead">Main page</div>

        <div class="well">
            <a href="/userslist">View Users</a><br/>
        </div>
        <div class="well">
            <a href="/taskslist">View Tasks</a><br/>
        </div>
        <div class="well">
            <a href="/addUser">Add User</a><br/>
        </div>
        <div class="well">
            <a href="/removeTask">Remove Task</a><br/>
        </div>
        <div class="well">
            <a href="/removeUser">Remove User</a><br/>
        </div>

    </div>
</body>
</html>
