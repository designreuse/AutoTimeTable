<%-- 
    Document   : editCurricula
    Created on : Jun 4, 2015, 7:02:52 PM
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
        <script src="<%=application.getContextPath()%>/twitterbootstrap/js/jquery.min.js"></script>        
        <script src="<%=application.getContextPath()%>/twitterbootstrap/js/bootstrap.min.js"></script>
        <title>Sınıf Düzenleme Sayfası</title>
    </head>

    <body>
        <div class="container-fluid">
            <jsp:include page="header.jsp" />
            <div class="row-fluid">
                <header class="main-page-header" style="margin-bottom: 20px">
                    <center style="margin-top: 15px"> 
                        <img src="../website/img/atauni.png" alt="Resim Bulunamadı" class="img-circle" /> 
                        <h2>Sınıflar</h2>
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
                        <form action="CurriculumEditSaveServlet" method="Post">
                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                <h4 class="modal-title" id="myModalLabel">Modal title</h4>
                            </div>
                            <div class="modal-body">

                                <div class="form-group">
                                    <label for="curriculumLevel">Sınıf Seviyesi</label>
                                    <select name="curriculumLevel" id="curriculumLevel" class="form-control">
                                        <option value="1">1</option>
                                        <option value="2">2</option>
                                        <option value="3">3</option>
                                        <option value="4">4</option>
                                        <option value="5">5</option>
                                        <option value="6">6</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="numberOfBranch">Şube Sayısı</label>
                                    <select name="numberOfBranch" id="numberOfBranch" class="form-control">
                                        <option value="1">1</option>
                                        <option value="2">2</option>
                                        <option value="3">3</option>
                                        <option value="4">4</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>
                                        <input type="checkbox" name="isCheckedNight" value="true"> İkinci Öğretim Eklensin
                                    </label>
                                </div>
                                <div class="form-group">
                                    <label>
                                        <input type="checkbox" name="isCheckedTr" value="true"> Türkçe
                                    </label>
                                    <label>
                                        <input type="checkbox" name="isCheckedPercent30Eng" value="true"> %30 İngilizce
                                    </label>
                                    <label>
                                        <input type="checkbox" name="isCheckedEng"> %100 İngilizce
                                    </label>
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
            <table class="table table-striped table-bordered">
                <thead>
                    <tr>
                        <th>Kodu</th>
                        <th>Öğrenim Tipi</th>
                    </tr>
                </thead>
                <c:forEach items="${requestScope.curriculumList}" var="curricula">
                    <tr>
                        <td>${curricula.code}</td>
                        <td>${curricula.nightClass == false ? 'Örgün Öğretim':'İkinci Öğretim'}</td>
                        <td>
                            <a href="CurriculumInfoEditServlet?curriculumId=${curricula.id}" class="btn btn-warning" >Düzenle</a>
                            <a href="#"  class="btn btn-info">Sınıf Dersleri</a>
                            <a href="#" class="btn btn-danger">Sil</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </body>
</html>
