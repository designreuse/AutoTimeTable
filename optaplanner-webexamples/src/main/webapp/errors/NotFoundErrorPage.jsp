<%-- 
    Document   : NotFoundErrorPage
    Created on : 15.Şub.2015, 19:28:37
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
<body style="background-image: url(data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABoAAAAaCAYAAACpSkzOAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAALEgAACxIB0t1+/AAAABZ0RVh0Q3JlYXRpb24gVGltZQAxMC8yOS8xMiKqq3kAAAAcdEVYdFNvZnR3YXJlAEFkb2JlIEZpcmV3b3JrcyBDUzVxteM2AAABHklEQVRIib2Vyw6EIAxFW5idr///Qx9sfG3pLEyJ3tAwi5EmBqRo7vHawiEEERHS6x7MTMxMVv6+z3tPMUYSkfTM/R0fEaG2bbMv+Gc4nZzn+dN4HAcREa3r+hi3bcuu68jLskhVIlW073tWaYlQ9+F9IpqmSfq+fwskhdO/AwmUTJXrOuaRQNeRkOd5lq7rXmS5InmERKoER/QMvUAPlZDHcZRhGN4CSeGY+aHMqgcks5RrHv/eeh455x5KrMq2yHQdibDO6ncG/KZWL7M8xDyS1/MIO0NJqdULLS81X6/X6aR0nqBSJcPeZnlZrzN477NKURn2Nus8sjzmEII0TfMiyxUuxphVWjpJkbx0btUnshRihVv70Bv8ItXq6Asoi/ZiCbU6YgAAAABJRU5ErkJggg==);">
<div class="container">
    <div class="row">
        <div class="col-md-12">
            <div class="error-template">
                <h1>
                    Oops!</h1>
                <h2>
                    404 Kaynak Bulunamadı</h2>
                <div class="error-details">
                    Üzgünüz, Erişmek istediğiniz sayfayı bulamadık.
                </div>
                <div class="error-actions">
                    <a href="/optaplanner-webexamples/index.jsp" class="btn btn-primary btn-lg">
                        <span class="glyphicon glyphicon-home"></span>
                        Ana Sayfaya Dönün </a>
                    <a href="/optaplanner-webexamples/index.jsp" class="btn btn-default btn-lg">
                        <span class="glyphicon glyphicon-envelope"></span> Destek İsteyin </a>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
