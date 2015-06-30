<%-- 
    Document   : roomDepsEdit
    Created on : Jun 10, 2015, 2:32:30 PM
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
        <title>Öğretim Elemanı Ders Günleri Düzenleme Sayfası</title>
    </head>

    <body>
        <div class="container-fluid">
            <jsp:include page="header.jsp" />
            <div class="row-fluid">
                <header class="main-page-header" style="margin-bottom: 20px">
                    ${contents.prepareContents(pageContext.servletContext)}
                    <center style="margin-top: 15px"> 
                        <img src="../website/img/atauni.png" alt="Resim Bulunamadı" class="img-circle" /> 
                        <h2>Ders derslik bağımlılıkları</h2>
                        <h3>${course.code} ${course.name}</h3>
                        <h4>Dersi yerleştirmek istediğiniz sınıfları işaretleyiniz</h4>
                        <c:if test="${message != null}">
                            <h2><span class="label ${message.result == false ? 'label-danger' : 'label-success'} " style="width: 500px">${message.content}</span></h2>
                            </c:if>
                    </center>
                </header>
                <form method="POST" action="SaveCourseRoomDeps" class="form-inline">
                    <input type="hidden" name="courseId" value="${course.id}" />
                    <c:forEach var="room" items="${roomList}" varStatus="i">
                        <div class="checkb  ox checkbox-inline">
                            <input type="checkbox" name="isSelectedRoom-${room.code}" ${isRoomSelectArray[i.index] == true ? 'checked' :''}>${room.code}</input>
                        </div>
                    </c:forEach>
                    <div class="row">
                        <div class="col-md-6" ></div>
                        <button type="submit" class="btn btn-success btn col-md-1" >Düzenle</button>
                        <a href="CoursesViewServlet" type="submit" class="btn btn-danger col-md-1" >Geri Dön</a>
                    </div>
                </form>
            </div>

        </div>
</html>
