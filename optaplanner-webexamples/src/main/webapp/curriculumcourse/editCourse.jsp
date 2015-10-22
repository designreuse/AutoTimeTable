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
        <script src="<%=application.getContextPath()%>/twitterbootstrap/js/jquery.min.js"></script>        
        <script src="<%=application.getContextPath()%>/twitterbootstrap/js/bootstrap.min.js"></script>
        <title>Ders Düzenleme Sayfası</title>
    </head>
    <script >
        $(document).ready(function () {
            $("#hidelab").hide();
            $("#checkLab").change(function () {
                if($(this).is(':checked')) {
                    $("#hidelab").show();
                } else {
                    $("#hidelab").hide();
                }
            });
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
            <div class="row-fluid">
                <div class="col-md-6">
                    <button type="submit" class="btn btn-default" data-toggle="modal" data-target="#myModal">Ekle</button>
                    <a href="courseScheduleEdit.jsp" type="submit" class="btn btn-danger btn" >Geri Dön</a>
                </div>
            </div>  
            <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <form action="CourseEditSaveServlet" method="POST">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title" id="myModalLabel">Ders Bilgileri</h4>
                            </div>
                            <div class="modal-body">

                                <div class="form-group">
                                    <label for="courseCode">Kodu</label>
                                    <input type="text" class="form-control" id="courseCode" name="courseCode"/>
                                </div>
                                <div class="form-group">
                                    <label for="courseName">Adı</label>
                                    <input type="text" class="form-control" id="courseName" name="courseName"/>
                                </div>

                                <div class="form-group">
                                    <label for="courseLectureSize">Ders Saati</label>
                                    <input type="text" class="form-control" id="courseLectureSize" name="courseLectureSize"/>
                                </div>
                                <div class="form-group">
                                    <label for="courseStudentSize">Öğrenci Sayısı</label>
                                    <input type="text" class="form-control" id="courseStudentSize" name="courseStudentSize"/>
                                </div>
                                <div class="form-group">
                                    <label for="courseTeacher">Öğretim Elemanı</label>
                                    <select class="form-control" id="courseTeacher" name="courseTeacher" >
                                        <c:forEach var="teacher" items="${requestScope.teacherList}">
                                            <option value="${teacher.id}">${teacher.degree.shortName} ${teacher.name} ${teacher.surname}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <label>Dersi Alacak Sınıfları Seçin</label>
                                <c:forEach var="curriculum" items="${curriculumList}" varStatus="i">
                                    <div class="checkb  ox checkbox-inline">
                                        <input type="checkbox" name="isSelected-${curriculum.code}" value="${curriculum.code}">${curriculum.code}</input>
                                    </div>
                                </c:forEach>
                                <div class="form-group">
                                    <label for="checkLab">Dersin Teorisi Var mı ?</label>
                                    <input type="checkbox" class="checkbox" name="checkLab" id="checkLab"/>
                                </div>
                                <div id="hidelab">
                                    <div class="form-group" id="labLectureSize">
                                        <label for="labLectureSize">Teori Saati</label>
                                        <input type="text" name="labLectureSize"  class="form-control"/>
                                    </div>
                                    <label>Teori Dersleri İçin Derslikleri Seçin</label>
                                    <c:forEach var="room" items="${roomList}" varStatus="i">
                                        <div class="checkb  ox checkbox-inline">
                                            <input type="checkbox" name="isSelectedRoom-${room.code}">${room.code}</input>
                                        </div>
                                    </c:forEach>
                                </div>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                <button type="submit" class="btn btn-primary">Save changes</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
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
