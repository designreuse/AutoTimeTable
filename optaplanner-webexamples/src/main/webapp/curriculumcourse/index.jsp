<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>OptaPlanner webexamples</title>
        <link href="<%=application.getContextPath()%>/twitterbootstrap/css/bootstrap.css" rel="stylesheet" />
        <link href="<%=application.getContextPath()%>/twitterbootstrap/css/bootstrap.min.css" rel="stylesheet" />
        <link href="<%=application.getContextPath()%>/twitterbootstrap/css/bootstrap-responsive.css" rel="stylesheet" />
        <link href="<%=application.getContextPath()%>/website/css/optaplannerWebexamples.css" rel="stylesheet"/>
        <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
        <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
    </head>

    <body style="padding-left: 40px; padding-right: 40px;">
        <jsp:useBean id="contents" class="org.optaplanner.curriculumcourse.user.Contents" scope="request">
            <jsp:setProperty name="contents" property="path" value="${pageContext.servletContext.getRealPath('/')}" />
        </jsp:useBean>
        <div class="container-fluid">
            <jsp:include page="header.jsp" />
            <div class="row-fluid">
                <header class="main-page-header">
                    ${contents.prepareContents(pageContext.servletContext)}
                    <center style="margin-top: 15px"> 
                        <img src="../website/img/atauni.png" alt="Resim Bulunamadı" class="img-circle" /> 

                        <h2>Atatürk Üniversitesi Ders Hazırlama Programı </h2>
                    </center>
                </header>

                <ul>
                    <li><a href="CurriculumCourseLoadedServlet">Ders Programını oluşturmak için tıklayın</a></li>
                </ul>

                <div class='row-fluid'>
                    <div class='span6'>
                        <center><h3 class="bg-info info-header">Çözümü Başlatılabilir İçerikler</h3></center>
                        <div class="list-group">

                            <c:forEach var="content" items="${contents.importContents}" varStatus="i">
                                <a href="CurriculumCourseLoadedServlet?content=${content}&AMP;type=new" class="list-group-item">
                                    ${content}
                                    <c:choose>
                                        <c:when test="${contents.isSolved(content) == true}">
                                            <span class="label label-success curriculumtext" >Çözüldü</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="label label-danger curriculumtext" style="">Çözülmedi</span>
                                        </c:otherwise>
                                    </c:choose>
                                </a>     
                            </c:forEach>
                        </div>
                    </div>
                    <div class='span6'>
                        <center><h3 class="info-header bg-info">Çözülmüş İçerikler</h3></center>
                        <div class="list-group">
                            <c:forEach var="content" items="${contents.exportContents}">
                                <a href="CurriculumCourseLoadedServlet?content=${content}&AMP;type=solved"  class="list-group-item">
                                    ${content}
                                </a>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script src="<%=application.getContextPath()%>/twitterbootstrap/js/jquery.js"></script>
        <script src="<%=application.getContextPath()%>/twitterbootstrap/js/bootstrap.js"></script>
    </body>
</html>
