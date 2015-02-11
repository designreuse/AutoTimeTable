<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page import="org.optaplanner.webexamples.curriculumcourse.CurriculumCourseWebAction"%>


<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>OptaPlanner webexamples: Curriculum Course</title>
  <link href="../twitterbootstrap/css/bootstrap.css" rel="stylesheet">
  <link href="../twitterbootstrap/css/bootstrap-responsive.css" rel="stylesheet">
  <link href="../website/css/optaplannerWebexamples.css" rel="stylesheet">
  <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
  <!--[if lt IE 9]>
  <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
  <![endif]-->
</head>
<body>

<div class="container-fluid">
  <div class="row-fluid">
    
    <div class="span10">
      <header class="main-page-header">
        <h1>Curriculum course</h1>
      </header>
      <p>Kursları Gir.</p>
      <p>A dataset has been loaded.</p>
      <div>
        <button class="btn" onclick="window.location.href='solve.jsp'">
          <i class="icon-play"></i> Problemi çözün
        </button>
      </div>
      <jsp:include page="curriculumCoursePage.jsp"/>
    </div>
  </div>
</div>

<script src="../twitterbootstrap/js/jquery.js"></script>
<script src="../twitterbootstrap/js/bootstrap.js"></script>
</body>
</html>
