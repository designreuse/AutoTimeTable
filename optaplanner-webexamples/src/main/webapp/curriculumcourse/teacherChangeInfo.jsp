<%-- 
    Document   : teacherInfoChange
    Created on : Jun 3, 2015, 4:38:06 PM
    Author     : gurhan
--%>

<%@page import="java.util.List"%>
<%@page import="org.optaplanner.examples.curriculumcourse.domain.TeacherDegree"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<%=application.getContextPath()%>/twitterbootstrap/css/bootstrap.css" rel="stylesheet" />
        <link href="<%=application.getContextPath()%>/twitterbootstrap/css/bootstrap.min.css" rel="stylesheet" />
        <link href="<%=application.getContextPath()%>/twitterbootstrap/css/bootstrap-responsive.css" rel="stylesheet" />
        <link href="<%=application.getContextPath()%>/website/css/optaplannerWebexamples.css" rel="stylesheet"/>
        <title>Öğretim Elemanı Düzenleme Sayfası</title>
    </head>

    <body>
        <div class="container-fluid">
            <jsp:include page="header.jsp" />
            <div class="row-fluid">
                <header class="main-page-header" style="margin-bottom: 20px">
                    ${contents.prepareContents(pageContext.servletContext)}
                    <center style="margin-top: 15px"> 
                        <img src="../website/img/atauni.png" alt="Resim Bulunamadı" class="img-circle" /> 
                        <h2> Yeni Bilgileri Girin </h2>
                    </center>
                </header>
            </div>
            <form class="form-inline" action="TeacherEditSaveServlet" method="Post">
                <input type="hidden" name="teacherId" value="${requestScope.teacher.id}" />
                <div class="form-group">
                    <label for="teacherCode">Sicil Noss</label>
                    <input type="text" class="form-control" id="teacherCode" name="teacherCode" value="${requestScope.teacher.code}">
                </div>
                
                <div class="form-group">
                    <label for="teacherDegree">Ünvan</label>
                    <select class="form-control" id="teacherDegree" name="teacherDegree" >
                        <c:forEach var="degree" items="${requestScope.degreeList}">
                            <option value="${degree.shortName}" ${degree.shortName == requestScope.teacher.degree.shortName ? 'selected="selected"':''}>${degree.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="teacherName">İsim</label>
                    <input type="text" class="form-control" id="teacherName" name="teacherName" value="${requestScope.teacher.name}">
                </div>
                <div class="form-group">
                    <label for="teacherName">Soyisim</label>
                    <input type="text" class="form-control" id="teacherSurname" name="teacherSurname" value="${requestScope.teacher.surname}" />
                </div>
                <button type="submit" class="btn btn-default">Düzenle</button>
            </form>      
        </div>
    </body>
</body>
</html>
