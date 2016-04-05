<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<spring:form method="post" modelAttribute="userJSP" action="addUser">
    Name: <spring:input path="name"/>  <br/>
    Password: <spring:input path="password"/> <br/>

    <spring:button>Ok</spring:button>
</spring:form>
</body>
</html>
