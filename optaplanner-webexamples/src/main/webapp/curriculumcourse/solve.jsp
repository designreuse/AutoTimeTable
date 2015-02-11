

<%@page import="org.optaplanner.webexamples.curriculumcourse.CurriculumCourseWebAction"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    new CurriculumCourseWebAction().solve(session);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="REFRESH" content="0;url=<%=application.getContextPath()%>/curriculumcourse/solving.jsp"/>

    <title>JSP Page</title>
</head>
<body>
   
</body>
</html>
