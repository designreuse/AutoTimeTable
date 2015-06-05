<%-- 
    Document   : courseInfoEdit
    Created on : Jun 5, 2015, 1:47:11 PM
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
        <title>Ders Düzenleme Sayfası</title>
    </head>
    <body>
        <div class="container-fluid">
            <jsp:include page="header.jsp" />
            <div class="row-fluid">
                <header class="main-page-header" style="margin-bottom: 20px">
                    <center style="margin-top: 15px"> 
                        <img src="../website/img/atauni.png" alt="Resim Bulunamadı" class="img-circle" /> 
                        <h2>Dersler</h2>
                    </center>
                </header>
            </div>
            <form action="CourseEditSaveServlet" method="POST" style="margin-left: 20px">
                <input type="hidden" value="${requestScope.course.id}" name="courseId" />
                <div class="row">
                    <div class="form-group col-md-3">
                        <label for="courseCode">Kodu</label>
                        <input type="text" value="${requestScope.course.code}" class="form-control" id="courseCode" name="courseCode"/>
                    </div>
                    <div class="form-group col-md-3">
                        <label for="courseName">Adı</label>
                        <input type="text" value="${requestScope.course.name}" class="form-control" id="courseName" name="courseName"/>
                    </div>
                    <div class="form-group col-md-3">
                        <label for="courseLectureSize">Ders Saati</label>
                        <input type="text" value="${requestScope.course.lectureSize}" class="form-control" id="courseLectureSize" name="courseLectureSize"/>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-3">
                        <label for="courseStudentSize">Öğrenci Sayısı</label>
                        <input type="text" value="${requestScope.course.studentSize}" class="form-control" id="courseStudentSize" name="courseStudentSize"/>
                    </div>
                    <div class="form-group col-md-3">
                        <label for="courseTeacher">Öğretim Elemanı</label>
                        <select class="form-control" id="courseTeacher" name="courseTeacher" >
                            <c:forEach var="teacher" items="${requestScope.teacherList}">
                                <option value="${teacher.id}"
                                        ${teacher.id == requestScope.course.teacher.id ? 'selected="selected"':''}>${teacher.degree.shortName} ${teacher.name} ${teacher.surname}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group col-md-3">
                        <label for="courseCurriculum">Sınıf</label>
                        <select class="form-control" id="courseCurriculum" name="courseCurriculum" >
                            <c:forEach var="curriculum" items="${requestScope.curriculumList}">
                                <option value="${curriculum.id}" 
                                        ${curriculum.id == requestScope.course.curriculumList[0].id ? 'selected="selected"':''}>${curriculum.code}</option>
                            </c:forEach>
                        </select>

                    </div>
                    <div class="row">
                        <div class="col-md-7" ></div>
                        <button type="submit" class="btn btn-success btn col-md-2" >Ekle</button>

                    </div>
            </form>
        </div>
    </body>
</html>
