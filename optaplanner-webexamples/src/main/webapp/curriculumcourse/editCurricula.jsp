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
            </div>
            <form class="form-inline" action="CurriculumEditSaveServlet" method="Post">
                <div class="form-group">
                    <label for="curriculumCode">Kodu</label>
                    <input type="text" class="form-control" id="roomCode" name="curriculumCode" >
                </div>
                <button type="submit" class="btn btn-default">Ekle</button>
            </form>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Kodu</th>
                    </tr>
                </thead>
                <c:forEach items="${requestScope.curriculumList}" var="curricula">
                    <tr>
                        <td>${curricula.code}</td>
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
