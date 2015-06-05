<%-- 
    Document   : teacherPenaltyPeriodEdit
    Created on : Jun 5, 2015, 8:57:40 PM
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
                        <h2>Ders Günleri Düzenleme Sayfası</h2>
                        <h3>${teacher.degree.shortName} ${teacher.name} ${teacher.surname}</h3>
                        <h4>Öğretim elemanın girmek istemediği periyotları işaretleyiniz.</h4>
                        <c:if test="${message != null}">
                            <h2><span class="label ${message.result == false ? 'label-danger' : 'label-success'} " style="width: 500px">${message.content}</span></h2>
                        </c:if>
                    </center>
                </header>
            </div>
            <form method="Post" action="SaveTeacherPeriodPenalty">
                <input type="hidden" value="${teacher.id}" name="teacherId" />
                <c:forEach items="${requestScope.dayList}" var="day">
                    <div class="col-md-2">
                        <div class="form-group">
                            <label for="isDaysPeriodPenalt${day.dayIndex}">${requestScope.dayNames[day.dayIndex]} </label><br />
                            <c:forEach items="${requestScope.timeslotList}" var="timeslot">
                                <input type="checkbox" ${periodIsPenaltyArray[day.dayIndex][timeslot.timeslotIndex]==true?'checked':''} name="isDaysPeriodPenalty-${day.dayIndex}-${timeslot.timeslotIndex}">${timeslot.timeslotIndex}</input><br />
                            </c:forEach>
                        </div>
                    </div>
                </c:forEach>
                <div class="row">
                    <div class="col-md-6" ></div>
                    <button type="submit" class="btn btn-success btn col-md-2" >Düzenle</button>
                </div>
            </form>
        </div>
    </body>
</html>
