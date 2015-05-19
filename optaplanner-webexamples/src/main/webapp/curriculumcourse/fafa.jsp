<%-- 
    Document   : fafa
    Created on : May 15, 2015, 7:49:02 PM
    Author     : gurhan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <script type="text/javascript">
        function ajaxMetodu() {
            var xmlHttp;
            if (window.XMLHttpRequest) {
                xmlHttp = new XMLHttpRequest();
            } else {
                xmlHttp = new ActiveXObject();
            }
            xmlHttp.onreadystatechange = function () {
                if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
                }
            }
            xmlHttp.open("GET", "sunucudakiSinif", true);
            xmlHttp.send();
        }
    </script>
    <body>
        <input type="button" value="AJAX ile bilgi getir" onclick="ajaxMetodu()"/>
        <%try {
             String s=   request.getAttribute("veri").toString();
             out.print(s);
            } catch (Exception e) {
            }%>
    </body>
</html>
