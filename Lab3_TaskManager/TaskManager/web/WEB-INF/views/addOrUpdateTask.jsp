<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Add Task Form</title>
    <link href="static/css/bootstrap.css" rel="stylesheet"/>
    <link href="static/css/app.css" rel="stylesheet"/>
    <link href="static/css/bootstrap-select.css" rel="stylesheet"/>
</head>

<body>

<div class="generic-container">
    <div class="well lead">Add Task Form</div>
    <form:form method="POST" modelAttribute="taskJSP" class="form-horizontal">

        <form:input type="hidden" path="id" id="id"/>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="Name">Name</label>
                <div class="col-md-7">
                    <form:input type="text" path="name" id="Name" class="form-control input-sm"/>
                    <div class="has-error">
                        <form:errors path="name" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="description">Description</label>
                <div class="col-md-7">
                    <form:input type="text" path="description" id="description" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="description" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="contacts">Contacts</label>
                <div class="col-md-7">
                    <form:input type="text" path="contacts" id="contacts" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="contacts" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="date">Date</label>
                <div class="col-md-7">
                    <form:input type="localtime" path="date" id="date" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="date" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" for="highPriority">High Priority</label>
                <div class="col-md-7">
                    <form:input type="checkbox" path="highPriority" id="highPriority" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="highPriority" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" >Date</label>
                <div class="col-md-7">
                    <select name="parent" class="selectpicker">
                        <c:forEach var="name" items="${tasklistJSP}">
                            <c:choose>
                                <c:when test="${taskJSP.parentId not null and taskJSP.parentId eq name.id}">
                                    <option selected>${name.name} (${name.id})</option>
                                </c:when>
                                <c:otherwise>
                                    <option>${name.name} (${name.id})</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" >Date</label>
                <div class="col-md-7">
                    <select name="user" class="selectpicker">
                        <c:forEach var="name" items="${userlistJSP}">
                            <c:choose>
                                <c:when test="${taskJSP.userId not null and taskJSP.userId eq name.id}">
                                    <option selected>${name.name} (${name.id})</option>
                                </c:when>
                                <c:otherwise>
                                    <option>${name.name} (${name.id})</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-actions floatRight">
                <c:choose>
                    <c:when test="${edit}">
                        <input type="submit" value="Update" class="btn btn-primary btn-sm"/> or <a href="<c:url value='/taskslist' />">Cancel</a>
                    </c:when>
                    <c:otherwise>
                        <input type="submit" value="Add" class="btn btn-primary btn-sm"/> or <a href="<c:url value='/taskslist' />">Cancel</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </form:form>
</div>
</body>
</html>
