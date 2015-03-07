<%-- 
    Document   : index
    Created on : 15.Şub.2015, 19:02:21
    Author     : gurhan
--%>

<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="org.apache.shiro.subject.Subject"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% 
    System.out.println("Ana index e geldi buradan gitti");
    Subject currentUser = SecurityUtils.getSubject();
    if(currentUser.hasRole("admin")) {
        response.sendRedirect("curriculumcourse/index.jsp");
    } else if (currentUser.hasRole("guest")) {
        response.sendRedirect("guest/index.jsp");
    } else {
        response.sendRedirect("login.jsp");
    }
        
%>
