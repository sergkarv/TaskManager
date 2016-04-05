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
<spring:form method="post" modelAttribute="taskJSP" action="addTaskPost">
    Name: <spring:input path="name"/>  <br/>
    Description: <spring:input path="description"/> <br/>
    Contacts: <spring:input path="contacts"/> <br/>
    Date: <spring:input path="date"/> <br/>
    <c:set var="priority" value="low"/>

    <input name = "Priority" type="radio" value="low" checked>Low
    <input name = "Priority" type="radio" value="high">High <br/>

    <select name="parent">
        <c:forEach var="parentObject" items="${parentJSP}">
            <option>${parentObject.name}</option>
        </c:forEach>
    </select>

    <select name="user">
        <c:forEach var="userObject" items="${usersJSP}">
            <option>${userObject.name}</option>
        </c:forEach>
    </select>
    <spring:button>Ok</spring:button>
</spring:form>

<%
    String selectParent[] = request.getParameterValues("parent");
    if (selectParent != null && selectParent.length != 0) {
        for (int i = 0; i < selectParent.length; i++) {
            out.println(selectParent[i]);
        }
    }

    String radio[] = request.getParameterValues("Priority");
    if (radio != null && radio.length != 0) {
        for (int i = 0; i < radio.length; i++) {
            out.println(radio[i]);
        }
    }

    String selectUser[] = request.getParameterValues("user");
    if (selectUser != null && selectUser.length != 0) {
        for (int i = 0; i < select.length; i++) {
            out.println(selectUser[i]);
        }
    }
%>

</body>
</html>
