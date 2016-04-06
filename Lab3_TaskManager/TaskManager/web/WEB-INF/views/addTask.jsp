<%@ taglib prefix="spring" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: Администратор
  Date: 31.03.16
  Time: 12:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<spring:form method="post" modelAttribute="taskJSP" action="addTask">
    Name: <spring:input path="name"/>  <br/>
    Description: <spring:input path="description"/> <br/>
    Contacts: <spring:input path="contacts"/> <br/>
    Date: <spring:input path="date"/> <br/>
    <input name = "Priority" type="radio" value="low" checked>Low
    <input name = "Priority" type="radio" value="high">High <br/>
    <select name="parent">
        <c:forEach var="name" items="${listTask}">
            <option>${name}</option>
        </c:forEach>
    </select>
    <br/>
    <select name="user">
        <c:forEach var="name" items="${listUser}">
            <option>${name}</option>
        </c:forEach>
        ${priorityJSP}= ${false}

    </select>
    <br/>
    <spring:button>Ok</spring:button>
</spring:form>
</body>
</html>
