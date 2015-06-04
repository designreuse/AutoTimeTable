<%-- 
    Document   : roomChangeInfo
    Created on : Jun 3, 2015, 11:53:22 PM
    Author     : gurhan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
                    ${contents.prepareContents(pageContext.servletContext)}
                    <center style="margin-top: 15px"> 
                        <img src="../website/img/atauni.png" alt="Resim Bulunamadı" class="img-circle" /> 
                        <h2>Yeni Bilgileri Girin </h2>
                    </center>
                </header>
                <form class="form-inline" action="RoomEditSaveServlet" method="Post">
                    <input type="hidden" value="${requestScope.room.id}" name="roomId"/>
                    <div class="form-group">
                        <label for="roomCode">Kodu</label>
                        <input type="text" class="form-control" id="roomCode" name="roomCode" value="${requestScope.room.code}" >
                    </div>
                    <div class="form-group">
                        <label for="roomCapacity">Kapasitesi</label>
                        <input type="text" class="form-control" id="roomCapacity" name="roomCapacity" placeholder="Sayı Girin..." value="${requestScope.room.capacity}">
                    </div>
                    <button type="submit" class="btn btn-default">Düzenle</button>
                </form>
            </div>
       </div>
    </body>
</html>
