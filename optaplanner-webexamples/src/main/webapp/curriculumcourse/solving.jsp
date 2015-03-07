<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>OptaPlanner webexamples: Curriculum course</title>
  <link href="../twitterbootstrap/css/bootstrap.css" rel="stylesheet">
  <link href="../twitterbootstrap/css/bootstrap-responsive.css" rel="stylesheet">
  <link href="../website/css/optaplannerWebexamples.css" rel="stylesheet">
  <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
  <!--[if lt IE 9]>
  <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->

  <!-- HACK to refresh this page automatically every 2 seconds -->
  <!-- TODO: it should only refresh the image -->
  <c:if test="${solver.solving == false}">
    <meta http-equiv="REFRESH" content="0;url=terminated.jsp"/>
  </c:if>
  <meta http-equiv="REFRESH" content="2;url=solving.jsp"/>
</head>
<body>
<div class="container-fluid">
    <div class="row-fluid">
        <div class="span10">
            <header class="main-page-header">
                <h1>Curriculum Course</h1>
            </header>
            <p>Çözüm başlatıldı her 2 saniyede bir sayfa yenilecenek</p>
            <p>Deger: <c:out value="${solver.solving}" /></p>
            <div>
             <button class="btn" onclick="window.location.href='terminateEarly.jsp'"><i class="icon-stop"></i> Erkenden Sonlandır</button>
            </div>
            <jsp:include page="curriculumCoursePage.jsp" />  
        </div>
    </div>
</div>
        
<script src="../twitterbootstrap/js/jquery.js"></script>
<script src="../twitterbootstrap/js/bootstrap.js"></script>
</body>
</html>
