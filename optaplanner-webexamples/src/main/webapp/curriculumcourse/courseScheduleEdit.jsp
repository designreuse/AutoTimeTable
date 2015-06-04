<%-- 
    Document   : new_courseschedule
    Created on : Jun 1, 2015, 11:40:25 PM
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
            </div>
            
            <div class="row">
                <div class="col-md-3">
                    <div class="jumbotron">
                        <p><a class="btn btn-primary btn-lg" href="TeachersViewServlet" role="button">Öğretim Elemanı İşlemleri</a></p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="jumbotron">
                        <p><a class="btn btn-primary btn-lg" href="RoomsViewServlet" role="button">Derslik İşlemleri</a></p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="jumbotron">
                        <p><a class="btn btn-primary btn-lg" href="CurriculumsViewServlet" role="button">Sınıf İşlemleri</a></p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="jumbotron">
                        <p><a class="btn btn-primary btn-lg" href="#" role="button">Ders İşlemleri</a></p>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-6">
                    <a type="button" class="btn btn-success btn-lg" style="float: right">Kaydet</a>
                </div>
                <div class="col-md-6">
                    <a type="button" class="btn btn-danger btn-lg" style="float: left">İptal</a>
                </div>
            </div>
            
        </div>
    </body>
</html>
