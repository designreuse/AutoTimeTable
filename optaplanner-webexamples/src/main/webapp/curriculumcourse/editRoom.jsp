<%-- 
    Document   : editRoom
    Created on : Jun 3, 2015, 11:23:02 PM
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
        <title>Derslik Düzenleme Sayfası</title>
    </head>

    <body>
        <div class="container-fluid">
            <jsp:include page="header.jsp" />
            <div class="row-fluid">
                <header class="main-page-header" style="margin-bottom: 20px">
                    <center style="margin-top: 15px"> 
                        <img src="../website/img/atauni.png" alt="Resim Bulunamadı" class="img-circle" /> 
                        <h2>Derslikler</h2>
                    </center>
                </header>
                <form class="form-inline" action="RoomEditSaveServlet" method="Post">
                    <div class="form-group">
                        <label for="roomCode">Kodu</label>
                        <input type="text" class="form-control" id="roomCode" name="roomCode" >
                    </div>
                    <div class="form-group">
                        <label for="roomCapacity">Kapasitesi</label>
                        <input type="text" class="form-control" id="roomCapacity" name="roomCapacity" placeholder="Sayı Girin..." >
                    </div>
                    <button type="submit" class="btn btn-default">Ekle</button>
                </form>
                <table class="table table-striped table-bordered">
                    <thead>
                        <tr>
                            <th>Kodu</th>
                            <th>Kapasitesi</th>
                        </tr>
                    </thead>
                    <c:forEach var="room" items="${requestScope.roomList}" varStatus="loop">
                        <tr>
                            <td>${room.code}</td>
                            <td>${room.capacity}</td>
                            <td>
                                <a href="RoomInfoEditServlet?roomId=${room.id}" class="btn btn-warning" >Düzenle</a>
                                <a href="#"  class="btn btn-info">Derslik Günleri</a>
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
