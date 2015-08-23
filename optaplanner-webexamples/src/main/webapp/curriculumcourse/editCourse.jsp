<%-- 
    Document   : editCourse
    Created on : Jun 5, 2015, 1:19:33 PM
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
        <script src="<%=application.getContextPath()%>/twitterbootstrap/js/jquery.js"></script>
        <link href="<%=application.getContextPath()%>/twitterbootstrap/css/bootstrap-responsive.css" rel="stylesheet" />
        <link href="<%=application.getContextPath()%>/website/css/optaplannerWebexamples.css" rel="stylesheet"/>
        <title>Ders Düzenleme Sayfası</title>
    </head>
    <script >
         $(document).ready(function () {
             if($("#checkLab").is(':checked')) {
                 $("labLectureSize").show();
                 $("labRoomDeps").show();
             } else {
                 $("labLectureSize").hide();
                 $("labRoomDeps").hide();
             }
         });
    </script>
    <body>
        <div class="container-fluid">
            <jsp:include page="header.jsp" />
            <div class="row-fluid">
                <header class="main-page-header" style="margin-bottom: 20px">
                    <center style="margin-top: 15px"> 
                        <img src="../website/img/atauni.png" alt="Resim Bulunamadı" class="img-circle" /> 
                        <h2>Dersler</h2>
                        <c:if test="${message != null}">
                            <h2><span class="label ${message.result == false ? 'label-danger' : 'label-success'} " style="width: 500px">${message.content}</span></h2>
                            </c:if>
                    </center>
                </header>
            </div>
            <form  action="CourseEditSaveServlet" method="POST" style="margin-left: 20px">
                <div class="row">
                    <div class="form-group col-md-3">
                        <label for="courseCode">Kodu</label>
                        <input type="text" class="form-control" id="courseCode" name="courseCode"/>
                    </div>
                    <div class="form-group col-md-3">
                        <label for="courseName">Adı</label>
                        <input type="text" class="form-control" id="courseName" name="courseName"/>
                    </div>

                    <div class="form-group col-md-3">
                        <label for="courseLectureSize">Ders Saati</label>
                        <input type="text" class="form-control" id="courseLectureSize" name="courseLectureSize"/>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-3">
                        <label for="courseStudentSize">Öğrenci Sayısı</label>
                        <input type="text" class="form-control" id="courseStudentSize" name="courseStudentSize"/>
                    </div>
                    <div class="form-group col-md-3">
                        <label for="courseTeacher">Öğretim Elemanı</label>
                        <select class="form-control" id="courseTeacher" name="courseTeacher" >
                            <c:forEach var="teacher" items="${requestScope.teacherList}">
                                <option value="${teacher.id}">${teacher.degree.shortName} ${teacher.name} ${teacher.surname}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group col-md-3">
                        <label for="courseCurriculum">Sınıf</label>
                        <select class="form-control" id="courseCurriculum" name="courseCurriculum" >
                            <c:forEach var="curriculum" items="${requestScope.curriculumList}">
                                <option value="${curriculum.id}">${curriculum.code}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-3">
                        <label for="checkLab">Dersin Teorisi Var</label>
                        <input type="checkbox" class="checkbox" name="checkLab" id="checkLab"/>
                    </div>
                    <div class="form-group col-md-3" id="labLectureSize">
                        <label for="labLectureSize">Teori Saati</label>
                        <input type="text" name="labLectureSize"  class="form-control"/>
                    </div>
                </div>
                <div class="row" id="labRoomDeps">
                    <label>Teori Dersleri İçin Derslikleri Seçin</label>
                    <c:forEach var="room" items="${roomList}" varStatus="i">
                        <div class="checkb  ox checkbox-inline">
                            <input type="checkbox" name="isSelectedRoom-${room.code}" ${isRoomSelectArray[i.index] == true ? 'checked' :''}>${room.code}</input>
                        </div>
                    </c:forEach>
                </div>
                <div class="row">
                    <div class="col-md-7" ></div>
                    <button type="submit" class="btn btn-success btn col-md-1" >Ekle</button>
                    <a href="courseScheduleEdit.jsp" type="submit" class="btn btn-danger btn col-md-1" >Geri Dön</a>

                </div>
            </form>
            <table class="table table-bordered table-striped">
                <thead>
                    <tr>
                        <td>Kodu</td>
                        <td>Adı</td>
                        <td>Ders Saati</td>
                        <td>Öğrenci Sayısı</td>
                        <td>Öğretim Elemanı</td>
                        <td>Sınıf</td>

                    </tr>
                </thead>
                <c:forEach var="course" items="${requestScope.courseList}">
                    <tr>
                        <td>${course.code}</td>
                        <td>${course.name}</td>
                        <td>${course.lectureSize}</td>
                        <td>${course.studentSize}</td>
                        <td>${course.teacher.degree.shortName} ${course.teacher.name} ${course.teacher.surname}</td>
                        <td>${course.curriculumList[0].code}</td>
                        <td>
                            <a href="CourseInfoEditServlet?courseId=${course.id}" class="btn btn-warning" >Düzenle</a>
                            <a href="CourseRoomDep?courseId=${course.id}"  class="btn btn-info">Oda Bağımlılıkları</a>
                            <a href="#" class="btn btn-danger">Sil</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </body>
</html>
