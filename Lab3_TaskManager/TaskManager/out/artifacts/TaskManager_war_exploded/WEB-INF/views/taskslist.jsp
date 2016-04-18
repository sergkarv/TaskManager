<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Tasks List</title>
    <link href="static/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="static/css/app.css" rel="stylesheet" type="text/css"/>
</head>

<body>
<div >
    <div >

        <div class="well lead">List of Tasks</div>
        <table class="table table-hover">
            <thead>
            <tr>
                <th>Name</th>
                <th>Description</th>
                <th>Contacts</th>
                <th>Date</th>
                <th>High Priority</th>
                <th>Parent Task</th>
                <th>User</th>
                <th width="100"></th>
                <th width="100"></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${taskListJSP}" var="task">
                <tr>
                    <td>${task.name}</td>
                    <td>${task.description}</td>
                    <td>${task.contacts}</td>
                    <td>${task.date}</td>
                    <td>${task.highPriority}</td>
                    <td>
                            <c:if test="${task.parentId eq null}">
                                <c:out value="null"/>
                            </c:if>
                            <c:if test="${task.parentId ne null}">
                                <c:forEach items="${taskListJSP}" var="parentTask">
                                    <c:if test="${parentTask.id eq task.parentId}">
                                        ${parentTask.name}
                                    </c:if>
                                </c:forEach>
                            </c:if>
                    </td>

                    <td>
                        <c:forEach items="${userListJSP}" var="userForTask">
                           <c:if test="${task.userId eq userForTask.id}">
                               ${userForTask.name}
                           </c:if>
                        </c:forEach>
                    </td>

                    <td><a href="<c:url value='/edit-task-${task.id}' />" class="btn btn-success custom-width">edit</a></td>
                    <td><a href="<c:url value='/delete-task-${task.id}' />" class="btn btn-danger custom-width">delete</a></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="well">
        <a href="/newtask">Add New Task</a>
    </div>
    <div class="well">
        <a href="/check-user">Go to Main page</a>
    </div>
</div>
</body>
</html>
