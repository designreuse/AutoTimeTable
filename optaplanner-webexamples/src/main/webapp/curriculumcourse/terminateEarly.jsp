<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="org.optaplanner.webexamples.curriculumcourse.CurriculumCourseWebAction"%>

<%
    new CurriculumCourseWebAction().terminateEarly(session);
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="REFRESH" content="0;url=terminated.jsp"/>
    </head>
    <body>
    </body>
</html>
