<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Import XML page</title>
    <link href="<c:url value='/static/css/bootstrap.css'/>" rel="stylesheet" type="text/css"/>
    <link href="<c:url value='/static/css/app.css'/>" rel="stylesheet" type="text/css"/>
</head>

<body>
<div class="generic-container">
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead">Import XML File</span></div>


        <form:form method="post" enctype="multipart/form-data" modelAttribute="XMLFile">

            <table class="table table-hover">

                <tr>
                    <td>User</td>
                    <td><input name="type_import" type="radio" value="user" class="form-control" checked></td>
                </tr>

                <tr>
                    <td>Task</td>
                    <td><input name="type_import" type="radio" value="task" class="form-control  "></td>
                </tr>

                <tr>
                    <td>Select File:</td>
                    <td><input type="file" name="file" class="form-control" accept="application/xml"/></td>
                    <td style="color: red; font-style: italic;">
                        <form:errors path="file" /></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" value="Import" class="btn btn-primary btn-sm" /></td>
                    <td></td>
                </tr>
            </table>
        </form:form>

    </div>

    <div class="well">
        <a href="/check-user">Go to Main page</a>
    </div>
</div>
</body>

</html>