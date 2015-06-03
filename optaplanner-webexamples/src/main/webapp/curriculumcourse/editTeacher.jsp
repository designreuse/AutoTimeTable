<%-- 
    Document   : teacherEdit
    Created on : Jun 3, 2015, 10:55:29 AM
    Author     : gurhan
--%>

<%@page import="java.util.List"%>
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
                        <h2>Öğretim Elemanları </h2>
                    </center>
                </header>
                <form class="form-inline" action="TeacherEditSaveServlet" method="Post">
                    <div class="form-group">
                        <label for="teacherCode">Sicil No</label>
                        <input type="text" class="form-control" id="teacherCode" name="teacherCode" >
                    </div>
                    <div class="form-group">
                        <label for="teacherDegree">Ünvan</label>
                        <select class="form-control" id="teacherDegree" name="teacherDegree" >
                            <c:forEach var="degree" items="${requestScope.degreeList}">
                                <option value="${degree.shortName}">${degree.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="teacherName">İsim</label>
                        <input type="text" class="form-control" id="teacherName" name="teacherName" >
                    </div>
                    <div class="form-group">
                        <label for="teacherName">Soyisim</label>
                        <input type="text" class="form-control" id="teacherSurname" name="teacherSurname" />
                    </div>
                    <button type="submit" class="btn btn-default">Ekle</button>
                </form>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Kodu</th>
                            <th>Ünvan</th>
                            <th>Ad</th>
                            <th>Soyad</th>
                            <th>İşlem</th>
                        </tr>
                    </thead>
                    <c:forEach var="teacher" items="${requestScope.teacherList}" varStatus="loop">
                        <tr>
                            <td>${teacher.code}</td>
                            <td>${teacher.degree.shortName}</td>
                            <td>${teacher.name}</td>
                            <td>${teacher.surname}</td>
                            <td>
                                <a href="TeacherInfoEditServlet?teacherId=${teacher.id}" class="btn btn-warning" >Düzenle</a>
                                <a href="#"  class="btn btn-info">Ders Günleri</a>
                                <a href ="#" class="btn btn-primary">Dersleri</a>
                                <a href="#" class="btn btn-danger">Sil</a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title" id="myModalLabel">Modal title</h4>
                            </div>
                            <div class="modal-body">
                                ...
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                                <button type="button" class="btn btn-primary">Save changes</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </body>
</html>
