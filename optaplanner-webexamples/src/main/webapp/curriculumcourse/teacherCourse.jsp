<%-- 
    Document   : teacherCourse
    Created on : Jun 6, 2015, 10:48:39 AM
    Author     : gurhan
--%>

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
                        <h2>${teacher.degree.shortName} ${teacher.name} ${teacher.surname}</h2>
                        <h3>Dersleri</h3>
                        <c:if test="${message != null}">
                            <h2><span class="label ${message.result == false ? 'label-danger' : 'label-success'} " style="width: 500px">${message.content}</span></h2>
                            </c:if>
                    </center>
                </header>
            </div>
            <form class="form-inline" action="AssignTheLesson" method="Post">
                <input type="hidden" value="${teacher.id}" name="teacherId" />
                <select name="courseId">
                    <c:forEach var="course" items="${noteacherCourseList}">
                        <option value="${course.id}">${course.code} - ${course.name}</option>
                    </c:forEach>
                </select>
                <input type="submit" class="btn btn-default" value="Dersi Ata" />
            </form>
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th>Kodu</th>
                        <th>Adı</th>
                        <th>Dersi Alan Sınıf</th>
                    </tr>
                </thead>
                <c:forEach var="course" items="${teacherCourseList}" >
                    <tr>
                        <td>${course.code}</td>
                        <td>${course.name}</td>
                        <td>${course.curriculumList[0].code}</td>
                        <td>
                            <a href="RemoveAssigmentTheLesson?teacherId=${teacher.id}&courseId=${course.id}" class="btn btn-danger">Atamayı Kaldır</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>

        </div>
    </body>
</html>
