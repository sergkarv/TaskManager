<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Index Page</title>
    <link href="static/css/bootstrap.css" rel="stylesheet"/>
    <link href="static/css/app.css" rel="stylesheet"/>
</head>
<body>

<div class="generic-container">

        <div class="well lead">Index Page</div>

        <spring:form method="post"  modelAttribute="userJSP" action="check-user" class="form-horizontal">

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
                    <label class="col-md-3 control-lable" for="password">Password</label>
                    <div class="col-md-7">
                        <form:input type="password" path="password" id="password" class="form-control input-sm" />
                        <div class="has-error">
                            <form:errors path="password" class="help-inline"/>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="form-actions floatRight">
                    <input type="submit" value="Sign in" class="btn btn-primary btn-sm"/>
                </div>
            </div>

        </spring:form>

</div>
</body>
</html>
