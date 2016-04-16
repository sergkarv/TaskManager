<%@ page import="java.util.Enumeration" %>
<%@ page import="java.sql.Date" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.TimeZone" %>
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
    <div class="well lead">Task Form</div>
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
                    <input name="time" type="datetime-local" path="date" id="date" class="form-control" />
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
                    <input name="highpriority" type="checkbox" path="highPriority" id="highPriority" class="form-control input-sm" />
                    <div class="has-error">
                        <form:errors path="highPriority" class="help-inline"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="form-group col-md-12">
                <label class="col-md-3 control-lable">User</label>
                <div class="col-md-7">
                    <select name="parent" class="form-control">
                        <option>null</option>
                        <c:forEach var="name" items="${tasklistJSP}">
                            <c:choose>
                                <c:when test="${taskJSP.parentId ne null}">
                                    <c:if test="${taskJSP.parentId eq name.id}">
                                        <option selected>${name.name} (${name.id})</option>
                                    </c:if>
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
                <label class="col-md-3 control-lable">User</label>
                <div class="col-md-7">
                    <select name="user" class="form-control" >
                        <c:forEach var="name" items="${userlistJSP}">
                            <c:choose>
                                <c:when test="${taskJSP.userId ne null}">
                                    <c:if test="${taskJSP.userId eq name.id}}">
                                        <option  selected>${name.name} (${name.id})</option>
                                    </c:if>
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

<%!
    Calendar strToCalendar(String s){
        String date = s.substring(0 , s.indexOf('T'));
        String time = s.substring(s.indexOf('T')+1, s.length());

        int hour = Integer.valueOf(time.substring(0, time.indexOf(':')));
        int minute = Integer.valueOf( time.substring(time.indexOf(':')+1, time.length()  ));
        int year = Integer.valueOf(date.substring(0, date.indexOf('-')));
        int month = Integer.valueOf( date.substring( date.indexOf('-')+1, date.lastIndexOf('-') ) );
        int day = Integer.valueOf( date.substring( date.lastIndexOf('-')+1, date.length()  ) );

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.setTimeZone(TimeZone.getTimeZone("UTF+4"));
        return calendar;
    }
%>

<%
    // Get the value of the request parameter
    String value = request.getParameter("time");
    if(value == null) return;
    Calendar calendar = strToCalendar(value);

    boolean highFlag = false;
    value = request.getParameter("highpriority");
    if(value == null) return;
    if(value.equals("on")){
        highFlag = true;
    }

    value = request.getParameter("parent");
    String idParentString = null;
    Integer idParent = null;
    if(value != null){
        if(!value.equals("null")){
            idParentString = value.substring( value.indexOf('(')+1, value.length() );
            idParent = Integer.valueOf(idParentString);
        }
    }

    value = request.getParameter("user");
    if(value == null) return;
    String idUserString = null;
    Integer idUser = null;
    idUserString = value.substring( value.indexOf('(')+1, value.length() );
    idUser = Integer.valueOf(idParentString);

    {
%>
        <c:set var="${taskJSP.date}" value="<%= calendar%>"/>
        <c:set var="${taskJSP.highPriority}" value="<%= highFlag%>"/>
        <c:set var="${taskJSP.parentId}" value="<%= idParent%>"/>
        <c:set var="${taskJSP.userId}" value="<%= idUser%>"/>
<%
    }
%>



</html>
