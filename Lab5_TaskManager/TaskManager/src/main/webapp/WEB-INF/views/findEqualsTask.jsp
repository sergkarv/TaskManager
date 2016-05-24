<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Find Task</title>
    <link href="<c:url value='/static/css/bootstrap.css'/>" rel="stylesheet" type="text/css"/>
    <link href="<c:url value='/static/css/app.css'/>" rel="stylesheet" type="text/css"/>
</head>

<body>


<div class="generic-container">
    <div class="well lead">Find Task</div>
    <form:form method="POST"  class="form-horizontal">

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" >Id</label>
                <div class="col-md-7">
                    <input type="text"  name="Id"  class="form-control input-sm"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable" >Name</label>
                <div class="col-md-7">
                    <input type="text"  name="Name" class="form-control input-sm"/>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Contacts</label>
                <div class="col-md-7">
                    <input type="text"  name="Contacts" class="form-control input-sm" />
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">Parent</label>
                <div class="col-md-7">
                    <select name="parent" class="form-control">
                        <option>null</option>
                        <c:forEach var="name" items="${taskListJSP}">
                            <option>${name.name} (${name.id})</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">User</label>
                <div class="col-md-7">
                    <select name="user" class="form-control" >
                        <c:forEach  var="name" items="${userListJSP}" varStatus="i">
                            <c:choose>
                                <c:when test="${i.count eq 0}">
                                    <option  selected>${name.name} (${name.id})</option>
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
                <input type="submit" value="Find" class="btn btn-primary btn-sm"/> or <a href="/search">Cancel</a>
            </div>
        </div>
    </form:form>
</div>

</body>

</html>
