<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>User export</title>
    <link href="<c:url value='/static/css/bootstrap.css'/>" rel="stylesheet" type="text/css"/>
    <link href="<c:url value='/static/css/app.css'/>" rel="stylesheet" type="text/css"/>
</head>

<body>


<div class="generic-container">
    <div class="well lead">User export</div>
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
                <label class="col-md-3 control-lable">Password</label>
                <div class="col-md-7">
                    <input type="text" name="Password" class="form-control input-sm" />
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-actions floatRight">
                <input type="submit" value="Export" class="btn btn-primary btn-sm"/> or <a href="/export/export_user">Cancel</a>
            </div>
        </div>
    </form:form>
</div>

</body>

</html>
