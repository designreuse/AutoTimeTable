<%-- 
    Document   : curriculaInfoEdit
    Created on : Jun 4, 2015, 10:48:15 PM
    Author     : gurhan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="<%=application.getContextPath()%>/twitterbootstrap/css/bootstrap.css" rel="stylesheet" />
        <link href="<%=application.getContextPath()%>/twitterbootstrap/css/bootstrap.min.css" rel="stylesheet" />
        <link href="<%=application.getContextPath()%>/twitterbootstrap/css/bootstrap-responsive.css" rel="stylesheet" />
        <link href="<%=application.getContextPath()%>/website/css/optaplannerWebexamples.css" rel="stylesheet"/>
        <title>Yeni Ders Programı Girişi</title>
    </head>
    <body style="padding-left: 40px; padding-right: 40px;">
        <div class="container-fluid">
            <jsp:include page="header.jsp" />
            <div class="row-fluid">
                <header  class="main-page-header">
                    <center style="margin-top: 15px"> 
                        <img src="../website/img/atauni.png" alt="Resim Bulunamadı" class="img-circle" /> 
                        <h2>Ders Programı Hazırlama Otomasyonu </h2>
                        <h3>Yeni Ders Programı Sistemi Oluşturma</h3>
                    </center>
                </header>
                <form class="form-inline" method="POST" action="CurriculumEditSaveServlet">
                    <input type="hidden" value="${requestScope.curriculum.id}" name="curriculumId" />
                    <div class="form-group">
                        <label for="curriculumCode">Sınıf Kodu</label>
                        <input type="text" value="${requestScope.curriculum.code}" name="curriculumCode" />
                    </div>
                    <button type="submit" class="btn btn-default">Düzenle</button>
                    <a href="CurriculumsViewServlet" type="submit" class="btn btn-danger" >Geri Dön</a>
                </form>
            </div>
        </div>

    </body>
</html>
