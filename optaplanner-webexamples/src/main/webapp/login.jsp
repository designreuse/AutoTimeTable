<%-- 
    Document   : login
    Created on : 14.Şub.2015, 21:04:59
    Author     : gurhan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="<%=application.getContextPath()%>/twitterbootstrap/css/bootstrap.css" rel="stylesheet" />
    <link href="<%=application.getContextPath()%>/website/css/optaplannerWebexamples.css" rel="stylesheet"/>
    <title>JSP Page</title>
</head>
<body>
<div class="container">
    <form class="form-signin" action="  UserLoginServlet" method="post">
        <center>
            <img src="website/img/atauni.png" alt="Resim Bulunamadı" class="img-circle" /> 
            <h3 class="form-signin-heading">Atatürk Üniversitesi</h3>
            <h3 class="form-signin-heading" style="margin-bottom: 20px">Ders Programı Otomasyonu</h3>
        </center>
        <label class="sr-only" for="userName">Tc No </label>
        <input name="userName" id="userName" class="form-control"  placeholder="Tc No" type="text" required=""/>
        <label class="sr-only" for="password">Şifre</label>
        <input name="password" type="password" id="password" class="form-control" placeholder="Şifre" required=""/>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Giriş Yapın</button>
    </form>
</div>
</body>
</html>
